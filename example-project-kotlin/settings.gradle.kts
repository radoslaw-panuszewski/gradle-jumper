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
include("notTypesafeSubproject1")
include("customBuildscriptNameSubproject1")

project(":customBuildscriptNameSubproject1").buildFileName = "custom-buildscript.gradle.kts"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")