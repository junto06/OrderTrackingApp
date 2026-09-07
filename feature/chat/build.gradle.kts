plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
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
    implementation(projects.core.base)
    implementation(projects.proto)
    implementation(projects.chatDomain)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
