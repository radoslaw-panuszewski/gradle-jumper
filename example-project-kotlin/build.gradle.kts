plugins {
    kotlin("jvm") version "1.9.21"
    camelCasePlugin
    `kebab-case-plugin`
    snake_case_plugin
    id("notTypesafePlugin")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.camelCaseSubproject1)
    implementation(projects.kebabCaseSubproject1)
    implementation(projects.snakeCaseSubproject1)
    implementation(project(":notTypesafeSubproject1"))
    implementation(projects.customBuildscriptNameSubproject1)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(19)
}