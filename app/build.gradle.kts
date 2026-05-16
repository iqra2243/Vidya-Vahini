plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.vidyavahini"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vidyavahini"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // ✅ VERY IMPORTANT (for your errors)
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {

    // ✅ Compose BOM (manages versions automatically)
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))

    // ✅ Core Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // ✅ Activity Compose
    implementation("androidx.activity:activity-compose:1.9.0")

    // ✅ Navigation (fixes NavHost, composable errors)
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ✅ Firebase BOM (fixes your previous error)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Firebase Realtime DB
    implementation("com.google.firebase:firebase-database-ktx")

    // (optional) Auth if you use it
    implementation("com.google.firebase:firebase-auth-ktx")

    // Debug tools
    debugImplementation("androidx.compose.ui:ui-tooling")
}