plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.koincleanarchitecture"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.koincleanarchitecture"
        minSdk = 26
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    flavorDimensions("appType")
    productFlavors{
        create("dev"){
            dimension = "appType"
            applicationIdSuffix=".dev"
            buildConfigField("String","BASE_URL","\"https://rickandmortyapi.com\"")
        }
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    /*Koin Dependency Injection*/
    implementation(libs.bundles.koin)
    /*Retrofit */
    implementation(libs.bundles.rerofit)
    implementation(libs.logging.interceptor)
    /*Lifecycle*/
    /* Lifecycle */
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.common)
    implementation(libs.androidx.lifecycle.lifecycle.process)
    implementation(libs.androidx.lifecycle.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.extensions)
    /* Kotlinx Coroutines */
    implementation(libs.kotlinx.coroutines.android)
    /*Timber */
    implementation(libs.timber)
    /*Activity Ktx*/
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)
    /*Coil For Image Loading*/

    implementation(libs.coil3.coil)
    implementation(libs.coil.network.okhttp)
    /*Glide for Image Loading*/
    implementation(libs.glide)
    /*Recycler view*/
    implementation(libs.androidx.recyclerview)
    /*Ktor Client for Http Api Calls*/
    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)




    implementation(libs.androidx.lifecycle.lifecycle.common)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}