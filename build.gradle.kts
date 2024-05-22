// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.library) apply (false)
    alias(libs.plugins.android.application) apply (false)
    alias(libs.plugins.kotlin.jvm) apply (false)
    alias(libs.plugins.kotlin.android) apply (false)
    alias(libs.plugins.kotlin.compose) apply (false)
    alias(libs.plugins.kotlin.multiplatform) apply (false)
    alias(libs.plugins.devtools.ksp) apply (false)
    alias(libs.plugins.build.konfig) apply (false)
    alias(libs.plugins.compose) apply (false)
    alias(libs.plugins.detekt) apply (false)
}
