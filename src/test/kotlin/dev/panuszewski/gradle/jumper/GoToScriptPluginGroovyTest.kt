package dev.panuszewski.gradle.jumper

class GoToScriptPluginGroovyTest : BaseGoToDeclarationTest() {

    override fun getTestDataPath() = "./example-project-groovy"

    fun testGoToCamelCasePlugin() {
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("\"camelCasePlugin\"")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("buildSrc/src/main/groovy/camelCasePlugin.gradle")
    }

    fun testGoToKebabCasePlugin() {
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("\"kebab-case-plugin\"")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("buildSrc/src/main/groovy/kebab-case-plugin.gradle")
    }

    fun testGoToSnakeCasePlugin() {
        // given
        openFileInEditor("build.gradle")
        putCaretOnElement("\"snake_case_plugin\"")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("buildSrc/src/main/groovy/snake_case_plugin.gradle")
    }

    fun testGoToNotTypesafePlugin() {
        // TODO
    }
}