plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mudassar.ordertracking"

    defaultConfig {
        applicationId = "com.mudassar.ordertracking"
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.base)
    implementation(projects.feature.ordertracking)
    implementation(projects.feature.chat)
    implementation(projects.chatSync)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.timber)
    ksp(libs.androidx.hilt.compiler)
}
