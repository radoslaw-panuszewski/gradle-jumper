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
    version = "2023.3"
    type = "IU"
    plugins = listOf("org.jetbrains.kotlin", "com.intellij.gradle")
    updateSinceUntilBuild = false
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation("net.pearx.kasechange:kasechange-jvm:1.4.1")
    
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)
    testImplementation(libs.assertk)
    // TODO spróbować nie wczytywać do classpatha klasek, które się duplikują
    //      albo dodać jara do customowej konfiguracji i zrobić tak żeby intellijowa konfiguracja ją extendowała
    //      (żeby zmienić kolejność czytania jarów)
//    testImplementation(files("lib/output.jar"))
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
}