plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.koincleanarchitecture"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.koincleanarchitecture"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    flavorDimensions("appType")
    productFlavors{
        create("dev"){
            dimension = "appType"
            applicationIdSuffix=".dev"
            buildConfigField("String","BASE_URL","https://rickandmortyapi.com/")
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


    implementation(libs.androidx.lifecycle.lifecycle.common)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}