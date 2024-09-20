plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.githubissuetracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.githubissuetracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt for Dependency Injection
    implementation (libs.hilt.android)
    ksp(libs.hilt.compiler)

    // HttpLoggingInterceptor
    implementation(libs.logging.interceptor)

    // Retrofit for network requests
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // Paging 3
    implementation (libs.androidx.paging.runtime)

    // Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    // Timber
    implementation (libs.timber)

    // ssp
    implementation (libs.sdp.android)

    // sdp
    implementation (libs.ssp.android)

    // Markdown
    implementation (libs.core)

    // Glide
    ksp (libs.ksp)
    implementation (libs.glide)

}