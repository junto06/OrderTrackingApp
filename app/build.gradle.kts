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
    implementation(projects.feature.ordertracking)
    implementation(projects.feature.chat)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.core.ktx)
}
