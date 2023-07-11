plugins {
    id(Plugins.Android.library)
    id(Plugins.JetBrains.android)
    id(Plugins.sqlDelight)
}

android {
    namespace = "${ConfigData.namespaceRoot}.db"

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
    implementation(Dependencies.SqlDelight.androidDriver)
    implementation(Dependencies.SqlDelight.coroutineExtensions)
    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.coroutines)

    implementation(project(":infrastructure:usecase"))
}