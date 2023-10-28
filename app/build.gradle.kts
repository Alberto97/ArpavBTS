import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("dagger.hilt.android.plugin")
    id("org.gradle.android.cache-fix")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "org.alberto97.arpavbts"
        namespace = "org.alberto97.arpavbts"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.5.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val secureProps = Properties()
        if (file("secure.properties").exists()) {
            file("secure.properties").inputStream().use { secureProps.load(it) }
        }

        // To add your Maps API key to this project:
        // 1. Create a file app/secure.properties
        // 2. Add this line, where YOUR_API_KEY is your API key:
        //        GOOGLE_MAPS_API_KEY=YOUR_API_KEY
        resValue("string", "google_maps_api_key", (secureProps.getProperty("GOOGLE_MAPS_API_KEY") ?: ""))

        val endpoint = "\"http://alberto97.altervista.org/arpav/v1/impianti.json\""
        buildConfigField("String", "ENDPOINT_URL", (secureProps.getProperty("ENDPOINT_URL") ?: endpoint))

        room {
            schemaLocationDir.set(file("$projectDir/schemas"))
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions.jvmTarget = "17"

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.preference:preference-ktx:1.2.1")

    // Hilt
    val hiltVersion = "2.48"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")
    ksp("androidx.hilt:hilt-compiler:1.1.0-alpha01")

    // Lifecycle
    val lifecycleVersion = "2.6.1"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // Navigation
    val navVersion = "2.6.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // Room
    val roomVersion = "2.5.2"
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:android-maps-utils:3.5.3")
    implementation("com.google.maps.android:maps-ktx:3.4.0")

    // Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Work
    val workVersion = "2.8.1"
    implementation("androidx.work:work-runtime-ktx:$workVersion")
    implementation("androidx.hilt:hilt-work:1.0.0")
}