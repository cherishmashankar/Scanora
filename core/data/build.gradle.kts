plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.cherishmashankar.scanora.core.data"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 31
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:database"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}