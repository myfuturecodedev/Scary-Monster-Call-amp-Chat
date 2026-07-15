plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.futurecode.scarymonstercallchat"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.futurecode.scarymonstercallchat"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true // Also removes unused resources
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)



    //facebook ads
    implementation("com.facebook.android:audience-network-sdk:6.21.0")

    //google ads
//    implementation("com.google.android.gms:play-services-ads:25.2.0")
    implementation("com.google.android.gms:play-services-ads:23.6.0") // Check for the latest 2026 version
    implementation("com.google.ads.mediation:facebook:6.16.0.0")
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    // Lifecycle - Aligned to stable 2.8.7 to prevent compiler crashes
    val lifecycle_version = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.4")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")

    //google billing
    implementation("com.android.billingclient:billing-ktx:8.3.0")

    // Retrofit & Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Material components
    implementation("com.google.android.material:material:1.12.0")

    // Lottie dependency
    implementation("com.airbnb.android:lottie:6.4.1")

    // glide - Downgraded to stable 4.x
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // CameraX
//    val camera_version = "1.3.4"
    val camera_version = "1.4.0" // Updated from 1.3.4
    implementation("androidx.camera:camera-core:${camera_version}")
    implementation("androidx.camera:camera-camera2:${camera_version}")
    implementation("androidx.camera:camera-lifecycle:${camera_version}")
    implementation("androidx.camera:camera-view:${camera_version}")

    // Essential for CameraX and K2 compiler stability
    // implementation("com.google.guava:listenablefuture:1.0")

    implementation("com.google.guava:guava:31.1-android")

    //lottie
    implementation("com.airbnb.android:lottie:6.7.1")


    //Work manager
    implementation("androidx.work:work-runtime-ktx:2.11.2")

    //firebase analytics
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:34.13.0"))

    implementation("com.intuit.sdp:sdp-android:1.1.1")

}