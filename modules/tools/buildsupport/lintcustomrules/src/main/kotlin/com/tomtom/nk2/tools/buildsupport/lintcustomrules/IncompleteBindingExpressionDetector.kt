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

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.TextFormat
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr

/**
 * Lint rule that checks that a binding expression in resource files has closing brace.
 */
@Suppress("UnstableApiUsage")
class IncompleteBindingExpressionDetector : ResourceXmlDetector() {

    override fun getApplicableAttributes(): Collection<String>? = ALL

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        if (attribute.value.startsWith("@{") && !attribute.value.endsWith("}")) {
            context.report(
                ISSUE,
                attribute,
                context.getValueLocation(attribute),
                ISSUE.getExplanation(TextFormat.TEXT)
            )
        }
    }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "IncompleteBindingExpression",
            briefDescription = "Missing closing brace in binding expression.",
            explanation = "An XML attribute's value started with @{ (implying that we want " +
                "to use data binding here) but didn't end with }, preventing data binding from " +
                "working.",
            category = Category.CORRECTNESS,
            priority = 9,
            severity = Severity.ERROR,
            implementation = Implementation(
                IncompleteBindingExpressionDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )
    }
}
