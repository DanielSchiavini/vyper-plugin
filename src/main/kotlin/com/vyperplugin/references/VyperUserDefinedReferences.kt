package com.vyperplugin.references

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.vyperplugin.completion.VyperCompleter
import com.vyperplugin.psi.*
import com.vyperplugin.psi.VyperTypes.IDENTIFIER

class VyperVarLiteralReference(element: VyperVarLiteral) : VyperReferenceBase<VyperVarLiteral>(element), VyperReference {

    override fun multiResolve(): Collection<PsiElement> {
        return VyperResolver.resolveVarLiteral(element)
    }

    override fun getVariants(): Array<out Any> {
        return arrayOf("Huy", "sdsd", "sdqwr")
    }
}

//class VyperSelfAccessReference(element: VyperVarLiteral, var self : VyperSelfAccessExpression) : VyperReferenceBase<VyperVarLiteral>(element) ,VyperReference {
//
//    override fun multiResolve(): Collection<PsiElement> {
//        return VyperResolver.resolveSelfAccessVarLiteral(self,element)
//    }
//
//    override fun getVariants(): Array<out Any> {
//        return VyperCompleter.completeSelfAccess(self)
//    }
//}

class VyperMemberAccessReference(element: VyperVarLiteral, var member: VyperMemberAccessExpression): VyperReferenceBase<VyperVarLiteral>(element) ,VyperReference {
    override fun multiResolve(): Collection<PsiElement> {
        return VyperResolver.resolveMemberAccess(member)
    }

    override fun getVariants(): Array<out Any> {
        return VyperCompleter.completeMemberAccess(member)
    }

}

class VyperCallReference(element: VyperCallElement) : VyperReferenceBase<VyperCallElement>(element), VyperReference {

//    override fun calculateDefaultRangeInElement(): TextRange {
//        return element.referenceNameElement.textRange
//    }

    fun resolveFunctionCall(): Collection<FunctionResolveResult> {
        val ref = element.expressionList.firstOrNull()
        return VyperResolver.resolveFunction(element)
    }

    override fun multiResolve(): Collection<PsiElement> {
        return resolveFunctionCall().map { it.psiElement }
    }
}