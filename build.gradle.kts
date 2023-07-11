// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenLocal()
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
    id(Plugins.Android.application) version Versions.gradle apply false
    id(Plugins.Android.library) version Versions.gradle apply false
    id(Plugins.JetBrains.android) version Versions.kotlin apply false
    id(Plugins.JetBrains.jvm) version Versions.kotlin apply false
}