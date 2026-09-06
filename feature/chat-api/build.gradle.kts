plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mudassar.feature.chatapi"
}

dependencies {
    api(projects.core.navigation)
}
