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
        private const val root = "app.cash.sqldelight"

        const val coroutineExtensions = "$root:coroutines-extensions-jvm:${Versions.sqlDelight}"
        const val gradle = "$root:gradle-plugin:${Versions.sqlDelight}"
    }

    object KotilnX {
        private const val root = "org.jetbrains.kotlinx"

        const val coroutines =
            "$root:kotlinx-coroutines-android:${Versions.KotlinX.coroutines_version}"
        const val serialization =
            "$root:kotlinx-serialization-json:${Versions.KotlinX.jsonSerialization}"
    }

    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"


    object Compose {
        private const val root = "androidx.compose"

        const val runtime = "$root.runtime:runtime:${Versions.Compose.version}"
        const val ui = "$root.ui:ui:${Versions.Compose.version}"
        const val preview = "$root.ui:ui-tooling:${Versions.Compose.version}"
    }

    object Lifecycle {
        const val root = "androidx.lifecycle"
        const val runtime = "$root:lifecycle-runtime-ktx:${Versions.lifecycle}"
    }
}
