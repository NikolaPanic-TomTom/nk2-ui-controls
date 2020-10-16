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

plugins {
    id("kotlin-kapt")
}

android {
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":api_common_resourceresolution"))
    implementation(project(":api_common_string_resource_helper"))
    implementation(project(":core_common_util"))

    implementation(Libraries.Android.APPCOMPAT)
    implementation(Libraries.Android.CARD_VIEW)
    implementation(Libraries.Android.CONSTRAINT_LAYOUT)
    implementation(Libraries.Android.LIFECYCLE_LIVE_DATA)
}
