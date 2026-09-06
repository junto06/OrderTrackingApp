plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.mudassar.feature.base"

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(projects.core.navigation)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.compose.ui)
    api(libs.androidx.compose.material3)
}
