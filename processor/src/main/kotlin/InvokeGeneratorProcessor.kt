package com.chummer.infrastructure.processor

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
        val invokeCandidates = useCaseClass
            .getAllFunctions()
            .filter { it.simpleName.asString() == "invoke" && it.parameters.size == 1 }
            .toList()

        val invokeFunction = invokeCandidates.toInvokeFunctionOrNull(useCaseClass) ?: return
        val resolved = invokeFunction.asMemberOf(useCaseType)
        val inputType = resolved.parameterTypes.singleOrNull()
        val outputType = resolved.returnType

        if (inputType == null || outputType == null) {
            logger.error("Failed to resolve invoke signature for $useCaseName", useCaseClass)
            return
        }

        val inputDeclaration = inputType.declaration as? KSClassDeclaration
        if (inputDeclaration == null) {
            logger.warn("Input type in $useCaseName($inputType) is not a concrete class, skipped")
            return
        }

        val params = inputDeclaration.primaryConstructor?.parameters.orEmpty()
        if (params.isEmpty() || params.any { it.name == null }) return

        val isSuspend = Modifier.SUSPEND in invokeFunction.modifiers

        val useCaseTypeName = useCaseClass.toClassName()
        val argsStatement = params.joinToString {
            "${it.name!!.asString()} = ${it.name!!.asString()}"
        }

        val spec = FunSpec.builder("invoke")
            .addModifiers(KModifier.OPERATOR)
            .apply { if(isSuspend) addModifiers(KModifier.SUSPEND) }
            .receiver(useCaseTypeName)
            .apply {
                params.forEach {
                    addParameter(
                        ParameterSpec.builder(it.name!!.asString(), it.type.toTypeName()).build()
                    )
                }
            }
            .returns(outputType.toTypeName())
            .addStatement("return invoke(%T($argsStatement))", inputDeclaration.toClassName())
            .build()

        generateFile(
            useCaseClass = useCaseClass,
            useCaseTypeName = useCaseTypeName,
            invokeSpec = spec,
            inputDeclaration = inputDeclaration
        )
    }

    private fun List<KSFunctionDeclaration>.toInvokeFunctionOrNull(
        useCaseClass: KSClassDeclaration
    ): KSFunctionDeclaration? = when (size) {
        1 -> first()
        0 -> {
            logger.error("No invoke found", useCaseClass)
            null
        }
        else -> {
            logger.error("Ambiguous invoke", useCaseClass)
            null
        }
    }

    private fun generateFile(
        useCaseClass: KSClassDeclaration,
        useCaseTypeName: ClassName,
        invokeSpec: FunSpec,
        inputDeclaration: KSClassDeclaration,
    ) {
        val fileSpec = FileSpec
            .builder(useCaseTypeName.packageName, "${useCaseClass.simpleName.asString()}_Invoke")
            .addFunction(invokeSpec)
            .build()

        val originatingFiles = listOfNotNull(useCaseClass.containingFile, inputDeclaration.containingFile)
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

class InvokeGeneratorProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        InvokeGenerateProcessor(environment.codeGenerator, environment.logger)
}

private const val GENERATE_INVOKE_FQN = "com.chummer.infrastructure.annotations.GenerateInvoke"
