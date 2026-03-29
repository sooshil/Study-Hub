plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sukajee.core.database"
    compileSdk = 36
    defaultConfig { minSdk = 29 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.android)
    implementation(project(":core:common"))
}
