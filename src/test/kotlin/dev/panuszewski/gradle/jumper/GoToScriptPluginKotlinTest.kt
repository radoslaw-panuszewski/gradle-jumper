package dev.panuszewski.gradle.jumper

class GoToScriptPluginKotlinTest : BaseGoToDeclarationTest() {

    override fun getTestDataPath() = "./example-project-kotlin"

    fun testGoToCamelCasePlugin() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("camelCasePlugin")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("buildSrc/src/main/kotlin/camelCasePlugin.gradle.kts")
    }

    fun testGoToKebabCasePlugin() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("`kebab-case-plugin`")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("buildSrc/src/main/kotlin/kebab-case-plugin.gradle.kts")
    }

    fun testGoToSnakeCasePlugin() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("snake_case_plugin")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("buildSrc/src/main/kotlin/snake_case_plugin.gradle.kts")
    }

    fun testGoToNotTypesafePlugin() {
        // given
        openFileInEditor("build.gradle.kts")
        putCaretOnElement("notTypesafePlugin")

        // when
        goToDeclaration()

        // then
        verifyFileIsOpen("buildSrc/src/main/kotlin/notTypesafePlugin.gradle.kts")
    }
}