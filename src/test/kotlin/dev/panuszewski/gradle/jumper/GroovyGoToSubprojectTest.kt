package dev.panuszewski.gradle.jumper

class GroovyGoToSubprojectTest : BaseGoToDeclarationTest() {

    override fun getTestDataPath() = "./example-project-groovy"

    fun testGoToCamelCaseSubproject() {
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("camelCaseSubproject1")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("camelCaseSubproject1/build.gradle")
    }

    fun testGoToKebabCaseSubproject() {
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("kebabCaseSubproject1")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("kebab-case-subproject-1/build.gradle")
    }

    fun testGoToSnakeCaseSubproject() {
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("snakeCaseSubproject1")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("snake_case_subproject_1/build.gradle")
    }

    fun testGoToNotTypesafeSubproject() {
        // TODO
    }

    fun testGoToCustomBuildscriptNameSubproject() {
        // TODO
    }

    fun testGoToNestedSubproject() {
        // TODO
    }
}