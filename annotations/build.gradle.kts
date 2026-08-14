import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.Maven.publish)
    id(Plugins.javaLibrary)
    kotlin("jvm") version "2.2.20"
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
                artifactId = "annotations"
                version = ConfigData.versionName
            }

            from(components[ConfigData.Components.java])
        }
    }
}

group = "com.chummer.infrastructure"

kotlin {
    jvmToolchain(21)
}
