plugins {
    id(Plugins.Maven.publish)
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
                    artifactId = "network"
                    version = ConfigData.versionName
                }

                from(components[ConfigData.Components.release])
            }
        }
    }
}

dependencies {
    api(Dependencies.Ktor.core)
    api(Dependencies.Ktor.okhttp)
    api(Dependencies.coroutines)

    api(project(":infrastructure:usecase"))
}