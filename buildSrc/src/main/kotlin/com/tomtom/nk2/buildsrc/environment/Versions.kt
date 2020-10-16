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

package com.tomtom.nk2.buildsrc.environment

import org.gradle.api.JavaVersion

/**
 * An object filled with constants for version numbers used throughout the project.
 */
object Versions {
    // Gradle and plugins
    @Suppress("unused")
    // Note: When updating the Android Gradle plugin, read `buildSrc/tasks/setupEnv.gradle.kts`.
    const val ANDROID_PLUGIN = "4.0.1"
    @Suppress("unused")
    const val ARTIFACTORY_PLUGIN = "4.13.0"
    val JAVA_COMPATIBILITY = JavaVersion.VERSION_1_8
    const val JVM = "1.8"
    const val KOTLIN = "1.3.72"
    const val KTLINT_CORE = "0.37.2"
    @Suppress("unused")
    const val KTLINT_PLUGIN = "9.2.1"
    const val KTLINT_TEST = "0.36.0"
    const val KOTLINX_COROUTINES = "1.3.5"
    const val KOTLIN_POET = "1.5.0"
    @Suppress("unused")
    const val NAVTEST_PLUGIN = "5.0.3"
    @Suppress("unused")
    const val NAVUI_EMULATORS_PLUGIN = "0.42"
    @Suppress("unused")
    const val SONARQUBE_PLUGIN = "3.0"
    @Suppress("unused")
    const val JACOCO_PLUGIN = "0.8.5"

    // Android
    const val BUILD_TOOLS = "29.0.3"
    const val COMPILE_SDK = 29
    // TODO(IVI-674): Increase min and target SDK versions when possible.
    const val MIN_SDK = 27
    const val TARGET_SDK = 27
    const val ANDROIDX_ANNOTATION = "1.1.0"
    const val ANDROIDX_APPCOMPAT = "1.1.0"
    const val ANDROIDX_CARD_VIEW = "1.0.0"
    const val ANDROIDX_CONSTRAINT_LAYOUT = "1.1.0"
    const val ANDROIDX_DATABINDING_COMPILER = "4.0.1"
    const val ANDROIDX_DYNAMIC_ANIMATION = "1.0.0"
    const val ANDROIDX_KTX = "1.2.0"
    const val ANDROIDX_LIFECYCLE = "2.2.0"
    const val ANDROIDX_MEDIA = "1.1.0"
    const val ANDROIDX_RECYCLER_VIEW = "1.1.0"

    // NavKit 2
    // These version numbers are taken from the NavKit 2 Android example app version
    // nk2-example-app-android 15.3.0.
    const val NAVKIT2_ANALYTICS_PROXY = "5.0.2"
    const val NAVKIT2_FRAMEWORK_CLIENTLIB_COMMON = "6.0.13"
    const val NAVKIT2_FRAMEWORK_HTTP = "4.1.2"
    const val NAVKIT2_FRAMEWORK_PROTOBUFCOMMS_MEMCHANNEL = "0.6.10"
    const val NAVKIT2_MAPDISPLAY_CLIENTLIB = "11.4.7"
    const val NAVKIT2_MAPDISPLAY_CLIENTLIB_TRAFFIC_RENDERER = "14.6.4"
    const val NAVKIT2_MAPDISPLAY_CLIENTLIB_TRIP_RENDERER = "29.0.7"
    const val NAVKIT2_MAPDISPLAY_ONBOARDSERVICE = "9.1.21"
    const val NAVKIT2_NAVIGATION_DRIVINGASSISTANCE_CLIENTLIB = "10.1.20"
    const val NAVKIT2_NAVIGATION_DRIVINGASSISTANCE_ONBOARD = "11.1.3"
    const val NAVKIT2_NAVIGATION_TRIP_CLIENTLIB = "30.0.4"
    const val NAVKIT2_NAVIGATION_ONBOARDSERVICE = "13.0.4"
    const val NAVKIT2_NAVIGATION_POSITIONING = "8.1.0"
    const val NAVKIT2_NEXTINSTRUCTIONPANEL = "0.1.48"

    // LNS
    // These version numbers are taken from the TomTom Online SDK's connectivity services repo
    // https://maven.tomtom.com:8443/nexus/content/repositories/snapshots-private/com/tomtom/online/sdk-connectivity-server-bt-le/
    // TODO(IVI-594): Remove the snapshot when LNS will move all libraries in the public one.
    // This repository version has been confirmed by LNS that it is stable.
    const val TOMTOM_ONLINE_SDK = "0.1.143-SNAPSHOT"

    // Alexa Auto
    const val AMAZON_ALEXA_AUTO_SDK = "2.3"

    // Exoplayer - used for Alexa Auto SDK without the to be released message broker.
    // TODO(IVI-1196): Migration to using the Alexa messagebroker extension should allow this
    //  dependency to be removed.
    const val EXOPLAYER = "2.7.1"

    // Automotive
    const val ANDROID_CAR = "0.0.11"

    // Util
    const val GLIDE = "4.11.0"
    const val GSON = "2.8.6"
    const val GUAVA_JRE = "29.0-jre"
    const val JSCIENCE = "4.3.1"
    const val PHONE_NUMBER = "8.12.3"
    const val SQLITE_JDBC = "3.32.3"
    const val TRACE_EVENTS = "1.0.17"

    // Testing
    const val ANDROID_FRAGMENT_TESTING = "1.2.5"
    const val ANDROID_LINT = "27.0.0"
    const val ANDROIDX_ARCH_CORE_TESTING = "2.1.0"
    const val ANDROIDX_TEST = "1.2.0"
    const val ANDROIDX_TEST_ESPRESSO = "3.1.0"
    const val ANDROIDX_TEST_EXT = "1.1.0"
    const val ANDROIDX_TEST_RULES = "1.2.0"
    const val ANDROIDX_TEST_UI_AUTOMATOR = "2.2.0"
    const val ASSERTJ = "3.12.2"
    const val EQUALS_VERIFIER = "3.4"
    const val JUNIT = "4.12"
    const val KTOR = "1.3.2"
    const val MOCKK = "1.9.3"
    const val RESTRICTION_BYPASS = "2.2"
    const val ROBOLECTRIC = "4.3.1"
}
