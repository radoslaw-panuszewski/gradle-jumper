package dev.panuszewski.gradlenavigation

class GoToSubprojectTest : BaseGoToDeclarationTest() {

    fun testGoToCamelCaseSubproject() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("camelCaseSubproject1")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("camelCaseSubproject1/build.gradle.kts")
    }

    fun testGoToKebabCaseSubproject() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("kebabCaseSubproject1")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("kebab-case-subproject-1/build.gradle.kts")
    }

    fun testGoToSnakeCaseSubproject() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("snakeCaseSubproject1")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("snake_case_subproject_1/build.gradle.kts")
    }
}