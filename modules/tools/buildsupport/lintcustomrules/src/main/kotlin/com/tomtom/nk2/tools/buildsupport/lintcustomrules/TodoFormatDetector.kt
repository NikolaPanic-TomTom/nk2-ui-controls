/*
 * Copyright (c) 2020 - 2020 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * licensee agreement between you and TomTom. If you are the licensee, you are only permitted
 * to use this Software in accordance with the terms of your license agreement. If you are
 * not the licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_AND_RESOURCE_FILES
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.XmlContext
import com.android.utils.forEach
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UFile
import org.w3c.dom.Document
import org.w3c.dom.Node

/**
 * Lint rule that checks the format of a to-do tag is as it should be.
 */
@Suppress("UnstableApiUsage")
class TodoFormatDetector : ResourceXmlDetector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement?>> {
        return listOf(UFile::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitFile(node: UFile) {
                for (documentation in node.getAllDocumentation()) {
                    checkDocumentation(context, documentation, null, null, documentation.text)
                }
            }

            fun UFile.getAllDocumentation(): List<PsiElement> {
                val documentation = mutableListOf<PsiElement>()
                sourcePsi.accept(object : PsiRecursiveElementWalkingVisitor() {
                    override fun visitComment(comment: PsiComment) {
                        documentation += comment
                        super.visitComment(comment)
                    }

                    override fun visitElement(element: PsiElement?) {
                        if (element is KDoc) {
                            documentation += element
                        }
                        super.visitElement(element)
                    }
                })
                return documentation
            }
        }
    }

    override fun getApplicableAttributes(): Collection<String>? = ALL

    override fun visitDocument(context: XmlContext, document: Document) =
        checkXml(context, document)

    private fun checkXml(context: XmlContext, node: Node) {
        if (node.nodeType == Node.COMMENT_NODE) {
            checkDocumentation(null, null, context, node, node.nodeValue)
        }
        node.childNodes.forEach {
            checkXml(context, it)
        }
    }

    private fun checkDocumentation(
        javaContext: JavaContext?,
        javaNode: PsiElement?,
        xmlContext: XmlContext?,
        xmlNode: Node?,
        documentation: CharSequence
    ) {
        TODO_REGEX.findAll(documentation).forEach { todo ->
            val message = when {
                todo.groupValues[2] != "TODO" -> "Wrong casing."
                todo.groupValues[3].isEmpty() -> "Missing JIRA ticket reference."
                todo.groupValues[4].isEmpty() -> "Missing colon."
                todo.groupValues[5].isEmpty() -> "Missing description."
                else -> return@forEach
            }

            val todoStartIndex = todo.range.first
            val todoLength = todo.groupValues[1].length

            if (javaContext != null && javaNode != null) {
                val location = javaContext.getRangeLocation(javaNode, todoStartIndex, todoLength)
                javaContext.report(ISSUE, javaNode, location, message)
            } else if (xmlContext != null && xmlNode != null) {
                val location =
                    xmlContext.getLocation(xmlNode, todoStartIndex, todoStartIndex + todoLength)
                xmlContext.report(ISSUE, xmlNode, location, message)
            }
        }
    }

    companion object {
        private val TODO_REGEX = Regex(
            """(\b(TODO)\b(\([A-Z]+-\d+\))?(:)?)( .)?""",
            RegexOption.IGNORE_CASE
        )

        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "TodoFormat",
            briefDescription = "Incorrect TODO format.",
            explanation = "TODOs must follow the format \"TODO(JIRA-123): Description\"",
            category = Category.CORRECTNESS,
            priority = 2,
            severity = Severity.ERROR,
            implementation = Implementation(
                TodoFormatDetector::class.java,
                JAVA_AND_RESOURCE_FILES,
                JAVA_FILE_SCOPE,
                RESOURCE_FILE_SCOPE
            )
        ).apply {
            setAndroidSpecific(false)
        }
    }
}
