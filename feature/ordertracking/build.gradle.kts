plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mudassar.feature.ordertracking"

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(projects.feature.base)
    implementation(projects.core.rpc)
    implementation(projects.feature.chatApi)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.osmdroid.android)
}
