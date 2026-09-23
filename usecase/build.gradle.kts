plugins {
    id(Plugins.Maven.publish)
    id(Plugins.javaLibrary)
    id(Plugins.kotlin)
    kotlin("jvm") version Versions.kotlin
}

group = "com.chummer.infrastructure"

kotlin {
    jvmToolchain(21)
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
