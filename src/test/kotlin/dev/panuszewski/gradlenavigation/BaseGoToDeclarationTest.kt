package dev.panuszewski.gradlenavigation

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.psi.KtElement

abstract class BaseGoToDeclarationTest : BasePlatformTestCase() {

    override fun getTestDataPath() = "./example-project"

    override fun setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject(".", ".")
    }

    protected fun openFileInEditor(path: String) {
        val file = myFixture.findFileInTempDir(path)
        myFixture.openFileInEditor(file)
    }

    protected fun putCaretOnElement(element: String) {
        val psiElement = PsiTreeUtil.findChildrenOfType(myFixture.file, KtElement::class.java)
            .find { it.text == element }
            ?: error("PsiElement not found for '$element' in file ${myFixture.file.name}")

        myFixture.editor.caretModel.moveToOffset(psiElement.textOffset)
    }

    protected fun goToDeclaration() = with(myFixture) {
        GotoDeclarationAction.findTargetElement(project, editor, editor.caretModel.offset)
            ?.containingFile
            ?.virtualFile
            ?.let(::openFileInEditor)
    }

    protected fun verifyFileIsOpen(path: String) {
        // BasePlatformTestCase uploads all files under the src directory (there is no way to upload to the root directory)
        // that's why we remove the prefix
        val actual = myFixture.file.virtualFile.path.removePrefix("/src/")
        assertThat(actual).isEqualTo(path)
    }
}