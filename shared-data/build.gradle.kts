import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.build.konfig)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.detekt)
}

val secretsPropertiesFile: File = project.file("secrets.properties")
val secretsProperties = Properties().apply {
    load(FileInputStream(secretsPropertiesFile))
}

buildkonfig {
    packageName = "com.vlohachov.shared.data"
    objectName = "TmdbConfig"

    val baseUrl = secretsProperties["base.url"] as String
    val baseImageUrl = secretsProperties["base.image.url"] as String
    val apiKey = secretsProperties["api.key"] as String
    val accessToken = secretsProperties["access.token"] as String

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "HOST", baseUrl)
        buildConfigField(FieldSpec.Type.STRING, "BASE_IMAGE_URL", baseImageUrl)
        buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)
        buildConfigField(FieldSpec.Type.STRING, "ACCESS_TOKEN", accessToken)
    }
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

    androidTarget()
    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared-data"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            api(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(project(":shared-domain"))

            implementation(libs.kotlin.corutiens.core)

            api(libs.bundles.ktor.data)
            api(libs.multiplatform.settings.no.arg)
        }
        commonTest.dependencies {
            implementation(libs.bundles.test)
            implementation(libs.ktor.client.mock)
        }
    }
}

android {
    namespace = "com.vlohachov.shared.data"
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
