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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.string_resource_helper.StringResolver

/**
 * A view model class for Information Control.
 */
open class TtiviInformationControlViewModel(
    val styling: Styling,
    val roundedImage: TtiviCardViewRoundedImageViewModel = TtiviCardViewRoundedImageViewModel(),
    val icon: LiveData<DrawableResolver> = MutableLiveData(),
    val text1: LiveData<StringResolver> = MutableLiveData(),
    val text2: LiveData<StringResolver> = MutableLiveData(),
    val text3: LiveData<StringResolver> = MutableLiveData(),
    val actionType: LiveData<ActionType>,
    val iconTintMatchesActionType: LiveData<Boolean>,
    val onClick: (() -> Unit)? = null,
    val additionalText: LiveData<StringResolver> = MutableLiveData(),
    val additionalIcon: LiveData<DrawableResolver> = MutableLiveData(),
    val additionalActionType: LiveData<ActionType>,
    val additionalIconTintMatchesAdditionalActionType: LiveData<Boolean>,
    val onAdditionalAreaClick: (() -> Unit)? = null
) {
    // TODO(IVI-1806): Update this enum when UX spec will define the different use cases.
    enum class ActionType {
        PRIMARY,
        SECONDARY,
        ACCEPTANCE,
        DESTRUCTIVE
    }

    data class Styling(
        val paddingTemplate: LiveData<Padding> = MutableLiveData(Padding.TEMPLATE_1),
        val imageSectionVerticalAlignment: VerticalAlignment = VerticalAlignment.CENTERED,
        val textSectionVerticalAlignment: VerticalAlignment = VerticalAlignment.CENTERED,
        val additionalSectionVerticalAlignment: VerticalAlignment = VerticalAlignment.CENTERED,
        val textLine1Style: TextStyle = TextStyle.STYLE_1,
        val textLine2Style: TextStyle = TextStyle.STYLE_2,
        val textLine3Style: TextStyle = TextStyle.STYLE_6
    ) {
        /**
         * Padding templates, see https://zpl.io/aB0YwNA.
         */
        enum class Padding {
            TEMPLATE_1,
            TEMPLATE_2,
            TEMPLATE_3,
            TEMPLATE_4,
            TEMPLATE_5
        }

        /**
         * Vertical alignments options, see https://zpl.io/aB0YwNA.
         */
        enum class VerticalAlignment {
            CENTERED,
            DOCKED_TO_TOP
        }

        enum class TextStyle {
            STYLE_1,
            STYLE_2,
            STYLE_3,
            STYLE_4,
            STYLE_5,
            STYLE_6
        }
    }
}
