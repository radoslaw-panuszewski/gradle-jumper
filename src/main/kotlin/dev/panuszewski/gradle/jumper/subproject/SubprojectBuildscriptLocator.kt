package dev.panuszewski.gradle.jumper.subproject

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.impl.compiled.ClsMethodImpl
import dev.panuszewski.gradle.jumper.util.findChildrenOfType
import dev.panuszewski.gradle.jumper.util.takeIfInstance
import net.pearx.kasechange.toCamelCase
import org.jetbrains.kotlin.idea.references.SyntheticPropertyAccessorReference

internal class SubprojectBuildscriptLocator(
    private val buildscriptFiles: List<PsiFile>,
    private val typesafeSubprojectAccessor: PsiElement
) {
    fun locateSubprojectBuildscriptFile(): PsiFile? =
        getSubprojectNameFromReferencedMethod()
            ?.let { subprojectName -> findByEnclosingDirName(subprojectName) }
            ?: findByGuessingTypesafeAccessor()

    private fun getSubprojectNameFromReferencedMethod(): String? {
        try {
            val reference = typesafeSubprojectAccessor.parent.references
                .filterIsInstance<SyntheticPropertyAccessorReference>()
                .firstOrNull()

            val referencedMethod = reference?.resolve()
                ?.takeIfInstance<ClsMethodImpl>()
                ?.sourceMirrorMethod

            val stringLiteral = referencedMethod?.body
                ?.findChildrenOfType<PsiLiteralExpression>()
                ?.firstOrNull()

            return stringLiteral?.text?.replace("\"", "")?.replace(":", "")

        } catch (e: Throwable) {
            return null
        }
    }

    private fun findByEnclosingDirName(name: String): PsiFile? =
        buildscriptFiles
            .firstOrNull { it.enclosingDirName == name }

    private fun findByGuessingTypesafeAccessor() =
        buildscriptFiles
            .firstOrNull { it.enclosingDirName?.toCamelCase() == typesafeSubprojectAccessor.text }
}

private val PsiFile.enclosingDirName: String?
    get() = parent?.name