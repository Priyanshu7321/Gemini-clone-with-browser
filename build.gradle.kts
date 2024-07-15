// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    val objectboxVersion by extra("3.8.0") // For KTS build scripts

    repositories {
        mavenCentral()
        // Note: 2.9.0 and older are available on jcenter()
    }

    dependencies {
        // Android Gradle Plugin 4.1.0 or later supported.
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("io.objectbox:objectbox-gradle-plugin:$objectboxVersion")
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
}