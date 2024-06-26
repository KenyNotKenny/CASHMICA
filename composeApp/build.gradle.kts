import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)

    kotlin("plugin.serialization").version("1.9.22")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export("io.github.hoc081098:kmp-viewmodel:0.7.1") // required to expose the classes to iOS.
        }
    }


    val coroutinesVersion = "1.7.3"
    val ktorVersion = "2.3.7"
    val sqlDelightVersion = "1.5.5"
    val dateTimeVersion = "0.4.1"
    val postgrestVersion = "2.4.1"
    val gotrueVersion = "2.4.1"
    val storageVersion = "2.4.1"
    val coilVersion ="3.0.0-alpha01"


    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

            implementation("io.ktor:ktor-client-android:$ktorVersion")


        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            //Voyager
            val voyagerVersion = "1.0.0"
            implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")

            //supabase
            implementation("io.github.jan-tennert.supabase:postgrest-kt:$postgrestVersion")
            implementation("io.github.jan-tennert.supabase:gotrue-kt:$gotrueVersion")
            implementation("io.github.jan-tennert.supabase:storage-kt:$storageVersion")


            //Corountine
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")


            //SLF4J
            implementation("org.slf4j:slf4j-api:2.0.9" )
            implementation("org.slf4j:slf4j-simple:2.0.9" )

            //Kamel
            implementation("media.kamel:kamel-image:0.9.3")

            //Viewmodel
            implementation("io.github.hoc081098:kmp-viewmodel-compose:0.7.1")

            //Calf
            api("com.mohamedrejeb.calf:calf-ui:0.4.0")
            implementation("com.mohamedrejeb.calf:calf-file-picker:0.4.0")


            implementation("io.github.darkokoa:datetime-wheel-picker:1.0.0-beta01")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")


        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("io.ktor:ktor-client-java:$ktorVersion")

        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:$ktorVersion")
        }
        jvmMain.dependencies {
            implementation("io.ktor:ktor-client-java:$ktorVersion")
        }

    }
}

android {
    namespace = "org.littlebutenough.cashmica"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.littlebutenough.cashmica"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}




compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.littlebutenough.cashmica"
            packageVersion = "1.0.0"
        }

        tasks.withType<Jar> {
            manifest {
                attributes["Main-Class"] = "MainKt"
            }
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE



        }
    }
}





