package dev.panuszewski.gradlenavigation

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.compiled.ClsMethodImpl
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import org.jetbrains.kotlin.idea.references.SyntheticPropertyAccessorReference
import org.jetbrains.kotlin.psi.stubs.elements.KtDotQualifiedExpressionElementType

class GoToSubprojectHandler : GotoDeclarationHandler {

    override fun getGotoDeclarationTargets(element: PsiElement?, offset: Int, editor: Editor): Array<PsiElement> {
        val project = editor.project

        if (project != null && element != null && isWithinGradleBuildscript(element)) {
            val subprojectReference = PsiTreeUtil.findFirstParent(element) {
                try {
                    it.elementType is KtDotQualifiedExpressionElementType
                        && PsiTreeUtil.firstChild(it).text == "projects"
                } catch (e: Exception) {
                    false
                }
            }
            if (subprojectReference != null) {

                val allSubprojectBuildscripts =  FilenameIndex.getVirtualFilesByName("build.gradle.kts", GlobalSearchScope.allScope(project))
                    .mapNotNull { PsiManager.getInstance(editor.project!!).findFile(it) }

                val subprojectBuildscript = getSubprojectNameFromSyntheticMethod(element)
                    ?.let { subprojectName -> allSubprojectBuildscripts.firstOrNull { it.parent?.name == subprojectName } }
                    ?: allSubprojectBuildscripts.firstOrNull {
                        it.parent?.name?.filter { it.isLetterOrDigit() }?.lowercase() == element.text
                    }

                return subprojectBuildscript?.let { arrayOf(it) } ?: emptyArray()
            }
        }
        return emptyArray()
    }

    private fun getSubprojectNameFromSyntheticMethod(element: PsiElement) =
        element.parent.references
            .filterIsInstance<SyntheticPropertyAccessorReference>()
            .firstOrNull()
            ?.resolve()
            ?.let { it as? ClsMethodImpl }
            ?.sourceMirrorMethod
            ?.body
            ?.let { body -> PsiTreeUtil.findChildrenOfType(body, PsiLiteralExpression::class.java) }
            ?.firstOrNull()
            ?.text
            ?.replace("\"", "")
            ?.replace(":", "")

    private fun isWithinGradleBuildscript(sourceElement: PsiElement): Boolean =
        sourceElement.containingFile.name.endsWith("gradle.kts")
}