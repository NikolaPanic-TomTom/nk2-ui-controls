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

package com.tomtom.nk2.core.common.uicontrols.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.use
import com.tomtom.nk2.core.common.uicontrols.R

/**
 * A reusable UI control that is used for displaying text labels.
 *
 * @param context The display context.
 * @param attrs The attribute set provided in the layout XML file.
 */
open class TtiviTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    /**
     * Constructor that is called when inflating the control from XML.
     */
    constructor(context: Context, attributeSet: AttributeSet) : this(
        context, attributeSet, 0
    )

    /**
     * Simple constructor to use when creating the control from code.
     */
    constructor (context: Context) : this(context, null, 0)

    enum class TextSizeIdentifier(@StyleRes val textAppearanceRes: Int) {
        H1(R.style.TtiviText_TextAppearance_H1),
        H2(R.style.TtiviText_TextAppearance_H2),
        H3(R.style.TtiviText_TextAppearance_H3),
        H4(R.style.TtiviText_TextAppearance_H4),
        H5(R.style.TtiviText_TextAppearance_H5),
        B1(R.style.TtiviText_TextAppearance_B1),
        B2(R.style.TtiviText_TextAppearance_B2)
    }

    /**
     * [TextWeight] reflects the attribute enum [ttiviTextWeight] in the file attrs.xml. Keep this
     * file up to date when changing this enum.
     */
    enum class TextWeight(@AttrRes val fontAttr: Int) {
        BOLD(R.attr.ttivi_font_bold),
        MEDIUM(R.attr.ttivi_font_medium),
        THIN(R.attr.ttivi_font_thin)
    }

    private var _ttiviTextSizeIdentifier: TextSizeIdentifier = TextSizeIdentifier.H5
    var ttiviTextSizeIdentifier: TextSizeIdentifier
        set(value) {
            if (value != _ttiviTextSizeIdentifier) {
                _ttiviTextSizeIdentifier = value
                updateTextAppearance()
            }
        }
        get() = _ttiviTextSizeIdentifier

    private var _ttiviTextWeight: TextWeight = TextWeight.MEDIUM
    var ttiviTextWeight: TextWeight
        set(value) {
            if (value != _ttiviTextWeight) {
                _ttiviTextWeight = value
                updateTextAppearance()
            }
        }
        get() = _ttiviTextWeight

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TtiviTextView, defStyleAttr, 0).use { a ->
            readCustomAttributes(a)
        }
        updateTextAppearance()
    }

    private fun updateTypeface() {
        context.theme.obtainStyledAttributes(intArrayOf(_ttiviTextWeight.fontAttr))
            .use {
                val fontRes = it.getResourceId(
                    0 /* Only one item is in the style array being queried above. */,
                    NOT_FOUND_ID
                )
                require(fontRes != NOT_FOUND_ID)
                typeface = context.resources.getFont(fontRes)
            }
    }

    private fun updateTextAppearance(@StyleRes resId: Int? = null) {
        // The text appearance must be set (or reset) before the typeface, otherwise the weight of
        // the text does not get set correctly by Android and the previous typeface setting is lost.
        super.setTextAppearance(ttiviTextSizeIdentifier.textAppearanceRes)
        resId?.let { super.setTextAppearance(it) }
        updateTypeface()
    }

    private fun readCustomAttributes(typedArray: TypedArray) {
        _ttiviTextSizeIdentifier = TextSizeIdentifier.values()[
            typedArray.getInteger(
                R.styleable.TtiviTextView_ttiviTextSizeIdentifier,
                ttiviTextSizeIdentifier.ordinal
            )
        ]

        _ttiviTextWeight = TextWeight.values()[
            typedArray.getInteger(
                R.styleable.TtiviTextView_ttiviTextWeight, ttiviTextWeight.ordinal
            )
        ]
    }

    override fun setTextAppearance(@StyleRes resId: Int) {
        context.obtainStyledAttributes(resId, R.styleable.TtiviTextView).use { a ->
            readCustomAttributes(a)
        }
        updateTextAppearance(resId)
    }

    companion object {
        private const val NOT_FOUND_ID = -1
    }
}
