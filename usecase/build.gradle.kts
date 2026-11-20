plugins {
    id("jvm-convention")
    id("publishing-convention")
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinxCoroutines)
    implementation(project(":annotations"))
}
