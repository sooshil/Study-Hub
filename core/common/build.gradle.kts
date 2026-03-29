plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.sukajee.core.common"
    compileSdk = 36
    defaultConfig { minSdk = 29 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}

