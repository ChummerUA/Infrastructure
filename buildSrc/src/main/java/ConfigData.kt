import org.gradle.api.JavaVersion

object ConfigData {
    const val buildToolsVersion = "30.0.3"

    const val compileSdk = 33
    const val targetSdkVersion = 33
    const val minSdk = 24

    val javaVersion = JavaVersion.VERSION_1_8
    const val jvmTarget = "1.8"

    const val versionCode = 1
    const val versionName = "1.0"

    const val namespaceRoot = "com.chummer.infrastructure"
}
