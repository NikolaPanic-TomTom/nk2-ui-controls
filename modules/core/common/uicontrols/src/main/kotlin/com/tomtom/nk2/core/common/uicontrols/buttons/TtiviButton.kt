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

package com.tomtom.nk2.core.common.uicontrols.buttons

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.use
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.resourceresolution.getDimensionByAttr
import com.tomtom.nk2.api.common.resourceresolution.getFloatByAttr
import com.tomtom.nk2.core.common.uicontrols.R
import kotlin.math.roundToInt

/**
 * A reusable button control that implements the different visual states required by UX.
 * @param context The display context.
 * @param attrs The attribute set provided in the layout XML file.
 */
@RequiresApi(Build.VERSION_CODES.Q)
class TtiviButton(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatButton(context, attrs, defStyleAttr) {

    /**
     * Constructor that is called when inflating the control from XML.
     */
    constructor (context: Context, attrs: AttributeSet) : this(
        context,
        attrs,
        androidx.appcompat.R.attr.buttonStyle
    )

    /**
     * Simple constructor to use when creating the control from code.
     */
    constructor (context: Context) : this(context, null, androidx.appcompat.R.attr.buttonStyle)

    /**
     * [ActionType] reflects the attribute enum [ttiviActionType] in the file attrs.xml. Keep this
     * file up to date when changing this enum.
     */
    enum class ActionType { PRIMARY, SECONDARY, TERTIARY, ACCEPTANCE, DESTRUCTIVE, FLOATING }

    private val disabledAlpha: Float =
        context.getFloatByAttr(R.attr.ttivi_fraction_default_button_alpha_disabled)
    private val enabledAlpha: Float =
        context.getFloatByAttr(R.attr.ttivi_fraction_default_button_alpha_enabled)

    var ttiviDrawable: Drawable? = null
        set(value) {
            if (field != value) {
                field = value
                applyTint()
                updatePadding()
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    value,
                    compoundDrawablesRelative[1],
                    compoundDrawablesRelative[2],
                    compoundDrawablesRelative[3]
                )
            }
        }

    var ttiviDrawableTint: Int? = null
        set(value) {
            if (field != value) {
                field = value
                applyTint()
            }
        }

    private fun applyTint() {
        ttiviDrawable?.colorFilter = when (ttiviDrawableTintMatchesTextColor) {
            true -> textColors.defaultColor
            false -> ttiviDrawableTint
        }?.let {
            PorterDuffColorFilter(it, PorterDuff.Mode.MULTIPLY)
        }
    }

    var ttiviDrawableTintMatchesTextColor: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                applyTint()
            }
        }

    private var _ttiviActionType: ActionType? = null
    var ttiviActionType: ActionType
        get() = _ttiviActionType!!
        set(value) {
            if (_ttiviActionType != value) {
                _ttiviActionType = value

                setTextColor(
                    context.getColorStateList(
                        when (value) {
                            ActionType.PRIMARY,
                            ActionType.ACCEPTANCE,
                            ActionType.DESTRUCTIVE -> R.color.ttivi_button_text
                            ActionType.SECONDARY,
                            ActionType.TERTIARY,
                            ActionType.FLOATING -> R.color.ttivi_button_secondary_text
                        }
                    )
                )

                setBackgroundResource(
                    when (value) {
                        ActionType.PRIMARY -> R.drawable.ttivi_button_primary_background
                        ActionType.SECONDARY -> R.drawable.ttivi_button_secondary_background
                        ActionType.TERTIARY -> R.drawable.ttivi_button_tertiary_background
                        ActionType.ACCEPTANCE -> R.drawable.ttivi_button_acceptance_background
                        ActionType.DESTRUCTIVE -> R.drawable.ttivi_button_destructive_background
                        ActionType.FLOATING -> R.drawable.ttivi_button_floating_background
                    }
                )

                elevation = when (value) {
                    ActionType.PRIMARY,
                    ActionType.SECONDARY,
                    ActionType.TERTIARY,
                    ActionType.ACCEPTANCE,
                    ActionType.DESTRUCTIVE -> 0f
                    ActionType.FLOATING ->
                        context.getDimensionByAttr(R.attr.ttivi_radius_shadow_elevation_normal)
                }

                applyTint()
            }
        }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TtiviButton, defStyleAttr, 0).use {
            ttiviActionType = ActionType.values()[
                it.getInteger(R.styleable.TtiviButton_ttiviActionType, ActionType.PRIMARY.ordinal)
            ]
            ttiviDrawable = it.getDrawable(R.styleable.TtiviButton_ttiviDrawable)
            ttiviDrawableTint = it.getColor(R.styleable.TtiviButton_ttiviDrawableTint, Color.WHITE)
            ttiviDrawableTintMatchesTextColor =
                it.getBoolean(R.styleable.TtiviButton_ttiviDrawableTintMatchesTextColor, false)
        }

        updateAlpha()
        updatePadding()
    }

    fun setTtiviDrawable(drawableResolver: DrawableResolver?) {
        ttiviDrawable = drawableResolver?.get(context)
        measureDrawablePosition()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateAlpha()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        measureDrawablePosition()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        measureDrawablePosition()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        updatePadding()
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        updatePadding()
    }

    /**
     * Overridden method because TextView's calculation of the compound left padding always includes
     * the android:drawablePadding, even if the text itself is empty.
     */
    override fun getCompoundPaddingLeft(): Int {
        if (text.isNotEmpty()) {
            return super.getCompoundPaddingLeft()
        }

        val drawableWidth = when (layoutDirection) {
            View.LAYOUT_DIRECTION_RTL -> 0
            else -> ttiviDrawable?.intrinsicWidth ?: 0
        }

        return paddingLeft + drawableWidth
    }

    /**
     * Overridden method because TextView's calculation of the compound right padding always
     * includes the android:drawablePadding, even if the text itself is empty.
     */
    override fun getCompoundPaddingRight(): Int {
        if (text.isNotEmpty()) {
            return super.getCompoundPaddingRight()
        }

        val drawableWidth = when (layoutDirection) {
            View.LAYOUT_DIRECTION_RTL -> ttiviDrawable?.intrinsicWidth ?: 0
            else -> 0
        }

        return paddingRight + drawableWidth
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        measureDrawablePosition()
    }

    private fun updateAlpha() {
        alpha = if (isEnabled) {
            enabledAlpha
        } else {
            disabledAlpha
        }
    }

    private fun getDimensionByAttr(@AttrRes attrRes: Int) =
        context.getDimensionByAttr(attrRes).roundToInt()

    private fun updatePadding() {
        when {
            text.isEmpty() -> {
                val paddingImageOnly = getDimensionByAttr(
                    R.attr.ttivi_default_button_padding_horizontal_image_only
                )

                setPadding(
                    paddingImageOnly,
                    paddingTop,
                    paddingImageOnly,
                    paddingBottom
                )
            }
            ttiviDrawable == null -> {
                val paddingTextOnly =
                    getDimensionByAttr(R.attr.ttivi_default_button_padding_horizontal_text_only)

                setPadding(
                    paddingTextOnly,
                    paddingTop,
                    paddingTextOnly,
                    paddingBottom
                )
            }
            else -> {
                val paddingImageAndTextStart =
                    getDimensionByAttr(R.attr.ttivi_default_button_padding_start_text_and_image)
                val paddingImageAndTextEnd =
                    getDimensionByAttr(R.attr.ttivi_default_button_padding_end_text_and_image)

                setPadding(
                    paddingImageAndTextStart,
                    paddingTop,
                    paddingImageAndTextEnd,
                    paddingBottom
                )
            }
        }
        measureDrawablePosition()
    }

    private fun measureDrawablePosition() {
        compoundDrawables.first()?.let {
            val labelOffset =
                if (text.isEmpty()) 0 else calculateLabelWidth() + compoundDrawablePadding
            val contentWidth = it.intrinsicWidth + labelOffset
            val newDrawablePadding =
                ((measuredWidth - paddingStart - contentWidth - paddingEnd) / 2).coerceAtLeast(0)

            it.setBounds(
                newDrawablePadding,
                0,
                newDrawablePadding + it.intrinsicWidth,
                it.intrinsicHeight)
        }
    }

    private fun calculateLabelWidth() = paint.measureText(text.toString()).toInt()
}
