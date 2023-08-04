import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.Maven.publish)
    id(Plugins.javaLibrary)
    id(Plugins.JetBrains.jvm)
    id(Plugins.kotlin)
}

java {
    sourceCompatibility = ConfigData.javaVersion
    targetCompatibility = ConfigData.javaVersion
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = ConfigData.jvmTarget
}


publishing {
    publications {
        create<MavenPublication>("default") {
            pom {
                groupId = ConfigData.namespaceRoot
                artifactId = "usecase"
                version = ConfigData.versionName
            }
            from(components[ConfigData.Components.java])
        }
    }
}

dependencies {
    api(Dependencies.KotilnX.coroutines)
}
