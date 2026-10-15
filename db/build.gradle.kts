plugins {
    id("jvm-convention")
    id("publishing-convention")
    alias(libs.plugins.sqlDelight)
}

dependencies {
    api(libs.sqlDelight.coroutineExtensions)
    api(libs.kotlinxCoroutines)

    api(project(":usecase"))
}
