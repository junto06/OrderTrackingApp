plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mudassar.feature.chat.domain"
}

dependencies {
    api(libs.kotlinx.coroutines.core)
}
