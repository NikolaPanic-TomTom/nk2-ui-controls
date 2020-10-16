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

buildscript {
    apply("repositories.gradle.kts")
}
apply("repositories.gradle.kts")

plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation(kotlin("gradle-plugin", getVersion("KOTLIN")))
    implementation(kotlin("android-extensions", getVersion("KOTLIN")))
    implementation("com.android.tools.build:gradle:${getVersion("ANDROID_PLUGIN")}")
    implementation("com.google.guava:guava:${getVersion("GUAVA_JRE")}")
    implementation("org.jfrog.buildinfo:build-info-extractor-gradle:${getVersion("ARTIFACTORY_PLUGIN")}")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:${getVersion("KTLINT_PLUGIN")}")
    implementation("com.tomtom.navtest:navtest-core:${getVersion("NAVTEST_PLUGIN")}")
    implementation("com.tomtom.navui:emulators-plugin:${getVersion("NAVUI_EMULATORS_PLUGIN")}")
    implementation("org.jacoco:org.jacoco.core:${getVersion("JACOCO_PLUGIN")}")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${getVersion("SONARQUBE_PLUGIN")}")

    testImplementation("junit:junit:${getVersion("JUNIT")}")
    testImplementation("io.mockk:mockk:${getVersion("MOCKK")}")
}

/**
 * Workaround function to load versions from this module's version object. This is needed because
 * this module's build script needs to know the versions of the build tools in order to build the
 * versions object. To resolve this cyclic dependency, we parse the code file manually. This
 * approach should be avoided anywhere else as Versions can just be accessed directly.
 */
fun getVersion(versionKey: String): String {
    val versionsFile = "Versions.kt"
    val versionRegex = "$versionKey *= *\"([^\"]*)\"".toRegex()
    return fileTree(projectDir)
        .find { it.name == versionsFile }
        ?.useLines { lines ->
            lines.mapNotNull { versionRegex.find(it)?.groups?.get(1)?.value }
                .firstOrNull()
                ?: throw Exception("Could not find $versionKey in $versionsFile.")
        } ?: throw Exception("$versionsFile could not be found or read.")
}
