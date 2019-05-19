package com.vyperplugin.references

import com.intellij.psi.PsiElement
import com.intellij.util.containers.toMutableSmartList
import com.vyperplugin.psi.*
import com.vyperplugin.psi.VyperTypes.*


object VyperResolver {
    fun resolveVarLiteral(element: VyperNamedElement): List<PsiElement> {
        val resolveResultList = lexicalDeclarations(element).filter { it.name == element.name }.toList()
        return resolveResultList
    }

    fun lexicalDeclarations(place: PsiElement, stop: (PsiElement) -> Boolean = { false }): List<VyperNamedElement> {
//        val globalType = SolInternalTypeFactory.of(place.project).globalType
        return lexicalDeclRec(place, stop).distinct()
    }

    private fun lexicalDeclRec(place: PsiElement, stop: (PsiElement) -> Boolean): List<VyperNamedElement> {
        val parents =  place.ancestors
                .drop(1) // current element might not be a VyperElement
                .toList()

        val elements =  parents
                .takeWhile { it is VyperElement && !stop(it) }.filter{it is VyperFunctionDefinition}
                .flatMap { lexicalDeclarations(it, place) }

        return elements


    }

    private fun lexicalDeclarations(scope: PsiElement, place: PsiElement): List<VyperNamedElement> {
        return when (scope) {
            /*is SolVariableDeclaration -> {
                scope.declarationList?.declarationItemList?.filterIsInstance<SolNamedElement>()?.asSequence()
                        ?: scope.typedDeclarationList?.typedDeclarationItemList?.filterIsInstance<SolNamedElement>()?.asSequence()
                        ?: sequenceOf(scope)
            }
            is SolVariableDefinition -> lexicalDeclarations(scope.firstChild, place)

            is SolStateVariableDeclaration -> sequenceOf(scope)
            is SolContractDefinition -> {
                val childrenScope = sequenceOf(
                        scope.stateVariableDeclarationList,
                        scope.enumDefinitionList,
                        scope.structDefinitionList).flatten()
                        .map { lexicalDeclarations(it, place) }
                        .flatten()
                val extendsScope = scope.supers.asSequence()
                        .map { resolveTypeName(it).firstOrNull() }
                        .filterNotNull()
                        .map { lexicalDeclarations(it, place) }
                        .flatten()
                childrenScope + extendsScope
            }
            is SolFunctionDefinition -> {
                scope.parameters.asSequence() +
                        (scope.returns?.parameterDefList?.asSequence() ?: emptySequence())
            }
            is SolConstructorDefinition -> {
                scope.parameterList?.parameterDefList?.asSequence() ?: emptySequence()
            }
            is SolEnumDefinition -> sequenceOf(scope)

            is SolStatement -> {
                scope.children.asSequence()
                        .map { lexicalDeclarations(it, place) }
                        .flatten()
            }

            is SolBlock -> {
                scope.statementList.asSequence()
                        .map { lexicalDeclarations(it, place) }
                        .flatten()
            }

            is SolTupleStatement -> {
                scope.variableDeclaration?.let {
                    val declarationList = it.declarationList
                    val typedDeclarationList = it.typedDeclarationList
                    when {
                        declarationList != null -> declarationList.declarationItemList.asSequence()
                        typedDeclarationList != null -> typedDeclarationList.typedDeclarationItemList.asSequence()
                        else -> emptySequence()
                    }
                } ?: emptySequence()
            }

            else -> emptySequence()*/
            is VyperFunctionDefinition -> { scope.parameters?.paramDefList ?: emptyList()

            }
//            is VyperStateVariableDeclaration -> listOf(scope)
                else -> emptyList()
            }
        }


    fun resolveFunction(element: VyperCallExpression, skipThis: Boolean = false): Collection<FunctionResolveResult> {
        return resolveFunRec(element,skipThis).filter { it.name == element.referenceName }.map{FunctionResolveResult(it)}
    }

    private fun resolveFunRec(element: VyperCallExpression, skipThis: Boolean = false): List<VyperFunctionDefinition> {
        val ref = element.expressionList.firstOrNull()
        val res = mutableListOf<VyperFunctionDefinition>()

        if(ref is VyperSelfAccessExpression) res.addAll((element.file as VyperFile)
                                                .getStatements().filter { it is VyperFunctionDefinition}.map{it as VyperFunctionDefinition})

        return res
    }

    private fun <T> Sequence<T>.takeWhileInclusive(pred: (T) -> Boolean): Sequence<T> {
        var shouldContinue = true
        return takeWhile {
            val result = shouldContinue
            shouldContinue = pred(it)
            result
        }
    }

    //now only for self.var
    //typechecking to be implemented
    fun resolveSelfAccessVarLiteral(element: VyperSelfAccessExpression, id: VyperVarLiteral): Collection<PsiElement> {

        return resolveSelfAccessVarLiteralRec(element)
                .filter {it.name == id.name}

    }

    fun resolveSelfAccessVarLiteralRec(element: VyperSelfAccessExpression): Collection<VyperNamedElement>{
        return (element.file as VyperFile).getStatements().filter { it is VyperStateVariableDeclaration }.map{it as VyperStateVariableDeclaration}
    }
}

data class FunctionResolveResult(val psiElement: PsiElement, val usingLibrary: Boolean = false)