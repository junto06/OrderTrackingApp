plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

dependencies {
    implementation(projects.proto)
    runtimeOnly(libs.grpc.netty.shaded)
}

application {
    mainClass.set("com.mudassar.ordertracking.server.MainKt")
}
