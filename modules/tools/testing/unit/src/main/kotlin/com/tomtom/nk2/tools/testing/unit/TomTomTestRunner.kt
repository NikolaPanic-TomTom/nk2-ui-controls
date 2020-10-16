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

package com.tomtom.nk2.tools.testing.unit

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

class TomTomTestRunner(testClass: Class<*>?) : RobolectricTestRunner(testClass) {
    /**
     * Robolectric uses SDK 16 by default, which is far behind IndiGO's targeted SDK.
     * To ensure unit tests are ran against the targeted SDK, we configure it here.
     * See Versions.kt for the TARGET_SDK value.
     */
    override fun buildGlobalConfig(): Config =
        Config.Builder().setSdk(BuildConfig.TARGET_SDK).build()
}
