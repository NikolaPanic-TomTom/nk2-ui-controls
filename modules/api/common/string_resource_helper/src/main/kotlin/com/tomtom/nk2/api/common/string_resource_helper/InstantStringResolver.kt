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
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlinx.android.parcel.Parcelize

/**
 * A [StringResolver] that takes an [Instant] date time value and resolves that to the [String]
 * representation using the specified [formatStyle] in the default time zone for the given
 * [Context].
 *
 * Note that an [Instant] is a lean representation of a unique point in time. It can be seen as a
 * basic UTC time value.
 */
@Parcelize
data class InstantStringResolver(
    private val instant: Instant,
    private val formatStyle: FormatStyle
) : StringResolver {

    override fun get(context: Context): String {
        val currentLocale: Locale = context.resources.configuration.locales[0]
        val formatter = DateTimeFormatter.ofLocalizedDateTime(formatStyle).withLocale(currentLocale)
        return formatter.format(ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()))
    }
}
