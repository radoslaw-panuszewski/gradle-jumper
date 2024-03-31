package dev.panuszewski.gradle.jumper

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope.projectScope
import com.intellij.psi.util.elementType
import dev.panuszewski.gradle.jumper.util.and
import dev.panuszewski.gradle.jumper.util.findFirstParent
import dev.panuszewski.gradle.jumper.util.or
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementTypes

public class GoToScriptPluginHandler : GradleGoToDeclarationHandler() {

    override fun getGradleGotoDeclarationTargets(psiElement: PsiElement, project: Project): Array<PsiElement> {
        if (isScriptPluginUsage(psiElement)) {
            val pluginName = getPluginNameFrom(psiElement)
            return findScriptFilesForPlugin(project, pluginName).toTypedArray()
        }
        return emptyArray()
    }

    private fun isScriptPluginUsage(element: PsiElement): Boolean {
        val pluginBlock = element.findFirstParent { parent ->
            and(
                or(
                    parent.elementType == KtNodeTypes.CALL_EXPRESSION,
                    parent.elementType == GroovyElementTypes.METHOD_CALL_EXPRESSION
                ),
                parent.firstChild.text == "plugins"
            )
        }
        return pluginBlock != null
    }

    private fun getPluginNameFrom(psiElement: PsiElement) =
        psiElement.text
            .replace("`", "")
            .replace("\"", "")
            .replace("'", "")

    private fun findScriptFilesForPlugin(project: Project, pluginName: String): List<PsiFile> {
        val kotlinFiles = FilenameIndex.getVirtualFilesByName("$pluginName.gradle.kts", projectScope(project))
        val groovyFiles = FilenameIndex.getVirtualFilesByName("$pluginName.gradle", projectScope(project))

        return (kotlinFiles + groovyFiles)
            .mapNotNull { PsiManager.getInstance(project).findFile(it) }
    }
}