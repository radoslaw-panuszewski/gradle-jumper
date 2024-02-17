package dev.panuszewski.gradlenavigation.scriptplugin

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope.allScope
import com.intellij.psi.util.elementType
import dev.panuszewski.gradlenavigation.and
import dev.panuszewski.gradlenavigation.findFirstParent
import dev.panuszewski.gradlenavigation.or
import org.jetbrains.kotlin.psi.stubs.elements.KtValueArgumentElementType

class GoToScriptPluginHandler : GotoDeclarationHandler {

    override fun getGotoDeclarationTargets(element: PsiElement?, offset: Int, editor: Editor): Array<PsiElement> {
        element ?: return emptyArray()
        val project = editor.project ?: return emptyArray()

        if (isWithinGradleBuildscript(element) && isScriptPluginUsage(element)) {
            val pluginName = element.text.replace("`", "")

            return FilenameIndex.getVirtualFilesByName("$pluginName.gradle.kts", allScope(project))
                .mapNotNull { PsiManager.getInstance(project).findFile(it) }
                .toTypedArray()
        }
        return emptyArray()
    }

    private fun isWithinGradleBuildscript(element: PsiElement) =
        or(
            element.containingFile.name.endsWith("gradle.kts"),
            element.containingFile.name.endsWith("gradle")
        )

    private fun isScriptPluginUsage(element: PsiElement): Boolean {
        val pluginBlock = element.findFirstParent { parent ->
            and(
                parent.lastChild.elementType is KtValueArgumentElementType<*>,
                parent.firstChild.text == "plugins"
            )
        }
        return pluginBlock != null
    }
}