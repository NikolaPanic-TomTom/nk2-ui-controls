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

package com.tomtom.nk2.api.common.resourceresolution.drawable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import kotlinx.android.parcel.Parcelize

/**
 * A [DrawableResolver] that creates a [Drawable] from a [Bitmap] and target density based on
 * the display metrics of the [Context].
 */
@Parcelize
data class BitmapDrawableResolver(private val bitmap: Bitmap) : DrawableResolver {
    override fun get(context: Context) = BitmapDrawable(context.resources, bitmap)
}
