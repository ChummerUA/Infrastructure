object Dependencies {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.ktx}"
    }

    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val okhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
    }

    object SqlDelight {
        const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
        const val coroutineExtensions = "com.squareup.sqldelight:coroutines-extensions-jvm:${Versions.sqlDelight}"
        const val gradle = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
    }

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"
}
