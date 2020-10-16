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

package com.tomtom.nk2.api.common.resourceresolution

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.content.res.use

/**
 * Returns the string contents of an Android text asset based on the given [assetFilePath].
 */
fun Context.readTextAsset(assetFilePath: String): String =
    assets.open(assetFilePath).bufferedReader().use { it.readText() }

/**
 * Returns the integer value that the given [attrRes] refers to.
 *
 * @throws IllegalArgumentException if the given attribute cannot be found.
 */
fun Context.getIntegerByAttr(@AttrRes attrRes: Int): Int =
    resolveAttr(attrRes).data

/**
 * Returns the float value that the given [attrRes] refers to.
 *
 * @throws IllegalArgumentException if the given attribute cannot be found.
 */
fun Context.getFloatByAttr(@AttrRes attrRes: Int): Float =
    resolveAttr(attrRes).float

/**
 * Returns the color value that the given [attrRes] refers to.
 *
 * @throws IllegalArgumentException if the given attribute cannot be found.
 */
@ColorInt
fun Context.getColorByAttr(@AttrRes attrRes: Int): Int =
    resolveAttr(attrRes).data

/**
 * Returns the color array value that the given [attrRes] refers to.
 *
 * @throws IllegalArgumentException if the given attribute cannot be found.
 */
fun Context.getColorArrayByAttr(@AttrRes attrRes: Int): IntArray =
    resolveAttr(attrRes).let { colorArrayRes ->
        resources.obtainTypedArray(colorArrayRes.resourceId).use { colorArray ->
            IntArray(colorArray.length()) { colorArray.getColor(it, 0) }
        }
    }

/**
 * Returns a pixel representation of the dimen value that the given [attrRes] refers to.
 *
 * Note: Density pixels are automatically converted to their pixel values based on the [Context]'s
 * density.
 *
 * @throws IllegalArgumentException if the given attribute cannot be found.
 */
@Px
fun Context.getDimensionByAttr(@AttrRes attrRes: Int): Float =
    resolveAttr(attrRes).getDimension(resources.displayMetrics)

/**
 * Returns the font that the given [attrRes] refers to.
 *
 * @throws IllegalArgumentException if the given attribute cannot be found.
 */
fun Context.getFontByAttr(@AttrRes attrRes: Int): Typeface =
    resolveAttr(attrRes).let { resources.getFont(it.resourceId) }

/**
 * Resolves the given [attrRes] based on the [Context]'s theme.
 *
 * @throws IllegalArgumentException if the given attribute cannot be found.
 */
private fun Context.resolveAttr(@AttrRes attrRes: Int): TypedValue =
    TypedValue().apply {
        theme.resolveAttribute(attrRes, this, true).also { attrFound -> require(attrFound) }
    }
