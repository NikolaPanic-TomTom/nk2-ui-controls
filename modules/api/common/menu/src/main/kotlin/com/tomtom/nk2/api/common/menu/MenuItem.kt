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

package com.tomtom.nk2.api.common.menu

import android.graphics.drawable.StateListDrawable
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.resourceresolution.drawable.ResourceDrawableResolver
import com.tomtom.nk2.api.common.string_resource_helper.ResourceStringResolver
import com.tomtom.nk2.api.common.string_resource_helper.StringResolver
import kotlinx.android.parcel.Parcelize

/**
 * Represents a [MenuItem] with an icon and a text label.
 *
 * @param id A unique identifier for this menu item.
 * @param icon A [DrawableResolver] that returns this menu item's icon. The provided icon can make
 *  use of a [StateListDrawable] to support different states, such as `state_selected` for when the
 *  menu item is selected.
 * @param label A [StringResolver] that returns this menu item's label.
 */
@Parcelize
data class MenuItem(
    val id: Id,
    val icon: DrawableResolver,
    val label: StringResolver
) : Parcelable {

    constructor(
        idValue: String,
        @DrawableRes iconRes: Int,
        @StringRes labelResourceId: Int
    ) : this(
        Id(idValue),
        ResourceDrawableResolver(iconRes),
        ResourceStringResolver(labelResourceId)
    )

    /**
     * A unique identifier of a [MenuItem].
     */
    @Parcelize
    data class Id(val value: String) : Parcelable
}
