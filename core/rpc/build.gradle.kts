plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mudassar.core.rpc"
}

dependencies {
    api(libs.kotlinx.coroutines.core)
}
