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

import androidx.lifecycle.MutableLiveData
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.string_resource_helper.StringResolver

/**
 * Class for all headers ViewModel in the list.
 */
open class TtiviListItemHeaderViewModel : TtiviListItemViewModel {
    val headerIcon = MutableLiveData<DrawableResolver>()
    val headerText = MutableLiveData<StringResolver>()
    override val type: TtiviListItemViewModel.ListItemType =
        TtiviListItemViewModel.ListItemType.HEADER
}
