package dev.panuszewski.gradlenavigation

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import org.jetbrains.kotlin.psi.stubs.elements.KtValueArgumentElementType

class GoToScriptPluginHandler : GotoDeclarationHandler {

    override fun getGotoDeclarationTargets(element: PsiElement?, offset: Int, editor: Editor): Array<PsiElement> {
        val project = editor.project
        if (project != null && element != null && isWithinGradleBuildscript(element)) {
            val pluginBlock = PsiTreeUtil.findFirstParent(element) {
                try {
                    it.firstChild.text == "plugins"
                        && it.lastChild.elementType is KtValueArgumentElementType<*>
                } catch (e: Exception) {
                    false
                }
            }
            if (pluginBlock != null) {
                return FilenameIndex.getVirtualFilesByName("${element.text}.gradle.kts", GlobalSearchScope.allScope(project))
                    .mapNotNull { PsiManager.getInstance(editor.project!!).findFile(it) }
                    .toTypedArray()

            }
        }
        return emptyArray()
    }

    private fun isWithinGradleBuildscript(sourceElement: PsiElement): Boolean =
        sourceElement.containingFile.name.endsWith("gradle.kts")
}