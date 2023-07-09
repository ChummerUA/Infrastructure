object Dependencies {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle_version}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_version}"

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.ktx_version}"
    }

    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Versions.ktor_version}"
        const val okhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor_version}"
    }

    object SqlDelight {
        const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqldelight_version}"
        const val gradle = "com.squareup.sqldelight:gradle-plugin:${Versions.sqldelight_version}"
    }

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines_version}"

    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.data_store_version}"
}
