package dev.panuszewski.gradle.jumper

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope.allScope
import com.intellij.psi.util.elementType
import dev.panuszewski.gradle.jumper.util.and
import dev.panuszewski.gradle.jumper.util.findFirstParent
import dev.panuszewski.gradle.jumper.util.firstChild
import dev.panuszewski.gradle.jumper.util.or
import net.pearx.kasechange.toCamelCase
import org.jetbrains.kotlin.psi.stubs.elements.KtDotQualifiedExpressionElementType
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementTypes

public class GoToSubprojectHandler : GradleGoToDeclarationHandler() {

    override fun getGradleGotoDeclarationTargets(psiElement: PsiElement, project: Project): Array<PsiElement> {
        if (isPartOfTypesafeProjectAccessorExpression(psiElement)) {
            val buildscriptFiles = findAllBuildscriptFiles(project)

            return buildscriptFiles
                .filter { it.enclosingDirName?.toCamelCase() == psiElement.text }
                .toTypedArray()
        }
        return emptyArray()
    }

    private fun isPartOfTypesafeProjectAccessorExpression(element: PsiElement): Boolean {
        val typesafeProjectAccessorExpression = element.findFirstParent { parent ->
            and(
                or(
                    parent.elementType is KtDotQualifiedExpressionElementType,
                    parent.elementType == GroovyElementTypes.REFERENCE_EXPRESSION
                ),
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

private val PsiFile.enclosingDirName: String?
    get() = parent?.name