plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.exammitrabykaushal"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.exammitrabykaushal"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.2"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)
    // Lifecycle
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.lifecycle.runtime.compose)
    // Room
    implementation (libs.androidx.room.runtime)
    implementation(libs.androidx.tv.material)
    annotationProcessor (libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.compose) // Critical for navigation
    // Firebase Auth
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.crashlytics.buildtools)
    // Firebase Database
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.database)
    implementation(libs.google.firebase.database.ktx)
    // Add the dependency for the Firebase Authentication library
    implementation(platform(libs.firebase.bom))
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.google.firebase.auth)
    implementation(libs.firebase.auth.ktx) // Or latest
    implementation(libs.play.services.auth)
    // For example, declare the dependencies for Firebase Authentication and Cloud Firestore
    implementation (libs.firebase.firestore)
    // Admob
    implementation(libs.play.services.ads)
    // Retrofit + Moshi
    implementation (libs.retrofit)
    implementation (libs.converter.moshi)
    // Coroutines
    implementation (libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
    // Coil
    implementation (libs.coil.compose)
    // Graphs (for Analytics using chart)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    // extend icon compose
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}