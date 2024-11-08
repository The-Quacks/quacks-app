plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.quacks_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quacks_app"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.firebase.bom)
    implementation(libs.firebase.firestore)
    implementation(libs.core)
    implementation(libs.javase)
    implementation(libs.firebase.storage.v2020)
    implementation(libs.play.services.code.scanner)
//    implementation("com.google.firebase:firebase-firestore:24.1.0") {
//        exclude(group = "com.google.firebase", module = "firebase-common")
//    }
    dependencies {
        implementation(libs.glide)
        annotationProcessor(libs.compiler)
    }
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}