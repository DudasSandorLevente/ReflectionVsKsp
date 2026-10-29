plugins {
    alias(libs.plugins.android.test)
}

android {
    namespace = "com.sld.reflectionvsksp.benchmark"
    targetProjectPath = ":app"
    compileSdk {
        version = release(37)
    }
    defaultConfig {
        minSdk = 24
        targetSdk = 37
        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildTypes {
        create("benchmark") {
            initWith(getByName("debug"))
            matchingFallbacks += listOf("debug")
        }
    }
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.benchmark.junit4)
    implementation("androidx.lifecycle:lifecycle-common:2.11.0")
}
