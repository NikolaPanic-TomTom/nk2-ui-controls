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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver

/**
 * Class to hold the data displayed on the rounded image view.
 * A source image can be directly set or through remote retrieval from an URL.
 * Layering of the elements top-to-bottom:
 * [textOnImage]
 * [foregroundImage]
 * [sourceImage] or [sourceImagePlaceholder] if [sourceImage] is not set
 * [backgroundImage]
 * */
open class TtiviCardViewRoundedImageViewModel(

    /**
     * [textOnImage] is displayed on top of [foregroundImage].
     */
    val textOnImage: LiveData<String> = MutableLiveData(),

    /**
     * [foregroundImage] is displayed on top of [sourceImage].
     */
    val foregroundImage: LiveData<DrawableResolver> = MutableLiveData(),

    /**
     * [sourceImagePlaceholder] is displayed whenever [sourceImage] is not set.
     */
    val sourceImagePlaceholder: LiveData<DrawableResolver> = MutableLiveData(),

    /**
     * [sourceImage] is displayed on top of [backgroundImage] as [android:src].
     */
    val sourceImage: LiveData<DrawableResolver> = MutableLiveData(),

    /**
     * [backgroundImage] is displayed as [android:background].
     */
    val backgroundImage: LiveData<DrawableResolver> = MutableLiveData(),

    /**
     * [Uri] where the image is to be fetched from. If image was successfully retrieved,
     * [sourceImage] will be overwritten by this image.
     */
    val remoteImageUri: LiveData<Uri> = MutableLiveData()
)
