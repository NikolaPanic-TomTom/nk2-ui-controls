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

import androidx.annotation.AttrRes
import com.tomtom.nk2.core.common.uicontrols.QuantityIconDrawableResolver
import com.tomtom.nk2.core.common.uicontrols.R

/**
 * Helper function to create a primary quantity icon drawable resolver that implements visual design
 * specification required by UX.
 * https://app.zeplin.io/project/5d4bd0787d4f119b00da5646/screen/5ee873b6dfc05198ba3dc7f4
 */
fun createPrimaryButtonQuantityIconDrawableResolver(
    quantity: Int,
    @AttrRes iconSize: Int = R.attr.ttivi_icon_size_s,
    @AttrRes backgroundColor: Int = R.attr.ttivi_color_accent_extra_dark,
    @AttrRes textFont: Int = R.attr.ttivi_font_medium,
    @AttrRes textColor: Int = R.attr.ttivi_color_button_text_primary,
    @AttrRes textSize: Int = R.attr.ttivi_text_size_h5,
    @AttrRes letterSpacing: Int = R.attr.ttivi_text_letter_spacing_to_text_size_ratio_h5
) = QuantityIconDrawableResolver(
    quantity = quantity,
    iconSize = iconSize,
    backgroundColor = backgroundColor,
    textFont = textFont,
    textColor = textColor,
    textSize = textSize,
    letterSpacing = letterSpacing
)
