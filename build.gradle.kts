@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.intellij)
}

group = "dev.panuszewski"
version = versionFromNearestTag()

repositories {
    mavenCentral()
    maven { url = uri("https://www.jetbrains.com/intellij-repository/snapshots/") }
}

intellij {
    version = "2023.1"
    type = "IU"
    plugins = listOf("org.jetbrains.kotlin", "org.intellij.groovy")
    updateSinceUntilBuild = false
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    explicitApi()
}

dependencies {
    implementation(libs.kasechange)
    testImplementation(libs.assertk)
}

tasks {
    runIde {
        ideDir = File("${System.getenv("HOME")}/Applications/IntelliJ IDEA Ultimate.app/Contents")
    }

    signPlugin {
        certificateChain = System.getenv("CERTIFICATE_CHAIN")
        privateKey = System.getenv("PRIVATE_KEY")
        password = System.getenv("PRIVATE_KEY_PASSWORD")
    }

    publishPlugin {
        token = System.getenv("PUBLISH_TOKEN")
    }

    buildSearchableOptions {
        enabled = false
    }

    check {
        dependsOn(verifyPlugin)
    }
}