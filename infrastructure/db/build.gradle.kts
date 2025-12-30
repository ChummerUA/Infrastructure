import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.Maven.publish)
    id(Plugins.javaLibrary)
    id(Plugins.JetBrains.jvm)
    id(Plugins.sqlDelight)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = ConfigData.jvmTarget
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

dependencies {
    api(Dependencies.SqlDelight.coroutineExtensions)
    api(Dependencies.KotilnX.coroutines)

    api(project(":infrastructure:usecase"))
}