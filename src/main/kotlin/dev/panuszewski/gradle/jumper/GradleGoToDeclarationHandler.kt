package dev.panuszewski.gradle.jumper

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import dev.panuszewski.gradle.jumper.util.or

public abstract class GradleGoToDeclarationHandler : GotoDeclarationHandler {

    final override fun getGotoDeclarationTargets(
        psiElement: PsiElement?,
        offset: Int,
        editor: Editor
    ): Array<PsiElement> {

        psiElement ?: return emptyArray()
        val project = editor.project ?: return emptyArray()

        if (isWithinGradleBuildscript(psiElement)) {
            return getGradleGotoDeclarationTargets(psiElement, project)
        }
        return emptyArray()
    }

    private fun isWithinGradleBuildscript(element: PsiElement) =
        or(
            element.containingFile.name.endsWith("gradle.kts"),
            element.containingFile.name.endsWith("gradle")
        )

    protected abstract fun getGradleGotoDeclarationTargets(psiElement: PsiElement, project: Project): Array<PsiElement>
}