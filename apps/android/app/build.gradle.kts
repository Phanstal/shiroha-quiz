plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.fufu.quiz"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fufu.quiz"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.1.0"
        testInstrumentationRunner = "com.fufu.quiz.state.QuizRepositoryCloudRestoreSafetyInstrumentation"
        buildConfigField("boolean", "MATH_RENDERING_ENABLED", "true")
        buildConfigField("boolean", "LATEX_COMPARISON_ENABLED", "false")
    }

    // Single APK: one Native/Compose build, no web shell and no test variants.
    flavorDimensions += "variant"
    productFlavors {
        create("native") {
            dimension = "variant"
        }
    }

    buildTypes {
        release {
            // Sideload-friendly single APK; CI does not need a private keystore.
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            isShrinkResources = true
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
        freeCompilerArgs += "-XXLanguage:+BreakContinueInInlineLambdas"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    sourceSets.getByName("native").apply {
        java.srcDir("src/katex/java")
        assets.srcDir("src/katex/assets")
    }
    sourceSets.getByName("testNative").apply {
        java.srcDir("src/androidTestNative/java")
        java.srcDir("src/testKatex/java")
    }
    testOptions.unitTests.isIncludeAndroidResources = true

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

val exportFufuApk by tasks.registering(Copy::class) {
    val versionName = "1.1.0"
    from(layout.buildDirectory.file("outputs/apk/native/release/app-native-release.apk"))
    into(layout.buildDirectory.dir("outputs/fufuquiz"))
    rename { "FUFUQuiz-v$versionName.apk" }
}

afterEvaluate {
    tasks.named("assembleNativeRelease") {
        finalizedBy(exportFufuApk)
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    add("nativeImplementation", "androidx.work:work-runtime-ktx:2.9.1")
    testImplementation("junit:junit:4.13.2")
    add("testNativeImplementation", "net.sf.kxml:kxml2:2.3.0")
    add("testNativeImplementation", "org.robolectric:robolectric:4.12.2")
    add("testNativeImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
