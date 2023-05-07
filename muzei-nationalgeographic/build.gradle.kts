@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.io.FileInputStream
import java.util.Properties

plugins {
   id("com.android.application")
   id("org.jetbrains.kotlin.android")
}

dependencies {
   // kotlin
   implementation("androidx.core:core-ktx:1.10.0")
   // muzei
   implementation("com.google.android.apps.muzei:muzei-api:3.4.1")
   // retrofit
   implementation("com.squareup.retrofit2:retrofit:2.9.0")
   implementation("com.squareup.retrofit2:converter-gson:2.9.0")
   implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
   // misc
   implementation("androidx.work:work-runtime-ktx:2.8.1")
   implementation("androidx.preference:preference-ktx:1.2.0")
   implementation("com.github.sisyphsu:dateparser:1.0.11")
   // de-sugaring
   coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
   // test
   testImplementation("junit:junit:4.13.2")
}

android {
   namespace = "de.msal.muzei.nationalgeographic"
   compileSdk = 33
   buildToolsVersion = "33.0.1"

   defaultConfig {
      applicationId = "de.msal.muzei.nationalgeographic"
      minSdk = 21
      targetSdk = 33
      // SemVer
      versionName = "2.4.4"
      versionCode = 20404
      archivesName.set("$applicationId-v$versionCode")
      //
      // vectorDrawables.useSupportLibrary = true
   }

   buildFeatures {
      buildConfig = true
   }

   fun getSecret(key: String): String? {
      val secretsFile: File = rootProject.file("keystore/signing.properties")
      return if (secretsFile.exists()) {
         val props = Properties()
         props.load(FileInputStream(secretsFile))
         props.getProperty(key)
      } else {
         null
      }
   }

   signingConfigs {
      create("release") {
         if (getSecret("STORE_FILE") != null) {
            storeFile = File(getSecret("STORE_FILE")!!)
            storePassword = getSecret("STORE_PASSWORD")
            keyAlias = getSecret("KEY_ALIAS")
            keyPassword = getSecret("KEY_PASSWORD")
         }
      }
   }

   buildTypes {
      release {
         signingConfig = signingConfigs.getByName("release")
         isDebuggable = false
         isMinifyEnabled = true
         isShrinkResources = true
         proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
         )
      }
      debug {
         applicationIdSuffix = ".debug"
         versionNameSuffix = " [DEBUG]"
      }
   }

   compileOptions {
      sourceCompatibility(JavaVersion.VERSION_17)
      targetCompatibility(JavaVersion.VERSION_17)
   }

   kotlinOptions {
      jvmTarget = JavaVersion.VERSION_17.toString()
   }

   lint.disable.add("InvalidPackage")

   testOptions {
      unitTests.isReturnDefaultValues = true
   }

}

// versionCode <-> versionName /////////////////////////////////////////////////////////////////////

/**
 * Checks if versionCode and versionName match.
 * Needed because of F-Droid: both have to be hard-coded and can't be assigned dynamically.
 * So at least check during build for them to match.
 */
tasks.register("checkVersion") {
   doLast {
      val versionCode: Int? = android.defaultConfig.versionCode
      val correctVersionCode: Int = generateVersionCode(android.defaultConfig.versionName!!)
      if (versionCode != correctVersionCode) throw GradleException(
         "versionCode and versionName don't match: versionCode should be $correctVersionCode. Is $versionCode."
      )
   }
}
tasks.findByName("assemble")!!.dependsOn(tasks.findByName("checkVersion")!!)

/**
 * Checks if a fastlane changelog for the current version is present.
 */
tasks.register("checkFastlaneChangelog") {
   doLast {
      val versionCode: Int? = android.defaultConfig.versionCode
      val changelogFile: File =
         file("$rootDir/fastlane/metadata/android/en-US/changelogs/${versionCode}.txt")
      if (!changelogFile.exists())
         throw GradleException(
            "Fastlane changelog missing: expecting file '$changelogFile'"
         )
   }
}
tasks.findByName("build")!!.dependsOn(tasks.findByName("checkFastlaneChangelog")!!)

/**
 * Generates a versionCode based on the given semVer String.
 *
 * @param semVer e.g. 1.3.1
 * @return e.g. 10301 (-> 1 03 01)
 */
fun generateVersionCode(semVer: String): Int {
   return semVer.split('.')
      .map { Integer.parseInt(it) }
      .reduce { sum, value -> sum * 100 + value }
}