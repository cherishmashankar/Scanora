plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)

}

android {
    namespace = "com.cherishmashankar.scanora.core.database"

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
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.room.testing)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}