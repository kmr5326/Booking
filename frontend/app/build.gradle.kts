plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
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
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.2.0")
    implementation("androidx.compose.ui:ui-text-android:1.5.4")
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
    implementation(Retrofit.CONVERTER_SCALARS)

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

    // google
    implementation(Google.GOOGLE_AUTH)

    implementation ("androidx.compose.runtime:runtime:1.3.3")
    implementation ("androidx.compose.runtime:runtime-livedata:1.3.3")
    implementation ("androidx.compose.runtime:runtime-rxjava2:1.3.3")

    implementation ("com.google.android.gms:play-services-location:21.0.1")

    // Paging
    implementation ("androidx.paging:paging-runtime:3.1.1")

    // Room
    implementation(Room.ROOM_RUNTIME)
    annotationProcessor(Room.ROOM_COMPILER)
    kapt(Room.ROOM_COMPILER)
    implementation(Room.ROOM_KTX)

    // Moshi
    implementation("com.squareup.moshi:moshi:1.15.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")
    implementation(platform("com.google.firebase:firebase-bom:32.4.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    // 카카오 로그인
    implementation ("com.kakao.sdk:v2-user:2.17.0") // 카카오 로그인

    //STOMP
    implementation ("com.github.bishoybasily:stomp:2.0.5")

    // serialize
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.12.3") // 버전은 적절하게 조절
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.3")

    // m3
    implementation("androidx.compose.material3:material3-window-size-class:1.0.1")
    implementation("androidx.compose.material3:material3:1.0.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

ksp {
    arg("moshi.kotlin.codegen.metadata", "true")
}


