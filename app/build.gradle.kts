plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.app_mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app_mobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation(libs.recyclerview)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
    implementation ("com.google.code.gson:gson:2.8.9")
//recognition
    implementation ("com.google.mlkit:text-recognition:16.0.0")
    //retfofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    // Converter for JSON parsing with Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // For using OkHttp (optional for logging network requests)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
//    //camera
//    implementation ("androidx.camera:camera-core:1.0.0")
//    implementation ("androidx.camera:camera-camera2:1.0.0")
//    implementation ("androidx.camera:camera-lifecycle:1.0.0")
//    implementation ("androidx.camera:camera-view:1.0.0")

    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-ui:2.7.4")
    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime:2.6.1")
    implementation ("com.google.android.material:material:1.10.0")

    implementation ("com.google.android.material:material:1.2.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.2")
    // RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.3.1")

    // Kotlin coroutine support for Room
    implementation ("androidx.room:room-ktx:2.5.2")
}