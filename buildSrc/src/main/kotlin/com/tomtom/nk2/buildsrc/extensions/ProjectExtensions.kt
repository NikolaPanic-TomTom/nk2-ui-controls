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

package com.tomtom.nk2.buildsrc.extensions

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.tomtom.navtest.NavTestExtension
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

val Project.android: TestedExtension
    get() = extensions.getByType(TestedExtension::class.java)

val Project.androidPackageName: String
    get() {
        val packageName = android.sourceSets["main"]?.manifest?.srcFile?.let { manifest ->
            groovy.util.XmlSlurper().parse(manifest).getProperty("@package").toString()
        }
        requireNotNull(packageName)
        require(packageName.isNotEmpty())
        return packageName
    }

val Project.androidLib: LibraryExtension?
    get() = extensions.findByType(LibraryExtension::class.java)

fun Project.android(action: Action<TestedExtension>) =
    action.execute(android)

fun TestedExtension.kotlinOptions(action: Action<KotlinJvmOptions>) =
    action.execute((this as ExtensionAware).extensions.getByType(KotlinJvmOptions::class.java))

fun ExtensionContainer.android(): BaseExtension =
    this.findByType(BaseExtension::class) ?:
        error("Missing android extension. Is the android plugin applied?")

val TaskContainer.test: TaskProvider<org.gradle.api.tasks.testing.Test>
    get() = named<org.gradle.api.tasks.testing.Test>("test")

/**
 * Return the NavTest extension instance.
 * This can for example be used to retrieve the test output directory.
 */
val Project.navTestRoot: NavTestExtension
    get() = rootProject.extensions.getByType(NavTestExtension::class.java)

/**
 * Retrieves a value from a project's `gradle.properties` files.
 */
fun Project.getGradleProperty(key: String, default: Boolean) =
    properties[key]?.toString()?.toBoolean() ?: default

/**
 * Adds a given [taskProvider] as a dependency to a root task. The root task is created when it
 * doesn't exist yet.
 *
 * @param taskProvider The taskProvider to add to the root task.
 * @param rootTaskName The name of the root task.
 * @param rootTaskDescription The description of the root task.
 * @param rootTaskGroup The group of the root task.
 */
fun Project.addTaskToRootTask(
    taskProvider: TaskProvider<*>,
    rootTaskName: String,
    rootTaskDescription: String,
    rootTaskGroup: String
) {
    val rootTask = rootProject.tasks.findByName(rootTaskName)
        ?: rootProject.tasks.register(rootTaskName) {
            description = rootTaskDescription
            group = rootTaskGroup
        }.get()
    rootTask.dependsOn(taskProvider)
}

/**
 * Retrieves a property from a project's ExtraProperties extension if available, or a default value
 * if not.
 *
 * @param propertyName The name of the property to attempt to retrieve.
 * @param defaultValue The default value to return if the property is not set.
 */
fun ExtraPropertiesExtension.getOrDefault(propertyName: String, defaultValue: Any): Any =
    if (has(propertyName)) {
        get(propertyName)!!
    } else {
        defaultValue
    }

/**
 * Assert variant for Gradle scripts.
 *
 * Assertions don't normally work in Gradle: this variant fails the script execution immediately.
 *
 * @param value Expression to verify.
 * @param lazyMessage Expression to report as error message.
 */
fun gradleAssert(value: Boolean, lazyMessage: () -> String) {
    if (!value) {
        throw GradleException(lazyMessage())
    }
}

/**
 * Mark a sub-project to be excluded from the code coverage report.
 */
fun Project.excludeFromCoverage() {
    extra.set("excludeFromCoverage", true)
}

/**
 * Return whether a project was removed from the code coverage report.
 */
fun Project.isExcludedFromCoverage(): Boolean =
    extensions.findByType(ExtraPropertiesExtension::class)?.has("excludeFromCoverage")
        ?: false

fun Project.jacoco(action: Action<JacocoPluginExtension>) =
    action.execute(extensions.getByType(JacocoPluginExtension::class.java))

fun Project.sonarqube(action: Action<org.sonarqube.gradle.SonarQubeExtension>) =
    action.execute(extensions.getByType(org.sonarqube.gradle.SonarQubeExtension::class.java))
