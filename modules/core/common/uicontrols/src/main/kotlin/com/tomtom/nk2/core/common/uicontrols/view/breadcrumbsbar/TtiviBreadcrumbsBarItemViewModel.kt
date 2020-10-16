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

package com.tomtom.nk2.core.common.uicontrols.view.breadcrumbsbar

import androidx.lifecycle.LiveData
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.string_resource_helper.StringResolver

/**
 * View model for items inside the breadcrumbs bar.
 *
 * @param label The label to show for the item.
 * @param icon An optional icon to show for the item.
 * @param onClick Callback to receive click for the item's click events.
 */
data class TtiviBreadcrumbsBarItemViewModel(
    val label: LiveData<StringResolver>,
    val icon: LiveData<DrawableResolver?>?,
    val onClick: () -> Unit
)
