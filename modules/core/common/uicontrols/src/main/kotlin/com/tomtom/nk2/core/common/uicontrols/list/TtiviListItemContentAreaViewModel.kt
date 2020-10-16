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

package com.tomtom.nk2.core.common.uicontrols.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.string_resource_helper.StringResolver
import com.tomtom.nk2.core.common.uicontrols.view.TtiviCardViewRoundedImageViewModel
import com.tomtom.nk2.core.common.uicontrols.view.TtiviInformationControlViewModel

/**
 * Class for all content area ViewModel in the list.
 */
abstract class TtiviListItemContentAreaViewModel(
    styling: Styling,
    roundedImage: TtiviCardViewRoundedImageViewModel = TtiviCardViewRoundedImageViewModel(),
    icon: LiveData<DrawableResolver> = MutableLiveData(),
    text1: LiveData<StringResolver> = MutableLiveData(),
    text2: LiveData<StringResolver> = MutableLiveData(),
    text3: LiveData<StringResolver> = MutableLiveData(),
    actionType: LiveData<ActionType>,
    iconTintMatchesActionType: LiveData<Boolean>,
    onClick: () -> Unit = {},
    additionalText: LiveData<StringResolver> = MutableLiveData(),
    additionalIcon: LiveData<DrawableResolver> = MutableLiveData(),
    additionalActionType: LiveData<ActionType>,
    additionalIconTintMatchesAdditionalActionType: LiveData<Boolean>,
    onAdditionalAreaClick: () -> Unit = {}
) :
    TtiviListItemViewModel,
    TtiviInformationControlViewModel(
        styling,
        roundedImage,
        icon,
        text1,
        text2,
        text3,
        actionType,
        iconTintMatchesActionType,
        onClick,
        additionalText,
        additionalIcon,
        additionalActionType,
        additionalIconTintMatchesAdditionalActionType,
        onAdditionalAreaClick
    ) {
    override val type: TtiviListItemViewModel.ListItemType =
        TtiviListItemViewModel.ListItemType.CONTENT
}
