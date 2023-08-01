plugins {
    id(Plugins.Maven.publish)
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

    publishing {
        multipleVariants("custom") {
            includeBuildTypeValues(
                BuildType.debug,
                BuildType.release
            )
            withJavadocJar()
        }
    }
}

afterEvaluate {
    android.libraryVariants.forEach { variant ->
        publishing.publications {
            create<MavenPublication>(variant.name) {
                pom {
                    groupId = ConfigData.namespaceRoot
                    artifactId = "db"
                    version = ConfigData.versionName
                }

                from(components[ConfigData.Components.release])
            }
        }
    }
}

dependencies {
    api(Dependencies.SqlDelight.androidDriver)
    api(Dependencies.SqlDelight.coroutineExtensions)
    api(Dependencies.AndroidX.core)
    api(Dependencies.KotilnX.coroutines)

    api(project(":infrastructure:usecase"))
}