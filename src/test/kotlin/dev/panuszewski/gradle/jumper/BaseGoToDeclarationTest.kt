package dev.panuszewski.gradle.jumper

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction
import com.intellij.openapi.file.exclude.OverrideFileTypeManager
import com.intellij.openapi.vfs.newvfs.impl.CachedFileType
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.plugins.groovy.GroovyFileType.GROOVY_FILE_TYPE

abstract class BaseGoToDeclarationTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject(".", ".")
    }

    protected fun openFileInEditor(path: String) {
        val file = myFixture.findFileInTempDir(path)
        if (path.endsWith(".gradle")) {
            OverrideFileTypeManager.getInstance().addFile(file, GROOVY_FILE_TYPE)
            CachedFileType.clearCache()
        }
        myFixture.openFileInEditor(file)
    }

    protected fun putCaretOnElement(element: String) {
        val psiElement = PsiTreeUtil.findChildrenOfType(myFixture.file, PsiElement::class.java)
            .find { it.text == element }
            ?: error("PsiElement not found for '$element' in file ${myFixture.file.name}")

        // TODO remove the +1 and handle the OPEN_QUOTE case properly (and also other token-level PSI elements)
        myFixture.editor.caretModel.moveToOffset(psiElement.textOffset + 1)
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