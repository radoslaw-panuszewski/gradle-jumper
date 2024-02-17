rootProject.name = "example-project"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("camelCaseSubproject1")
include("kebab-case-subproject-1")
include("snake_case_subproject_1")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")