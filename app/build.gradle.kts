plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lozada.pocketu"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lozada.pocketu"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Pruebas Unitarias Locales (test/)
    testImplementation(libs.junit)

    // Pruebas de Integración (androidTest/)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- DEPENDENCIAS AÑADIDAS PARA PRUEBAS DE ROOM EN MEMORIA ---
    androidTestImplementation("androidx.test:core-ktx:1.5.0") // Requerido para ApplicationProvider
    androidTestImplementation("androidx.room:room-testing:2.6.1") // Requerido para probar Room
}