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

package com.tomtom.nk2.core.common.uicontrols.image

import android.graphics.drawable.GradientDrawable
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.resourceresolution.drawable.GradientDrawableResolver
import com.tomtom.nk2.core.common.uicontrols.R
import kotlin.math.absoluteValue

/**
 * The list of gradient colors that may be used for placeholder image generation.
 */
private val gradientList = listOf(
    R.attr.ttivi_gradient_01,
    R.attr.ttivi_gradient_02,
    R.attr.ttivi_gradient_03,
    R.attr.ttivi_gradient_04,
    R.attr.ttivi_gradient_05,
    R.attr.ttivi_gradient_06,
    R.attr.ttivi_gradient_07,
    R.attr.ttivi_gradient_08,
    R.attr.ttivi_gradient_09,
    R.attr.ttivi_gradient_10
)

/**
 * This function will return a [DrawableResolver]. The [DrawableResolver] is chosen based on the
 * hashcode of the given objects in [args].
 *
 * For the same objects the same [DrawableResolver] will be returned.
 *
 * @throws [IllegalArgumentException] if the [gradientList] is empty.
 */
fun generatePlaceholderImage(vararg args: Any): DrawableResolver {
    return GradientDrawableResolver(
        GradientDrawable.Orientation.TL_BR,
        gradientList[(args.contentDeepHashCode() % gradientList.size).absoluteValue]
    )
}
