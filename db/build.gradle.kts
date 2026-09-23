import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.Maven.publish)
    id(Plugins.javaLibrary)
    id(Plugins.sqlDelight)
    id(Plugins.kotlin)
    kotlin("jvm") version Versions.kotlin
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(ConfigData.jvmTarget))
}

java {
    sourceCompatibility = ConfigData.javaVersion
    targetCompatibility = ConfigData.javaVersion
}

publishing {
    publications {
        create<MavenPublication>("default") {
            pom {
                groupId = ConfigData.namespaceRoot
                artifactId = "db"
                version = ConfigData.versionName
            }

            from(components[ConfigData.Components.java])
        }
    }
}


group = "com.chummer.infrastructure"

dependencies {
    api(Dependencies.SqlDelight.coroutineExtensions)
    api(Dependencies.KotilnX.coroutines)

    api(project(":usecase"))
}

kotlin {
    jvmToolchain(21)
}
