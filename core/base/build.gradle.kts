plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mudassar.core.base"
}

dependencies {
    implementation(libs.hilt.android)
}
