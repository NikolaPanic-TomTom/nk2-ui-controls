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

package com.tomtom.nk2.tools.buildsupport.lintcustomrules

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.GradleContext
import com.android.tools.lint.detector.api.GradleScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import java.util.Locale
import org.jetbrains.uast.kotlin.KotlinStringULiteralExpression
import org.jetbrains.uast.kotlin.KotlinUFunctionCallExpression

/**
 * Enforces the allowed dependencies between IndiGO modules. For more information, see `/README.md`.
 */
@Suppress("UnstableApiUsage")
class IllegalDependencyDetector : Detector(), GradleScanner {

    override fun checkDslPropertyAssignment(
        context: GradleContext,
        property: String,
        value: String,
        parent: String,
        parentParent: String?,
        propertyCookie: Any,
        valueCookie: Any,
        statementCookie: Any
    ) {
        if (parent != GRADLE_DEPENDENCIES) {
            return
        }

        val module = context.project.gradleProjectModel?.name?.let { Module.fromName(it) }
            ?: return
        val dependencyType = DependencyType.fromProperty(property)
            ?: return
        val dependencyModule = Module.fromCookie(valueCookie)
            ?: return

        if (!module.allowsDependency(dependencyType, dependencyModule)) {
            context.report(
                ISSUE,
                context.getLocation(propertyCookie),
                "${dependencyModule.name} is not allowed as $property() dependency of " +
                    "${module.name}."
            )
        }
    }

    companion object {
        private class Module(val name: String, val type: ModuleType, val category: String) {

            fun allowsDependency(dependencyType: DependencyType, dependencyModule: Module) =
                when (type) {
                    ModuleType.API -> when (dependencyType) {
                        DependencyType.API -> setOf(
                            ModuleType.API
                        )
                        DependencyType.IMPLEMENTATION -> setOf(
                            ModuleType.API,
                            ModuleType.CORE,
                            ModuleType.STOCK
                        )
                    }
                    ModuleType.CORE ->
                        setOf(
                            ModuleType.API,
                            ModuleType.CORE
                        )
                    ModuleType.INTEGRATION ->
                        when (category) {
                            "test" -> setOf(
                                ModuleType.API,
                                ModuleType.CORE,
                                ModuleType.INTEGRATION,
                                ModuleType.STOCK,
                                ModuleType.TOOLS
                            )
                            else -> setOf(
                                ModuleType.API,
                                ModuleType.CORE,
                                ModuleType.INTEGRATION,
                                ModuleType.STOCK
                            )
                        }
                    ModuleType.STOCK ->
                        setOf(
                            ModuleType.API,
                            ModuleType.CORE
                        )
                    ModuleType.TOOLS ->
                        setOf(
                            ModuleType.API,
                            ModuleType.CORE,
                            ModuleType.STOCK,
                            ModuleType.TOOLS
                        )
                }.contains(dependencyModule.type)

            companion object {
                fun fromCookie(cookie: Any) =
                    cookie.let { cookie as? KotlinUFunctionCallExpression }
                        ?.takeIf { it.methodIdentifier?.name == GRADLE_PROJECT }
                        ?.let { (it.valueArguments[0] as KotlinStringULiteralExpression).text }
                        ?.let { fromName(it) }

                fun fromName(name: String) =
                    MODULE_REGEX.matchEntire(name)?.let { match ->
                        ModuleType.from(match.groups[1]!!.value)?.let { moduleType ->
                            Module(name, moduleType, match.groups[2]!!.value)
                        }
                    }
            }
        }

        private enum class DependencyType {
            API,
            IMPLEMENTATION;

            companion object {
                fun fromProperty(property: String): DependencyType? {
                    val upperCaseProperty = property.toUpperCase(Locale.ROOT)
                    return values().find { it.name == upperCaseProperty }
                }
            }
        }

        private enum class ModuleType {
            API,
            CORE,
            INTEGRATION,
            STOCK,
            TOOLS;

            companion object {
                fun from(string: String): ModuleType? {
                    val upperCaseString = string.toUpperCase(Locale.ROOT)
                    return values().find { it.name == upperCaseString }
                }
            }
        }

        private const val GRADLE_DEPENDENCIES = "dependencies"
        private const val GRADLE_PROJECT = "project"
        private val MODULE_REGEX = Regex("^:?([a-z]+)_([a-z]+)_[a-z]+$")

        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "IllegalDependency",
            briefDescription = "Illegal module dependency.",
            explanation = """
                IndiGO modules have a different usage depending on their type, specified by the
                folder in which they live. E.g., modules in /modules/api/ are meant to be used by
                customers to create their own IndiGO plugins and must therefore maintain backward
                compatibility. Due to the different usage of modules, different restrictions apply
                to which other modules they may depend on, either on at an api or implementation
                level.
                For more information about what dependencies are valid, see /README.md.
                """,
            category = Category.COMPLIANCE,
            priority = 8,
            severity = Severity.ERROR,
            implementation = Implementation(
                IllegalDependencyDetector::class.java,
                Scope.GRADLE_SCOPE
            )
        )
    }
}
