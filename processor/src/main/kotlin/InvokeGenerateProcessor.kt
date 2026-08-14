import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class InvokeGenerateProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(GENERATE_INVOKE_FQN)
            .filterIsInstance<KSClassDeclaration>()
            .toList()
        val deferred = symbols.filterNot { it.validate() }
        symbols
            .filter { it.validate() }
            .forEach(::generateFor)

        return deferred
    }

    private fun generateFor(useCaseClass: KSClassDeclaration) {
        val useCaseType = useCaseClass
            .getAllSuperTypes()
            .find { it.declaration.qualifiedName?.asString() == USECASE_FQN }

        if (useCaseType == null) {
            logger.error(
                "@GenerateInvoke requires the class to transitively implement $USECASE_FQN<Input, Output>",
                useCaseClass,
            )
            return
        }

        val inputType = useCaseType.arguments.getOrNull(0)?.type?.resolve()
        val outputType = useCaseType.arguments.getOrNull(1)?.type?.resolve()
        if (inputType == null || outputType == null) {
            logger.error("Could not resolve Input/Output types", useCaseClass)
            return
        }

        val inputDeclaration = (inputType.declaration as? KSClassDeclaration) ?: return
        val parameters = inputDeclaration.primaryConstructor?.parameters.orEmpty()
        if (parameters.isEmpty()) return

        generateFlattenedInvoke(
            useCaseClass = useCaseClass,
            inputDeclaration = inputDeclaration,
            parameters = parameters,
            outputTypeName = outputType.toTypeName()
        )
    }

    private fun generateFlattenedInvoke(
        useCaseClass: KSClassDeclaration,
        inputDeclaration: KSClassDeclaration,
        parameters: List<KSValueParameter>,
        outputTypeName: TypeName,
    ) {
        if (parameters.any { it.name == null }) {
            logger.error("Input class has an unnamed constructor parameters", inputDeclaration)
            return
        }

        val useCaseTypeName = useCaseClass.toClassName()

        val args = parameters.joinToString { p ->
            val name = p.name!!.asString()
            "$name = $name"
        }

        val invokeSpec = FunSpec
            .builder("invoke")
            .addModifiers(KModifier.SUSPEND, KModifier.OPERATOR)
            .receiver(useCaseTypeName)
            .apply {
                parameters.forEach { p ->
                    addParameter(
                        ParameterSpec
                            .builder(p.name!!.asString(), p.type.toTypeName())
                            .build()
                    )
                }
            }
            .returns(outputTypeName)
            .addStatement("return invoke(%T$args)", inputDeclaration.toClassName())
            .build()

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

class InvokeGeneratorProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        InvokeGenerateProcessor(environment.codeGenerator, environment.logger)
}

private const val GENERATE_INVOKE_FQN = "com.chummer.infrastructure.annotations.GenerateInvoke"
private const val USECASE_FQN = "com.chummer.infrastructure.usecase"
