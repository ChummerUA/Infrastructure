package com.chummer.infrastructure.processor

import com.google.devtools.ksp.findActualType
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class InvokeGenerateProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver
            .getNewFiles()
            .flatMap { it.declarations }
            .flatMap { it.allClassDeclarations() }
            .filter { it.isConcreteMarkedUseCase() }
            .forEach(::generateFor)

        return emptyList()
    }

    private fun generateFor(useCaseClass: KSClassDeclaration) {
        val useCaseName = useCaseClass.simpleName.asString()
        val useCaseType = useCaseClass.asType(emptyList())
        val candidates = useCaseClass
            .getAllFunctions()
            .filter { it.simpleName.asString() == "execute" && it.parameters.size == 1 && it.extensionReceiver == null }
            .toList()
            .mostDerived()

        val executeFunction = candidates.toExecuteFunctionOrNull(useCaseClass) ?: return
        val resolved = executeFunction.asMemberOf(useCaseType)
        val inputType = resolved.parameterTypes.singleOrNull()
        val outputType = resolved.returnType

        if (inputType == null || outputType == null) {
            logger.error("Failed to resolve execute signature for $useCaseName", useCaseClass)
            return
        }

        val inputDeclaration: KSClassDeclaration? = when(val declaration = inputType.declaration) {
            is KSClassDeclaration -> declaration
            is KSTypeAlias -> declaration.findActualType()
            else -> null
        }

        val isUnit = inputDeclaration?.qualifiedName?.asString() == "kotlin.Unit" && !inputType.isMarkedNullable
        val isData = Modifier.DATA in inputDeclaration?.modifiers.orEmpty() && !inputType.isMarkedNullable
        val (params, forwardedArg) = when {
            isUnit -> emptyList<ParameterSpec>() to CodeBlock.of("Unit")
            isData -> {
                val params = inputDeclaration?.primaryConstructor?.parameters.orEmpty()
                if (params.isEmpty() || params.any { it.name == null }) {
                    logger.warn("Bad parameters", useCaseClass)
                    return
                }
                val inputClass = inputDeclaration!!.toClassName()
                val lastRequiredIndex = params.indexOfLast { !it.hasDefault }
                val firstOptionalIndex = params.indexOfFirst { it.hasDefault }
                val specs = params.mapIndexed { i, p ->
                    val name = p.name!!.asString()
                    val builder = ParameterSpec.builder(name, p.type.toTypeName())
                    if (p.hasDefault) {
                        if (i > lastRequiredIndex) {
                            val earlier = params.take(i).joinToString { "${it.name!!.asString()} = ${it.name!!.asString()}" }
                            builder.defaultValue("%T($earlier).%N", inputClass, name)
                        }
                    } else if (firstOptionalIndex != -1 && i > firstOptionalIndex) {
                        logger.warn(
                            "Default of '$name' in ${inputDeclaration.simpleName.asString()} is not propagated: " +
                                    "a required parameter follows it",
                            useCaseClass
                        )
                    }
                    builder.build()
                }
                val argsStatement = params.joinToString {
                    "${it.name!!.asString()} = ${it.name!!.asString()}"
                }
                specs to CodeBlock.of("%T($argsStatement)", inputClass)
            }

            else -> {
                val name = executeFunction.parameters.first().name?.asString() ?: "input"
                listOf(ParameterSpec.builder(name, inputType.toTypeName()).build()) to CodeBlock.of("%N", name)
            }
        }

        val isSuspend = Modifier.SUSPEND in executeFunction.modifiers

        val useCaseTypeName = useCaseClass.toClassName()

        val spec = FunSpec.builder("invoke")
            .addModifiers(KModifier.OPERATOR)
            .apply { if (isSuspend) addModifiers(KModifier.SUSPEND) }
            .receiver(useCaseTypeName)
            .addParameters(params)
            .returns(outputType.toTypeName())
            .addStatement("return execute(%L)", forwardedArg)
            .build()
        logger.info("Invoke generated: $spec", useCaseClass)

        generateFile(
            useCaseClass = useCaseClass,
            useCaseTypeName = useCaseTypeName,
            invokeSpec = spec,
            inputDeclaration = inputDeclaration
        )
    }

    private fun List<KSFunctionDeclaration>.toExecuteFunctionOrNull(
        useCaseClass: KSClassDeclaration
    ): KSFunctionDeclaration? = when (size) {
        1 -> first()
        0 -> {
            logger.error("No execute found", useCaseClass)
            null
        }

        else -> {
            logger.error(
                "Ambiguous execute:\n\t${this.joinToString("\n\t") { 
                    it.qualifiedName?.asString() ?: it.packageName.asString() }
                }",
                useCaseClass
            )
            null
        }
    }

    private fun generateFile(
        useCaseClass: KSClassDeclaration,
        useCaseTypeName: ClassName,
        invokeSpec: FunSpec,
        inputDeclaration: KSClassDeclaration?,
    ) {
        val fileSpec = FileSpec
            .builder(useCaseTypeName.packageName, "${useCaseClass.simpleName.asString()}_Invoke")
            .addFunction(invokeSpec)
            .build()

        val inputFile = inputDeclaration
            .takeIf { it?.origin == Origin.KOTLIN || it?.origin == Origin.JAVA }
            ?.containingFile

        val originatingFiles = listOfNotNull(useCaseClass.containingFile, inputFile)
            .distinct()
            .toTypedArray()

        fileSpec.writeTo(codeGenerator, Dependencies(aggregating = false, *originatingFiles))
    }
}

private fun KSDeclaration.allClassDeclarations(): Sequence<KSClassDeclaration> = sequence {
    if (this@allClassDeclarations is KSClassDeclaration) {
        yield(this@allClassDeclarations)
        declarations.forEach {
            yieldAll(it.allClassDeclarations())
        }
    }
}

private fun KSClassDeclaration.isConcreteMarkedUseCase(): Boolean {
    if (classKind != ClassKind.CLASS && classKind != ClassKind.OBJECT) return false
    if (Modifier.ABSTRACT in modifiers) return false
    return getAllSuperTypes().any { superType ->
        superType.declaration.annotations.any {
            it.annotationType.resolve().declaration.qualifiedName?.asString() == GENERATE_INVOKE_FQN
        }
    }
}

private fun List<KSFunctionDeclaration>.mostDerived(): List<KSFunctionDeclaration> {
    val overridden = this.mapNotNull { it.findOverridee()?.key() }
    return filter { it.key() !in overridden }.distinctBy { it.key() }
}

private fun KSDeclaration.key() =
    "${parentDeclaration?.qualifiedName?.asString()}#${simpleName.asString()}"

class InvokeGeneratorProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        InvokeGenerateProcessor(environment.codeGenerator, environment.logger)
}

private const val GENERATE_INVOKE_FQN = "com.chummer.infrastructure.annotations.GenerateInvoke"
