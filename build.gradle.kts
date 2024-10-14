@file:Suppress("UnstableApiUsage")

import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaUltimate
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.intellij.platform)
    alias(libs.plugins.axion.release)
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

scmVersion {
    tag {
        fallbackPrefixes = listOf("")
    }
    unshallowRepoOnCI = true
}

group = "dev.panuszewski"
version = scmVersion.version

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaUltimate("2023.1")
        bundledPlugins("org.jetbrains.kotlin", "org.intellij.groovy")
        instrumentationTools()
        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }
    implementation(libs.kasechange)
    testImplementation(libs.assertk)
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "231"
            untilBuild = provider { null }
        }
        vendor {
            name = "Radosław Panuszewski"
            email = "radoslaw.panuszewski15@gmail.com"
            url = "https://panuszewski.dev"
        }
        id = "dev.panuszewski.gradle-jumper"
        name = "Gradle Jumper"
        description = """
            <p>Adds enhanced "go to declaration" support for Gradle typesafe (and not typesafe) accessors.
            Instead of going to the generated code, you will jump directly to the place which is semantically referenced.</p>
    
            <p>Supported:</p>
            <ul>
                <li>subproject references</li>
                <li>precompiled script plugin references</li>
            </ul>
        """.trimIndent()
    }
    pluginVerification {
        ides {
            ide(IntellijIdeaUltimate, "LATEST-EAP-SNAPSHOT", useInstaller = false)
        }
    }
    signing {
        certificateChain = System.getenv("CERTIFICATE_CHAIN")
        privateKey = System.getenv("PRIVATE_KEY")
        password = System.getenv("PRIVATE_KEY_PASSWORD")
    }
    publishing {
        token = System.getenv("PUBLISH_TOKEN")
    }
    buildSearchableOptions = false
}

tasks {
    check {
        dependsOn(verifyPlugin)
    }
}