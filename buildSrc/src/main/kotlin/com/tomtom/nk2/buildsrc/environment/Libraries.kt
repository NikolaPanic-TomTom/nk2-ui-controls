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

/**
 * External libraries which modules depend on.
 */
object Libraries {
    object Android {
        const val ANDROIDX_DYNAMIC_ANIMATION = "androidx.dynamicanimation:dynamicanimation:${Versions.ANDROIDX_DYNAMIC_ANIMATION}"
        const val ANNOTATION = "androidx.annotation:annotation:${Versions.ANDROIDX_ANNOTATION}"
        const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}"
        const val CARD_VIEW = "androidx.cardview:cardview:${Versions.ANDROIDX_CARD_VIEW}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.ANDROIDX_CONSTRAINT_LAYOUT}"
        const val DATABINDING_COMPILER = "androidx.databinding:databinding-compiler:${Versions.ANDROIDX_DATABINDING_COMPILER}"
        const val KTX = "androidx.core:core-ktx:${Versions.ANDROIDX_KTX}"
        const val LIFECYCLE_COMMON_JAVA8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.ANDROIDX_LIFECYCLE}"
        const val LIFECYCLE_LIVE_DATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.ANDROIDX_LIFECYCLE}"
        const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime:${Versions.ANDROIDX_LIFECYCLE}"
        const val LIFECYCLE_SERVICE = "androidx.lifecycle:lifecycle-service:${Versions.ANDROIDX_LIFECYCLE}"
        const val LIFECYCLE_VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ANDROIDX_LIFECYCLE}"
        const val MEDIA = "androidx.media:media:${Versions.ANDROIDX_MEDIA}"
        const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Versions.ANDROIDX_RECYCLER_VIEW}"
    }

    object Kotlin {
        const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLINX_COROUTINES}"
        const val COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.KOTLINX_COROUTINES}"
        const val KOTLIN_POET = "com.squareup:kotlinpoet:${Versions.KOTLIN_POET}"
        const val KOTLIN_POET_METADATA = "com.squareup:kotlinpoet-metadata:${Versions.KOTLIN_POET}"
        const val KOTLIN_POET_METADATA_SPECS = "com.squareup:kotlinpoet-metadata-specs:${Versions.KOTLIN_POET}"
        const val REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
        const val TEST = "org.jetbrains.kotlin:kotlin-test:${Versions.KOTLIN}"
    }

    object Util {
        const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
        const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
        const val GUAVA_JRE = "com.google.guava:guava:${Versions.GUAVA_JRE}"
        const val JSCIENCE = "org.jscience:jscience:${Versions.JSCIENCE}"
        const val PHONE_NUMBER = "com.googlecode.libphonenumber:libphonenumber:${Versions.PHONE_NUMBER}"
        const val SQLITE_JDBC = "org.xerial:sqlite-jdbc:${Versions.SQLITE_JDBC}"
        const val TRACE_EVENTS = "com.tomtom.kotlin:traceevents:${Versions.TRACE_EVENTS}"
    }

    object Testing {
        const val ANDROID_FRAGMENT_TESTING = "androidx.fragment:fragment-testing:${Versions.ANDROID_FRAGMENT_TESTING}"
        const val ANDROID_LINT_API = "com.android.tools.lint:lint-api:${Versions.ANDROID_LINT}"
        const val ANDROID_LINT_CHECKS = "com.android.tools.lint:lint-checks:${Versions.ANDROID_LINT}"
        const val ANDROID_LINT_TESTS = "com.android.tools.lint:lint-tests:${Versions.ANDROID_LINT}"
        const val ANDROIDX_ARCH_CORE_TESTING = "androidx.arch.core:core-testing:${Versions.ANDROIDX_ARCH_CORE_TESTING}"
        const val ANDROID_TEST_CORE = "androidx.test:core:${Versions.ANDROIDX_TEST}"
        const val ANDROID_TEST_ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ANDROIDX_TEST_ESPRESSO}"
        const val ANDROID_TEST_ESPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib:${Versions.ANDROIDX_TEST_ESPRESSO}"
        const val ANDROID_TEST_EXT_JUNIT = "androidx.test.ext:junit-ktx:${Versions.ANDROIDX_TEST_EXT}"
        const val ANDROID_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST_RULES}"
        const val ANDROID_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
        const val ANDROID_TEST_UI_AUTOMATOR = "androidx.test.uiautomator:uiautomator:${Versions.ANDROIDX_TEST_UI_AUTOMATOR}"
        const val ASSERTJ = "org.assertj:assertj-core:${Versions.ASSERTJ}"
        const val EQUALS_VERIFIER = "nl.jqno.equalsverifier:equalsverifier:${Versions.EQUALS_VERIFIER}"
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
        const val KTLINT_CORE = "com.pinterest.ktlint:ktlint-core:${Versions.KTLINT_CORE}"
        const val KTLINT_RULESET_EXPERIMENTAL = "com.pinterest.ktlint:ktlint-ruleset-experimental:${Versions.KTLINT_CORE}"
        const val KTLINT_TEST = "com.pinterest.ktlint:ktlint-test:${Versions.KTLINT_TEST}"
        const val KTOR_CLIENT_ANDROID = "io.ktor:ktor-client-android:${Versions.KTOR}"
        const val KTOR_SERVER_NETTY = "io.ktor:ktor-server-netty:${Versions.KTOR}"
        const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
        const val MOCKK_ANDROID = "io.mockk:mockk-android:${Versions.MOCKK}"
        const val RESTRICTION_BYPASS = "com.github.ChickenHook:RestrictionBypass:${Versions.RESTRICTION_BYPASS}"
        const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"
    }

    object TomTom {
        object NavKit2 {
            const val ANALYTICS_PROXY = "com.tomtom.navkit2:analytics-proxy:${Versions.NAVKIT2_ANALYTICS_PROXY}"
            const val FRAMEWORK_CLIENTLIB_COMMON = "com.tomtom.navkit2:framework-clientlib-common:${Versions.NAVKIT2_FRAMEWORK_CLIENTLIB_COMMON}"
            const val FRAMEWORK_HTTP = "com.tomtom.navkit2:framework-http:${Versions.NAVKIT2_FRAMEWORK_HTTP}"
            const val FRAMEWORK_PROTOBUF_COMMS_MEMCHANNEL = "com.tomtom.navkit2:framework-protobufcomms-memchannel:${Versions.NAVKIT2_FRAMEWORK_PROTOBUFCOMMS_MEMCHANNEL}"
            const val MAPDISPLAY_CLIENTLIB = "com.tomtom.navkit2:mapdisplay-clientlib:${Versions.NAVKIT2_MAPDISPLAY_CLIENTLIB}"
            const val MAPDISPLAY_CLIENTLIB_TRAFFIC_RENDERER = "com.tomtom.navkit2:mapdisplay-clientlib-traffic-renderer:${Versions.NAVKIT2_MAPDISPLAY_CLIENTLIB_TRAFFIC_RENDERER}"
            const val MAPDISPLAY_CLIENTLIB_TRIP_RENDERER = "com.tomtom.navkit2:mapdisplay-clientlib-trip-renderer:${Versions.NAVKIT2_MAPDISPLAY_CLIENTLIB_TRIP_RENDERER}"
            const val MAPDISPLAY_ONBOARDSERVICE = "com.tomtom.navkit2:mapdisplay-onboardservice:${Versions.NAVKIT2_MAPDISPLAY_ONBOARDSERVICE}"
            const val NAVIGATION_DRIVINGASSISTANCE_CLIENTLIB = "com.tomtom.navkit2:navigation-drivingassistance-clientlib:${Versions.NAVKIT2_NAVIGATION_DRIVINGASSISTANCE_CLIENTLIB}"
            const val NAVIGATION_DRIVINGASSISTANCE_ONBOARD = "com.tomtom.navkit2:navigation-drivingassistance-onboardservice:${Versions.NAVKIT2_NAVIGATION_DRIVINGASSISTANCE_ONBOARD}"
            const val NAVIGATION_TRIP_CLIENTLIB = "com.tomtom.navkit2:navigation-trip-clientlib:${Versions.NAVKIT2_NAVIGATION_TRIP_CLIENTLIB}"
            const val NAVIGATION_ONBOARDSERVICE = "com.tomtom.navkit2:navigation-onboardservice:${Versions.NAVKIT2_NAVIGATION_ONBOARDSERVICE}"
            const val NAVIGATION_POSITIONING = "com.tomtom.navkit2:navigation-positioning:${Versions.NAVKIT2_NAVIGATION_POSITIONING}"
            const val NEXTINSTRUCTIONPANEL = "com.tomtom.navkit2:nextinstructionpanel:${Versions.NAVKIT2_NEXTINSTRUCTIONPANEL}"
        }

        object OnlineSdk {
            const val CONNECTIVITY_SERVER_BLE = "com.tomtom.online:sdk-connectivity-server-bt-le:${Versions.TOMTOM_ONLINE_SDK}"
        }

        object Automotive {
            const val ANDROID_CAR = "com.tomtom.ivi.ivi-automotive-sdk:android_car_lib:${Versions.ANDROID_CAR}"
        }
    }

    object Amazon {
        object Alexa {
            const val ADDRESS_BOOK = "com.amazon.alexa.aace:addressbook:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val ALEXA = "com.amazon.alexa.aace:alexa:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val AMAZON_LITE = "com.amazon.alexa.aace:amazonlite:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val APL = "com.amazon.alexa.aace:apl:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val AUTO_VOICE_CHROME = "com.amazon.alexa.aace:autovoicechrome:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val CAR_CONTROL = "com.amazon.alexa.aace:car-control:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val CBL = "com.amazon.alexa.aace:cbl:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val CONTACT_UPLOADER = "com.amazon.alexa.aace:contactuploader:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val CORE = "com.amazon.alexa.aace:core:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val MACC = "com.amazon.alexa.aace:maccandroid:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val NAVIGATION = "com.amazon.alexa.aace:navigation:${Versions.AMAZON_ALEXA_AUTO_SDK}"
            const val PHONE_CONTROL = "com.amazon.alexa.aace:phonecontrol:${Versions.AMAZON_ALEXA_AUTO_SDK}"
        }
    }

    // Exoplayer - used for Alexa Auto SDK while waiting for the yet-to-be-released message broker.
    // TODO(IVI-1196): Migration to using the Alexa messagebroker extension should allow this
    //  dependency to be removed.
    object Exoplayer {
        const val CORE = "com.google.android.exoplayer:exoplayer-core:${Versions.EXOPLAYER}"
        const val DASH = "com.google.android.exoplayer:exoplayer-dash:${Versions.EXOPLAYER}"
        const val SMOOTH_STREAMING = "com.google.android.exoplayer:exoplayer-smoothstreaming:${Versions.EXOPLAYER}"
        const val HLS = "com.google.android.exoplayer:exoplayer-hls:${Versions.EXOPLAYER}"
    }
}
