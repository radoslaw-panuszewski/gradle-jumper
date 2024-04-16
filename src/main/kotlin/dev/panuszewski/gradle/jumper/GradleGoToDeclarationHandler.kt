package dev.panuszewski.gradle.jumper

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import dev.panuszewski.gradle.jumper.util.or
import org.jetbrains.kotlin.lexer.KtTokens.CLOSING_QUOTE
import org.jetbrains.kotlin.lexer.KtTokens.OPEN_QUOTE

public abstract class GradleGoToDeclarationHandler : GotoDeclarationHandler {

    final override fun getGotoDeclarationTargets(
        psiElement: PsiElement?,
        offset: Int,
        editor: Editor
    ): Array<PsiElement> {

        psiElement ?: return emptyArray()
        val project = editor.project ?: return emptyArray()

        if (isWithinGradleBuildscript(psiElement)) {
            return when (psiElement.elementType) {
                OPEN_QUOTE -> getGradleGotoDeclarationTargets(psiElement.nextSibling, project)
                CLOSING_QUOTE -> getGradleGotoDeclarationTargets(psiElement.prevSibling, project)
                else -> getGradleGotoDeclarationTargets(psiElement, project)
            }
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