@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    version = "2023.3"
    type = "IU"
    plugins = listOf("org.jetbrains.kotlin")
    updateSinceUntilBuild = false
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
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

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    withType<Wrapper> {
        gradleVersion = "8.2"
    }
}