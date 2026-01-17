import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.detekt)
}

detekt {
    source.setFrom(
        "src/commonMain/kotlin",
        "src/commonTest/kotlin",
    )
}

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    androidLibrary {
        namespace = "com.vlohachov.shared.domain"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        withJava()
        withHostTestBuilder {}
            .configure {}
    }
    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared-domain"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.corutiens.core)
        }
        commonTest.dependencies {
            implementation(libs.bundles.test)
        }
    }
}
