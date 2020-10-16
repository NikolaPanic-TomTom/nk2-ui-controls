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

package com.tomtom.nk2.core.common.uicontrols

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.DENSITY_NONE
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.resourceresolution.getColorByAttr
import com.tomtom.nk2.api.common.resourceresolution.getDimensionByAttr
import com.tomtom.nk2.api.common.resourceresolution.getFontByAttr
import com.tomtom.nk2.core.common.uicontrols.QuantityIconDrawableResolver.Companion.QUANTITY_MAX_VALUE
import kotlinx.android.parcel.Parcelize

/**
 * A drawable resolver that creates an icon filled with a background color and a [quantity] value
 * with a density based on context used to resolve the icon.
 *
 * @param quantity A quantity in range [0..[QUANTITY_MAX_VALUE]]; value is clamped to those
 * boundaries otherwise.
 */
@Parcelize
data class QuantityIconDrawableResolver(
    private val quantity: Int,
    @AttrRes private val iconSize: Int,
    @AttrRes private val backgroundColor: Int,
    @AttrRes private val textFont: Int,
    @AttrRes private val textColor: Int,
    @AttrRes private val textSize: Int,
    @AttrRes private val letterSpacing: Int
) : DrawableResolver {
    override fun get(context: Context): Drawable {
        val text = quantity.coerceIn(0, QUANTITY_MAX_VALUE).toString()

        val size = context.getDimensionByAttr(iconSize)
        val iconSize = size.toInt()
        val bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888).apply {
            /**
             * Disable bitmap automatic scaling when drawing the bitmap in canvas as the canvas size
             * already takes the density into account.
             */
            density = DENSITY_NONE
        }
        val canvas = Canvas(bitmap)

        val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColorByAttr(backgroundColor)
            style = Paint.Style.FILL
        }

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColorByAttr(textColor)
            textSize = context.getDimensionByAttr(this@QuantityIconDrawableResolver.textSize)
            letterSpacing =
                context.getDimensionByAttr(this@QuantityIconDrawableResolver.letterSpacing)
            typeface = context.getFontByAttr(textFont)
        }

        val circleRadius = size / 2.0f
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, circlePaint)

        canvas.drawTextCentered(text, textPaint)

        return BitmapDrawable(context.resources, bitmap)
    }

    companion object {
        const val QUANTITY_MAX_VALUE = 99

        /**
         * Draw a text centered in the canvas clip bounds.
         */
        fun Canvas.drawTextCentered(text: String, textPaint: Paint) {
            textPaint.textAlign = Paint.Align.LEFT
            val textBounds = Rect()
            textPaint.getTextBounds(text, 0, text.length, textBounds)
            drawText(
                text,
                (clipBounds.width() - textBounds.width()) / 2.0f - textBounds.left,
                (clipBounds.height() + textBounds.height()) / 2.0f - textBounds.bottom,
                textPaint
            )
        }
    }
}
