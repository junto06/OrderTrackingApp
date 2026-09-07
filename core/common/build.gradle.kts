plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mudassar.core.common"
}

dependencies {
    api(projects.core.navigation)
    implementation(projects.core.base)
    implementation(projects.proto)
    implementation(libs.grpc.okhttp)
    implementation(libs.timber)
}
