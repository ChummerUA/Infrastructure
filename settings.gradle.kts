pluginManagement {
    repositories {
        mavenLocal()
//        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenLocal {
            name = "chummer"
        }
//        google()
        mavenCentral()
    }
}
rootProject.name = "Infrastructure"
//include(":infrastructure:usecase")
//include(":infrastructure:preferences")
//include(":infrastructure:network")
//include(":infrastructure:compose-utils")
include("usecase")
include("db")
include("annotations")
include("processor")