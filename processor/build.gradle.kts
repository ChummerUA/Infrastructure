plugins {
    id("jvm-convention")
    id("publishing-convention")
}

dependencies {
    implementation(project(":annotations"))
    compileOnly(libs.ksp)
    implementation(libs.kotlinPoet)
    implementation(libs.kotlinPoet.ksp)
}
