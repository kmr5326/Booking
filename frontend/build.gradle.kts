buildscript {
    repositories {
        // 기존에 있던 저장소들
        mavenCentral()
    }
    dependencies {
        // 기존에 있던 의존성들
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.library") version "8.1.2" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}