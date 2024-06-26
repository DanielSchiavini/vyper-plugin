package org.vyperlang.plugin.annotators

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import org.vyperlang.plugin.compile.VyperCompiler
import org.vyperlang.plugin.psi.VyperFile
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

class VyperCompilerListener(val project: Project) : PropertyChangeListener {
    override fun propertyChange(evt: PropertyChangeEvent?) {
        val data = evt!!.newValue as VyperCompiler.CompilerMessage

        ApplicationManager.getApplication().runReadAction {

            val psiFile = PsiManager.getInstance(project).findFile(data.file)
            //what if user picks another file?
            val document = PsiDocumentManager.getInstance(project).getDocument(psiFile!!)
            for (report in data.error) {
                val start = document!!.getLineStartOffset(report.line - 1)
                val end = document.getLineEndOffset(report.line - 1)
                val message = report.msg
                CompilerOutput.messages.add(CompilerMessage(TextRange(start, end), message))
            }
            DaemonCodeAnalyzer.getInstance(project).restart()
        }
    }


    fun listenAnalysis() {
        VyperCompiler.addListener(this)
    }
}

/**
 * Annotator that listens to the compiler output and annotates the file accordingly
 */
class CompilerAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is VyperFile) {
            for (message in CompilerOutput.messages) {
                holder.newAnnotation(HighlightSeverity.ERROR, message.message)
            }
            CompilerOutput.messages = mutableListOf()
        }
    }
}

object CompilerOutput {
    var messages: MutableList<CompilerMessage> = mutableListOf()
}

data class CompilerMessage(val range: TextRange, val message: String)