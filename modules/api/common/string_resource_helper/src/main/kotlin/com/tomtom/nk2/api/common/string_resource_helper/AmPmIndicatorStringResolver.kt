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
import android.text.format.DateFormat
import java.time.Instant
import java.util.Calendar
import kotlinx.android.parcel.Parcelize

/**
 * A [StringResolver] that takes an [Instant] date time value and resolves that to the [String]
 * representation of an AM or PM indicator.
 *
 * The indicator is determined by the [Context]'s 24 hour format preference. When a 24 hour format
 * should be used, an empty string is returned.
 *
 * Note that an [Instant] is a lean representation of a unique point in time. It can be seen as a
 * basic UTC time value.
 */
@Parcelize
data class AmPmIndicatorStringResolver(private val instant: Instant) : StringResolver {
    override fun get(context: Context): String {
        if (DateFormat.is24HourFormat(context)) {
            return ""
        }

        val defaultTimeZoneCalendar = Calendar.getInstance()
        defaultTimeZoneCalendar.timeInMillis = instant.toEpochMilli()
        return context.getString(
            when (defaultTimeZoneCalendar[Calendar.AM_PM]) {
                Calendar.AM -> R.string.ttivi_time_am_indicator
                else -> R.string.ttivi_time_pm_indicator
            }
        )
    }
}
