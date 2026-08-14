import org.gradle.api.JavaVersion

object ConfigData {

    const val compileSdk = 34
    const val minSdk = 24

    val javaVersion = JavaVersion.VERSION_21
    const val jvmTarget = "21"

    const val namespaceRoot = "com.chummer.infrastructure"

    const val versionName = "1.0.0"

    object Components {
        const val release = "release"
        const val java = "java"
    }
}

object BuildType {
    const val release = "release"
    const val debug = "debug"
}