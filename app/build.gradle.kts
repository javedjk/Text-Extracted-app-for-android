plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.textextractorfromimages"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.textextractorfromimages"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // AndroidX and UI Libraries
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    // Camera Core Library
    implementation("androidx.camera:camera-core:1.4.0")

    // Unit Testing Libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // UCrop for Image Cropping
    implementation("com.github.yalantis:ucrop:2.2.6")

    // Android Image Cropper Library
    implementation("com.vanniktech:android-image-cropper:4.6.0")

    // Image Picker Library
    implementation("com.github.dhaval2404:imagepicker:2.1")

    // ML Kit for Text Recognition
    implementation("com.google.mlkit:text-recognition:16.0.1")
}

// Ensuring project syncs correctly
configurations.all {
    resolutionStrategy {
        force("androidx.core:core-ktx:1.10.0") // Forcing latest core-ktx for better compatibility
    }
}
