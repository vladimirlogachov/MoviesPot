import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.build.konfig)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.detekt)
}

buildkonfig {
    packageName = "com.vlohachov.shared.presentation"
    objectName = "BuildConfig"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "VERSION_NAME",
            System.getenv("VERSION_NAME") ?: "1.0.0"
        )
    }
}

detekt {
    source.setFrom(
        "src/commonMain/kotlin",
        "src/commonTest/kotlin",
        "src/androidMain/kotlin",
        "src/iosMain/kotlin",
        "src/desktopMain/kotlin",
        "src/wasmJsMain/kotlin"
    )
}

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    // TODO("uncomment when androidx.paging:paging-common supports 'wasm' target")
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//    }
    jvm("desktop")
    androidLibrary {
        namespace = "com.vlohachov.shared.presentation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        withJava()
        withHostTestBuilder {}
            .configure {}
        androidResources {
            enable = true
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared-presentation"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
            api(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(projects.sharedData)
            implementation(projects.sharedDomain)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.kotlin.datetime)

            implementation(libs.bundles.coil)
            implementation(libs.bundles.koin.compose)
            implementation(libs.bundles.androidx.navigation)
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.compose.ui)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
        }
        commonTest.dependencies {
            implementation(libs.bundles.test)
            implementation(libs.androidx.paging.testing)
            implementation(libs.compose.ui.test)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        val desktopMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        val desktopTest by getting
        desktopTest.dependencies {
            implementation(libs.kotlin.corutiens.swing)
            implementation(compose.desktop.currentOs)
        }
    }
}
