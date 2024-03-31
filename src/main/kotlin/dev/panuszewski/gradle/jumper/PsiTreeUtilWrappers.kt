package dev.panuszewski.gradle.jumper

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

internal fun PsiElement.findFirstParent(condition: (PsiElement) -> Boolean): PsiElement? =
    PsiTreeUtil.findFirstParent(this) { psiElement ->
        try {
            condition(psiElement)
        } catch (t: Throwable) {
            false
        }
    }

internal fun PsiElement.firstChild(): PsiElement =
    PsiTreeUtil.firstChild(this)

internal inline fun <reified T : PsiElement> PsiElement.findChildrenOfType(): Collection<T> =
    PsiTreeUtil.findChildrenOfType(this, T::class.java)