plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    kotlin("plugin.serialization") version "2.0.21"
    id("org.jetbrains.kotlinx.kover") version "0.8.3"
}

kover {
    reports {
        filters {
            excludes {
                classes("*BuildConfig*", "*.di.*")
            }
        }
    }
}
