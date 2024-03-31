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
import dev.panuszewski.gradle.jumper.util.firstChild
import dev.panuszewski.gradle.jumper.util.or
import net.pearx.kasechange.toCamelCase
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.stubs.elements.KtDotQualifiedExpressionElementType
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementTypes

public class GoToSubprojectHandler : GradleGoToDeclarationHandler() {

    override fun getGradleGotoDeclarationTargets(psiElement: PsiElement, project: Project): Array<PsiElement> {
        if (isSubprojectReference(psiElement)) {
            val buildscriptFiles = findAllBuildscriptFilesIn(project)
            val subprojectName = getSubprojectNameFrom(psiElement)

            return buildscriptFiles
                .filter { it.enclosingDirName?.toCamelCase() == subprojectName }
                .toTypedArray()
        }
        return emptyArray()
    }

    private fun isSubprojectReference(psiElement: PsiElement) =
        or(
            isPartOfTypesafeProjectAccessorExpression(psiElement),
            isPartOfNonTypesafeProjectReference(psiElement)
        )

    private fun isPartOfTypesafeProjectAccessorExpression(element: PsiElement): Boolean {
        val typesafeProjectAccessorExpression = element.findFirstParent { parent ->
            and(
                parent.firstChild().text == "projects",
                or(
                    parent.elementType is KtDotQualifiedExpressionElementType,
                    parent.elementType == GroovyElementTypes.REFERENCE_EXPRESSION
                )
            )
        }
        return typesafeProjectAccessorExpression != null
    }

    private fun isPartOfNonTypesafeProjectReference(element: PsiElement): Boolean {
        val methodCallExpression = element.findFirstParent { parent ->
            and(
                parent.firstChild.text == "project",
                or(
                    parent.elementType == KtNodeTypes.CALL_EXPRESSION,
                    parent.elementType == GroovyElementTypes.METHOD_CALL_EXPRESSION
                )
            )
        }
        return methodCallExpression != null
    }

    private fun findAllBuildscriptFilesIn(project: Project): List<PsiFile> =
        FilenameIndex.getAllFilenames(project)
            .filter { filename -> filename.endsWith(".gradle.kts") || filename.endsWith(".gradle") }
            .flatMap { filename -> FilenameIndex.getVirtualFilesByName(filename, projectScope(project)) }
            .mapNotNull { PsiManager.getInstance(project).findFile(it) }

    private fun getSubprojectNameFrom(psiElement: PsiElement): String =
        psiElement.text
            .replace("`", "")
            .replace("\"", "")
            .replace("'", "")
            .removePrefix(":")
}

private val PsiFile.enclosingDirName: String?
    get() = parent?.name