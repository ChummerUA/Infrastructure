import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id(Plugins.Maven.publish)
    id(Plugins.Android.library)
    id(Plugins.JetBrains.android)
}

android {
    namespace = "com.chummer.compose_utils"

    defaultConfig {
        compileSdk = ConfigData.compileSdk
        minSdk = ConfigData.minSdk
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = ConfigData.javaVersion
        targetCompatibility = ConfigData.javaVersion
    }
    kotlinOptions {
        jvmTarget = ConfigData.jvmTarget
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.compiler
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
                    artifactId = "compose-utils"
                    version = ConfigData.versionName
                }

                from(components[ConfigData.Components.release])
            }
        }
    }
}

dependencies {
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.preview)
    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Lifecycle.runtime)
}
