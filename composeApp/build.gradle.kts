import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val ktor_version = "2.3.4"
val coroutines_version = "1.7.3"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version "1.9.10"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}


kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("io.ktor:ktor-server-core:$ktor_version")
            implementation("io.ktor:ktor-server-netty:$ktor_version")
            implementation("io.ktor:ktor-server-call-logging:$ktor_version")
            implementation("ch.qos.logback:logback-classic:1.4.11")
            implementation("io.ktor:ktor-server-content-negotiation:${ktor_version}")
            implementation("io.ktor:ktor-serialization-kotlinx-json:${ktor_version}")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            implementation("io.ktor:ktor-client-core:$ktor_version")
            implementation("io.ktor:ktor-client-cio:$ktor_version") // for JVM/desktop
            implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")


//            testImplementation("io.ktor:ktor-server-tests:$ktor_version")
//            testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.0")

        }
        commonTest.dependencies {
            implementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
            runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
            implementation("io.mockk:mockk:1.13.8")
            implementation(libs.kotlin.test)
            implementation("io.ktor:ktor-server-test-host:2.3.3")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutines_version")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutines_version")
            implementation("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.9.4")
            implementation("androidx.collection:collection:1.4.0")
        }
    }
}


compose.desktop {
    application {
        mainClass = "org.kevinparks.shipmenttracker.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.kevinparks.shipmenttracker.MainKt"
            packageVersion = "1.0.0"
        }
    }
}
