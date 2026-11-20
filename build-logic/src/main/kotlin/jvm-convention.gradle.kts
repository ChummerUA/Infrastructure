import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm")
}

val libs = the<VersionCatalogsExtension>().named("libs")

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_0
        apiVersion = KotlinVersion.KOTLIN_2_0

    }
}

dependencies {
    implementation(libs.findLibrary("kotlin-bom").get())
}

group = "com.chummer.infrastructure"
version = "1.0.1"
