plugins {
    id(Plugins.Android.library)
    id(Plugins.JetBrains.android)
}

android {
    namespace = "${ConfigData.namespaceRoot}.network"

    defaultConfig {
        compileSdk = ConfigData.compileSdk
        minSdk = ConfigData.minSdk

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = ConfigData.javaVersion
        targetCompatibility = ConfigData.javaVersion
    }
    kotlinOptions {
        jvmTarget = ConfigData.jvmTarget
    }
}

dependencies {
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.Ktor.okhttp)
    implementation(Dependencies.coroutines)

    implementation(project(":infrastructure:usecase"))
}