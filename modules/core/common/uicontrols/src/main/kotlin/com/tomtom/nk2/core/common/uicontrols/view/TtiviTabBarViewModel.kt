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
import androidx.lifecycle.ViewModel
import com.tomtom.nk2.core.common.livedata.DistinctMutableLiveData

/**
 * A class representing the state of a [TtiviTabBar].
 *
 * @param tabViewModels The [LiveData] feed for the individual tabs.
 * @param initialSelectedTabIndex The index of tab to be selected from when the tab bar is created.
 *  Can be left `null` to not have any tab selected.
 */
class TtiviTabBarViewModel(
    val tabViewModels: LiveData<List<TtiviTabViewModel>>,
    initialSelectedTabIndex: Int? = 0
) : ViewModel() {
    val selectedTabIndex = when (initialSelectedTabIndex) {
        null -> DistinctMutableLiveData()
        else -> DistinctMutableLiveData(initialSelectedTabIndex)
    }

    constructor(
        tabs: List<TtiviTabViewModel> = listOf(),
        initialSelectedTabIndex: Int? = 0
    ) : this(MutableLiveData(tabs), initialSelectedTabIndex)
}
