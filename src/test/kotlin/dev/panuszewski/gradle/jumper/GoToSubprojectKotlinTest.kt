package dev.panuszewski.gradle.jumper

class GoToSubprojectKotlinTest : BaseGoToDeclarationTest() {

    override fun getTestDataPath() = "./example-project-kotlin"

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

    fun testGoToNotTypesafeSubproject() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement(":notTypesafeSubproject1")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("notTypesafeSubproject1/build.gradle.kts")
    }

    fun testGoToCustomBuildscriptNameSubproject() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("customBuildscriptNameSubproject1")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("customBuildscriptNameSubproject1/custom-buildscript.gradle.kts")
    }

    fun testGoToNestedSubproject() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("nestedSubproject")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("nested/nestedSubproject/build.gradle.kts")
    }

    fun testGoToParentOfNestedSubproject() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("nested")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("nested/build.gradle.kts")
    }
}