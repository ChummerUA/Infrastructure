// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.gradle)
        classpath(Dependencies.kotlinGradle)
        classpath(Dependencies.SqlDelight.gradle)
    }
}

plugins {
    id(Plugins.Android.application) version Versions.gradle_version apply false
    id(Plugins.Android.library) version Versions.gradle_version apply false
    id(Plugins.JetBrains.android) version Versions.kotlin_version apply false
    id(Plugins.JetBrains.jvm) version Versions.kotlin_version apply false
}