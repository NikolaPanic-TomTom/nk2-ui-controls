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
import android.graphics.drawable.GradientDrawable
import androidx.annotation.AttrRes
import com.tomtom.nk2.api.common.resourceresolution.getColorArrayByAttr
import kotlinx.android.parcel.Parcelize

/**
 * A [DrawableResolver] that retrieves a gradient from a [Context]. The gradient will have a linear
 * shape.
 */
@Parcelize
data class GradientDrawableResolver(
    private val orientation: GradientDrawable.Orientation,
    @AttrRes private val gradientColorArrayAttr: Int
) : DrawableResolver {

    override fun get(context: Context) = GradientDrawable(
        orientation, context.getColorArrayByAttr(gradientColorArrayAttr)
    )
}
