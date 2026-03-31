plugins {
    id("com.android.application")
}

val appVersionCode = 5
val appVersionName = "1.3.0"

android {
    compileSdk = 36
    buildToolsVersion = "30.0.3"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        applicationId = "de.devisnik.android.sliding"
        minSdk = 21
        targetSdk = 36
        versionCode = appVersionCode
        versionName = appVersionName
    }
    signingConfigs {
        if (hasProperty("keystoreFile")) {
            create("release") {
                storeFile = file(property("keystoreFile") as String)
                keyAlias = property("keystoreKeyAlias") as String
                storePassword = property("keystoreStorePassword") as String
                keyPassword = property("keystoreKeyPassword") as String
            }
        }
    }
    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            if (hasProperty("keystoreFile")) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    namespace = "de.devisnik.android.sliding"
    lint {
        abortOnError = false
        lintConfig = file("lint_config.xml")
    }
}

dependencies {
    implementation(project(":model"))
}
