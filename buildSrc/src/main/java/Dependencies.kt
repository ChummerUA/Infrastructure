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
        private const val root = "com.squareup.sqldelight"

        const val androidDriver = "$root:android-driver:${Versions.sqlDelight}"
        const val coroutineExtensions = "$root:coroutines-extensions-jvm:${Versions.sqlDelight}"
        const val gradle = "$root:gradle-plugin:${Versions.sqlDelight}"
    }

    object KotilnX {
        private const val root = "org.jetbrains.kotlinx"

        const val coroutines = "$root:kotlinx-coroutines-android:${Versions.KotlinX.coroutines_version}"
        const val serialization = "$root:kotlinx-serialization-json:${Versions.KotlinX.jsonSerialization}"
    }

    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"
}
