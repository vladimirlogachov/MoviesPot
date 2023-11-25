//import io.gitlab.arturbosch.detekt.Detekt
//import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply (false)
    alias(libs.plugins.android.library) apply (false)
    alias(libs.plugins.kotlin.android) apply (false)
    alias(libs.plugins.kotlin.jvm) apply (false)
    alias(libs.plugins.devtools.ksp) apply (false)
    alias(libs.plugins.detekt) apply (false)
}

//allprojects {
//    detekt {
//        buildUponDefaultConfig = true // preconfigure defaults
//        allRules = true // activate all available (even unstable) rules.
//    }
//
//    tasks.withType(Detekt).configureEach {
//        jvmTarget = JavaVersion.VERSION_1_8
//    }
//    tasks.withType(DetektCreateBaselineTask).configureEach {
//        jvmTarget = JavaVersion.VERSION_1_8
//    }
//}
