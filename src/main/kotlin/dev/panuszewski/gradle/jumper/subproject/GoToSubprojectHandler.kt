package dev.panuszewski.gradle.jumper.subproject

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope.allScope
import com.intellij.psi.util.elementType
import dev.panuszewski.gradle.jumper.and
import dev.panuszewski.gradle.jumper.findFirstParent
import dev.panuszewski.gradle.jumper.firstChild
import dev.panuszewski.gradle.jumper.or
import org.jetbrains.kotlin.psi.stubs.elements.KtDotQualifiedExpressionElementType

public class GoToSubprojectHandler : GotoDeclarationHandler {

    override fun getGotoDeclarationTargets(element: PsiElement?, offset: Int, editor: Editor): Array<PsiElement> {
        element ?: return emptyArray()
        val project = editor.project ?: return emptyArray()

        if (isWithinGradleBuildscript(element) && isPartOfTypesafeProjectAccessorExpression(element)) {

            val locator = SubprojectBuildscriptLocator(
                buildscriptFiles = findAllBuildscriptFiles(project),
                typesafeSubprojectAccessor = element
            )
            val subprojectBuildscriptFile = locator.locateSubprojectBuildscriptFile()

            if (subprojectBuildscriptFile != null) {
                return arrayOf(subprojectBuildscriptFile)
            }
        }
        return emptyArray()
    }

    private fun isWithinGradleBuildscript(element: PsiElement) =
        or(
            element.containingFile.name.endsWith("gradle.kts"),
            element.containingFile.name.endsWith("gradle")
        )

    private fun isPartOfTypesafeProjectAccessorExpression(element: PsiElement): Boolean {
        val typesafeProjectAccessorExpression = element.findFirstParent { parent ->
            and(
                parent.elementType is KtDotQualifiedExpressionElementType,
                parent.firstChild().text == "projects"
            )
        }
        return typesafeProjectAccessorExpression != null
    }

    private fun findAllBuildscriptFiles(project: Project): List<PsiFile> {
        val kotlinBuildscripts = FilenameIndex.getVirtualFilesByName("build.gradle.kts", allScope(project))
        val groovyBuildscripts = FilenameIndex.getVirtualFilesByName("build.gradle", allScope(project))

        return (kotlinBuildscripts + groovyBuildscripts)
            .mapNotNull { PsiManager.getInstance(project).findFile(it) }
    }
}