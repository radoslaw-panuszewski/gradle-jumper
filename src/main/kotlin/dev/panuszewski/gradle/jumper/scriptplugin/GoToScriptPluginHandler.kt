package dev.panuszewski.gradle.jumper.scriptplugin

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope.allScope
import com.intellij.psi.util.elementType
import dev.panuszewski.gradle.jumper.GradleGoToDeclarationHandler
import dev.panuszewski.gradle.jumper.util.and
import dev.panuszewski.gradle.jumper.util.findFirstParent
import org.jetbrains.kotlin.psi.stubs.elements.KtValueArgumentElementType

public class GoToScriptPluginHandler : GradleGoToDeclarationHandler() {

    override fun getGradleGotoDeclarationTargets(psiElement: PsiElement, project: Project): Array<PsiElement> {
        if (isScriptPluginUsage(psiElement)) {
            val pluginName = psiElement.text.replace("`", "")

            return FilenameIndex.getVirtualFilesByName("$pluginName.gradle.kts", allScope(project))
                .mapNotNull { PsiManager.getInstance(project).findFile(it) }
                .toTypedArray()
        }
        return emptyArray()
    }

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