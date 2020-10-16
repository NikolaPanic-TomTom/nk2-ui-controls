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

package com.tomtom.nk2.api.common.string_resource_helper

import android.content.Context
import android.os.Parcelable
import java.util.Locale

/**
 * An interface for classes that resolve a [String] based on a [Context]. This is useful to separate
 * business logic from presentation, where a service or ViewModel can refer to a resource or other
 * types of data that depend on the [Locale] or display without needing a [Context] to resolve its
 * value.
 */
interface StringResolver : Parcelable {

    /**
     * Resolves a [String] based on the given [Context]. The resulting value may differ per
     * [Context]. E.g. due to differing locales.
     *
     * @param context The [Context] to use for string retrieval.
     * @return The resolved string.
     */
    fun get(context: Context): String
}
