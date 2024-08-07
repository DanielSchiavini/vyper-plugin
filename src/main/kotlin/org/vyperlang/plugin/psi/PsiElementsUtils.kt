package org.vyperlang.plugin.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil

val PsiElement.ancestors: Sequence<PsiElement> get() = generateSequence(this) { it.parent }
val PsiElement.siblings: Sequence<PsiElement> get() = generateSequence(this) { it.nextSibling }
val PsiElement.prevSiblings: Sequence<PsiElement> get() = generateSequence(this.prevSibling) { it.prevSibling }

val PsiElement.file get() = this.containingFile as VyperFile

inline fun <reified T : PsiElement> PsiElement.childOfType(strict: Boolean = true): T? =
    PsiTreeUtil.findChildOfType(this, T::class.java, strict)

fun findLastChildByType(type: IElementType, node: ASTNode): ASTNode? {
    var child = node.lastChildNode
    while (child != null) {
        val astNode = child
        if (astNode.elementType == type) return child
        child = child.treePrev
    }
    return null
}