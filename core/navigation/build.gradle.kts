plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mudassar.core.navigation"
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
}
