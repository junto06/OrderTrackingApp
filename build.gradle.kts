// Top-level build file where you can add configuration options common to all sub-projects/modules.
@file:Suppress("FunctionName")

import com.android.build.api.dsl.CommonExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
}

subprojects {
    AndroidConvention()
    JvmConvention()
    ComposeConvention()
    HiltConvention()
}

fun Project.AndroidConvention() {
    listOf("com.android.application", "com.android.library").forEach { pluginId ->
        pluginManager.withPlugin(pluginId) {
            extensions.configure<CommonExtension> {
                compileSdk {
                    version = release(libs.versions.compileSdk.get().toInt())
                }
                defaultConfig.minSdk = libs.versions.minSdk.get().toInt()
                val javaVersion = JavaVersion.toVersion(libs.versions.javaVersion.get())
                compileOptions.sourceCompatibility = javaVersion
                compileOptions.targetCompatibility = javaVersion
            }
        }
    }
}

fun Project.JvmConvention() {
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        val java = libs.versions.javaVersion.get()
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.toVersion(java)
            targetCompatibility = JavaVersion.toVersion(java)
        }
        extensions.configure<KotlinJvmProjectExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(java))
            }
        }
    }
}

fun Project.ComposeConvention() {
    pluginManager.withPlugin("org.jetbrains.kotlin.plugin.compose") {
        dependencies {
            add("api", platform(libs.androidx.compose.bom))
        }
        tasks.withType<KotlinCompilationTask<*>>().configureEach {
            compilerOptions {
                freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
            }
        }
    }
}

fun Project.HiltConvention() {
    pluginManager.withPlugin("com.google.dagger.hilt.android") {
        pluginManager.apply("com.google.devtools.ksp")
        dependencies {
            add("implementation", libs.hilt.android)
            add("ksp", libs.hilt.android.compiler)
        }
    }
}
