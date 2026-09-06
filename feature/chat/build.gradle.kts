plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mudassar.feature.chat"

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(projects.feature.chatApi)
    api(projects.feature.base)
    implementation(projects.core.rpc)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
