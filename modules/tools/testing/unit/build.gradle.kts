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

import com.tomtom.nk2.buildsrc.environment.Libraries

dependencies {
    api(project(":tools_testing_assertion"))
    api(project(":tools_testing_common"))
    api(project(":tools_testing_mock"))
    api(Libraries.Android.LIFECYCLE_LIVE_DATA)
    api(Libraries.Kotlin.COROUTINES_CORE)
    api(Libraries.Kotlin.COROUTINES_TEST)
    api(Libraries.Testing.ANDROID_TEST_CORE)
    api(Libraries.Testing.ANDROID_TEST_EXT_JUNIT)
    api(Libraries.Testing.EQUALS_VERIFIER)
    api(Libraries.Testing.ROBOLECTRIC) {
        exclude(group = "commons-logging", module = "commons-logging")
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
        exclude(group = "junit", module = "junit")
        exclude(group = "com.google.auto.service", module = "auto-service")
    }
}
