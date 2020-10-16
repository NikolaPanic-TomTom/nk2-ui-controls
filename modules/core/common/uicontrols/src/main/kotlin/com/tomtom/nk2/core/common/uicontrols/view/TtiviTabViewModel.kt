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

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.resourceresolution.drawable.ResourceDrawableResolver
import com.tomtom.nk2.api.common.string_resource_helper.ResourceStringResolver
import com.tomtom.nk2.api.common.string_resource_helper.StringResolver

/**
 * You have the option to use an [icon], an [iconUri] or both.
 * The [icon] will be immediately displayed. If an [iconUri] is provided, it will replace [icon]
 * after the download is completed.
 */
class TtiviTabViewModel(
    text: StringResolver,
    icon: DrawableResolver? = null,
    iconUri: Uri? = null,
    val isSelected: MutableLiveData<Boolean> = MutableLiveData(false)
) : ViewModel() {

    constructor(@StringRes text: Int, @DrawableRes icon: Int = 0) : this(
        ResourceStringResolver(text),
        ResourceDrawableResolver(icon)
    )

    constructor(@StringRes text: Int, iconUri: Uri) : this(
        ResourceStringResolver(text),
        null,
        iconUri
    )

    val text = MutableLiveData(text)
    val icon = MutableLiveData(icon)
    val iconUri = MutableLiveData(iconUri)
}
