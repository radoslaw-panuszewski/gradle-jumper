package dev.panuszewski.gradle.jumper.subproject

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope.allScope
import com.intellij.psi.util.elementType
import dev.panuszewski.gradle.jumper.GradleGoToDeclarationHandler
import dev.panuszewski.gradle.jumper.util.and
import dev.panuszewski.gradle.jumper.util.findFirstParent
import dev.panuszewski.gradle.jumper.util.firstChild
import org.jetbrains.kotlin.psi.stubs.elements.KtDotQualifiedExpressionElementType

public class GoToSubprojectHandler : GradleGoToDeclarationHandler() {

    override fun getGradleGotoDeclarationTargets(psiElement: PsiElement, project: Project): Array<PsiElement> {
        if (isPartOfTypesafeProjectAccessorExpression(psiElement)) {

            val locator = SubprojectBuildscriptLocator(
                buildscriptFiles = findAllBuildscriptFiles(project),
                typesafeSubprojectAccessor = psiElement
            )
            val subprojectBuildscriptFile = locator.locateSubprojectBuildscriptFile()

            if (subprojectBuildscriptFile != null) {
                return arrayOf(subprojectBuildscriptFile)
            }
        }
        return emptyArray()
    }

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