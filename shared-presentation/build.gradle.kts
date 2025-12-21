import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.build.konfig)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.compose.multiplatform)
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

    androidTarget()
    jvm("desktop")

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
            implementation(compose.uiTooling)
            api(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(project(":shared-data"))
            implementation(project(":shared-domain"))

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.kotlin.datetime)

            implementation(libs.bundles.coil)
            implementation(libs.bundles.koin.compose)
            implementation(libs.bundles.androidx.navigation)
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
        commonTest.dependencies {
            implementation(libs.bundles.test)
            implementation(libs.androidx.paging.testing)

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
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

android {
    namespace = "com.vlohachov.shared.presentation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
}
