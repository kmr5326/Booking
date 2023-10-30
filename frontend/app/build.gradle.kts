

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.ssafy.booking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ssafy.booking"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":data")))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Retrofit + Gson
    implementation(Retrofit.RETROFIT)
    implementation(Retrofit.CONVERTER_GSON)
    implementation(Retrofit.CONVERTER_JAXB)

    // okHttp
//    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))
    implementation(OkHttp.OKHTTP)
    implementation(OkHttp.LOGGING_INTERCEPTOR)

    //coroutines
    implementation(Coroutines.COROUTINES)

    //by viewModel
    implementation(AndroidX.ACTIVITY)
    implementation(AndroidX.FRAGMENT)

    // hilt
    implementation(DaggerHilt.DAGGER_HILT)
    kapt(DaggerHilt.DAGGER_HILT_COMPILER)
    implementation(DaggerHilt.DAGGER_HILT_VIEW_MODEL)

//    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
//    kaptAndroidTest("com.google.dagger:hilt-compiler:2.48.1")
//
//    testImplementation("com.google.dagger:hilt-android-testing:2.48.1")
//    kaptTest("com.google.dagger:hilt-compiler:2.48.1")

    // navigation
    implementation(NavComponent.NAVIGATION_COMPOSE)
    androidTestImplementation(NavComponent.NAVIGATION_TESTING)

    // coil
    implementation(Coil.COIL)
    implementation(Coil.COIL_COMPOSE)

    // Room
//    implementation(Room.ROOM_RUNTIME)
//    implementation(Room.ROOM_COMPILER)
//    implementation(Room.ROOM_KTX)

}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}