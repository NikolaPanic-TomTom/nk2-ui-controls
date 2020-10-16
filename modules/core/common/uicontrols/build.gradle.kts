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
    api(project(":api_common_resourceresolution"))
    api(project(":api_common_string_resource_helper"))
    api(Libraries.Android.APPCOMPAT)
    api(Libraries.Android.RECYCLER_VIEW)

    implementation(project(":core_common_animation"))
    implementation(project(":core_common_databinding"))
    implementation(project(":core_common_theme"))
    implementation(project(":core_common_livedata"))
    implementation(Libraries.Android.CARD_VIEW)
    implementation(Libraries.Android.CONSTRAINT_LAYOUT)
    implementation(Libraries.Util.GLIDE)

    // Allows data binding in tests.
    kaptAndroidTest(Libraries.Android.DATABINDING_COMPILER)

    testImplementation(project(":tools_testing_unit"))
}
