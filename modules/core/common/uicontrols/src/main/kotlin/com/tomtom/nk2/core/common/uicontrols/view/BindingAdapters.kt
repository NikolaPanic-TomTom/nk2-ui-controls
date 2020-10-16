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

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.use
import androidx.databinding.BindingAdapter
import com.tomtom.nk2.api.common.resourceresolution.getColorByAttr
import com.tomtom.nk2.core.common.databinding.setConstraintVerticalBias
import com.tomtom.nk2.core.common.databinding.setPaddingHorizontal
import com.tomtom.nk2.core.common.databinding.setPaddingVertical
import com.tomtom.nk2.core.common.uicontrols.R
import com.tomtom.nk2.core.common.uicontrols.view.TtiviInformationControlViewModel.ActionType
import com.tomtom.nk2.core.common.uicontrols.view.TtiviInformationControlViewModel.Styling

/**
 * Updates vertical alignment for a view inside ConstraintLayout.
 */
@BindingAdapter("layout_constraintVertical_bias")
fun View.setConstraintVerticalBias(alignment: Styling.VerticalAlignment) {
    val bias = when (alignment) {
        Styling.VerticalAlignment.CENTERED -> 0.5F
        Styling.VerticalAlignment.DOCKED_TO_TOP -> 0.0F
    }

    setConstraintVerticalBias(bias)
}

/**
 * Updates padding according to a padding template.
 */
@BindingAdapter("ttiviInformationControlPaddingTemplate")
fun View.setInformationControlPaddingTemplate(paddingTemplate: Styling.Padding) {
    val paddingStyle = when (paddingTemplate) {
        Styling.Padding.TEMPLATE_1 -> R.style.TtiviInformationControl_Padding_1
        Styling.Padding.TEMPLATE_2 -> R.style.TtiviInformationControl_Padding_2
        Styling.Padding.TEMPLATE_3 -> R.style.TtiviInformationControl_Padding_3
        Styling.Padding.TEMPLATE_4 -> R.style.TtiviInformationControl_Padding_4
        Styling.Padding.TEMPLATE_5 -> R.style.TtiviInformationControl_Padding_5
    }

    val paddingHorizontalId = android.R.attr.paddingHorizontal
    val paddingVerticalId = android.R.attr.paddingVertical
    val sortedAttrs = listOf(paddingHorizontalId, paddingVerticalId).sorted().toIntArray()

    context.obtainStyledAttributes(paddingStyle, sortedAttrs).use {
        setPaddingHorizontal(it.getDimension(sortedAttrs.indexOf(paddingHorizontalId), 0.0F))
        setPaddingVertical(it.getDimension(sortedAttrs.indexOf(paddingVerticalId), 0.0F))
    }
}

/**
 * Updates the text style according to a predefined Information Control text style.
 */
@BindingAdapter("ttiviInformationControlTextAppearance")
fun TextView.setInformationControlTextAppearance(style: Styling.TextStyle) {
    val textStyle = when (style) {
        Styling.TextStyle.STYLE_1 -> R.style.TtiviText_InformationControl_TextStyle1
        Styling.TextStyle.STYLE_2 -> R.style.TtiviText_InformationControl_TextStyle2
        Styling.TextStyle.STYLE_3 -> R.style.TtiviText_InformationControl_TextStyle3
        Styling.TextStyle.STYLE_4 -> R.style.TtiviText_InformationControl_TextStyle4
        Styling.TextStyle.STYLE_5 -> R.style.TtiviText_InformationControl_TextStyle5
        Styling.TextStyle.STYLE_6 -> R.style.TtiviText_InformationControl_TextStyle6
    }
    setTextAppearance(textStyle)
}

/**
 * A helper data binding that allows passing a [TtiviInformationControlViewModel.ActionType] and a
 * tintMatchesActionType without manually resolving the value through the context.
 */
@BindingAdapter("ttiviActionType", "ttiviTintMatchesActionType")
fun ImageView.setInformationControlIconTint(
    ttiviActionType: ActionType,
    ttiviTintMatchesActionType: Boolean
) {
    // TODO(IVI-1806): Update colors when UX spec will define the different use cases.
    imageTintList = if (ttiviTintMatchesActionType) {
        ColorStateList.valueOf(
            context.getColorByAttr(
                when (ttiviActionType) {
                    ActionType.PRIMARY -> R.attr.ttivi_color_information_control_icon_primary
                    ActionType.SECONDARY -> R.attr.ttivi_color_information_control_icon_secondary
                    ActionType.ACCEPTANCE -> R.attr.ttivi_color_information_control_icon_acceptance
                    ActionType.DESTRUCTIVE ->
                        R.attr.ttivi_color_information_control_icon_destructive
                }
            )
        )
    } else {
        // No tint applied.
        null
    }
}
