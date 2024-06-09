import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.build.konfig)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.compose)
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
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.paging.compose)
            implementation(compose.uiTooling)
            api(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(project(":shared-data"))
            implementation(project(":shared-domain"))

            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.navigation.common)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.paging.common)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlin.datetime)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose)
            api(libs.koin.core)

            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
        commonTest.dependencies {
            implementation(libs.turbine)
            implementation(libs.kotlin.test)
            implementation(libs.androidx.paging.testing)

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
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
