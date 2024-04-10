package dev.panuszewski.gradle.jumper

class GoToSubprojectGroovyTest : BaseGoToDeclarationTest() {

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
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("\":notTypesafeSubproject1\"")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("notTypesafeSubproject1/build.gradle")
    }

    fun testGoToCustomBuildscriptNameSubproject() {
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("customBuildscriptNameSubproject1")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("customBuildscriptNameSubproject1/custom-buildscript.gradle")
    }

    fun testGoToNestedSubproject() {
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("nestedSubproject")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("nested/nestedSubproject/build.gradle")
    }

    fun testGoToParentOfNestedSubproject() {
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("nested")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("nested/build.gradle")
    }
}