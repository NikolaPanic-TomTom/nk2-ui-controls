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

package com.tomtom.nk2.core.common.uicontrols.view.shadows

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.tomtom.nk2.tools.testing.mock.niceMockk
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

@Implements(AppCompatResources::class)
class ShadowAppCompatResources {
    companion object {
        @JvmStatic
        @Implementation
        @Suppress("UNUSED_PARAMETER")
        fun getDrawable(context: Context, @DrawableRes resId: Int): Drawable? =
            resId.takeIf { it > 0 }
                ?.let { niceMockk(name = "Mock from ShadowAppCompatResources.getDrawable($it)") }
    }
}
