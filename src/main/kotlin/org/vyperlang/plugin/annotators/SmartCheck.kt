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
import org.vyperlang.plugin.analyze.SmartCheckAnalyzer
import org.vyperlang.plugin.psi.VyperFile
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

class VyperSmartCheckListener(val project: Project) : PropertyChangeListener {
    override fun propertyChange(evt: PropertyChangeEvent?) {

        val data = evt!!.newValue as SmartCheckAnalyzer.SmartCheckData
        //TODO : compiler and implement this for all messages
        ApplicationManager.getApplication().runReadAction {
            val psiFile = PsiManager.getInstance(project).findFile(data.file)
            //what if user picks another file?
            val document = PsiDocumentManager.getInstance(project).getDocument(psiFile!!)
            for (report in data.smartCheckData) {
                val start = document!!.getLineStartOffset(report.line - 1)
                val end = document.getLineEndOffset(report.line - 1)
                val message = report.ruleId
                SmartCheckOutput.messages.add(SmartCheckMessage(TextRange(start, end), message))
            }
            DaemonCodeAnalyzer.getInstance(project).restart()

        }
    }


    fun listenAnalysis() {
        SmartCheckAnalyzer.addListener(this)
    }

}

/**
 * Annotator that listens to the compiler output and adds smart check messages to the file
 */
class SmartCheckAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is VyperFile) {
            for (message in SmartCheckOutput.messages) {
                holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message.message)
            }
            SmartCheckOutput.messages = mutableListOf()
        }
    }
}

object SmartCheckOutput {
    var messages: MutableList<SmartCheckMessage> = mutableListOf()
}

data class SmartCheckMessage(val range: TextRange, val message: String)