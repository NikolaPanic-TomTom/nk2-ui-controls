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
import com.tomtom.nk2.core.common.math.roundToNearestHalfDay
import com.tomtom.nk2.core.common.math.roundToNearestHour
import com.tomtom.nk2.core.common.math.roundToNearestMinute
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlinx.android.parcel.Parcelize

/**
 * A [StringResolver] that takes a duration and resolves it to a [String] which is the concatenation
 * of the number of minutes with the minute indicator.
 */
@Parcelize
data class LabeledDurationStringResolver(private val duration: Duration) : StringResolver {

    override fun get(context: Context): String =
        when {
            duration <= Duration.ofMinutes(1) ->
                toStringLessThan1MinuteThreshold(context)
            duration <= Duration.ofMinutes(59).plusSeconds(29) ->
                toStringLessThan1HourThreshold(context)
            duration <= Duration.ofHours(23).plusMinutes(59).plusSeconds(29) ->
                toStringLessThen1DayThreshold(context)
            duration <= Duration.ofHours(47).plusMinutes(29).plusSeconds(59) ->
                toStringLessThen2DaysThreshold(context)
            else -> toStringMoreThan2DaysThreshold(context)
        }

    private fun toStringLessThan1MinuteThreshold(context: Context): String {
        return context.getString(
            R.string.ttivi_time_labeled_duration_format,
            1,
            context.getString(R.string.ttivi_time_minute_indicator)
        )
    }

    private fun toStringLessThan1HourThreshold(context: Context): String {
        val minutes = duration.roundToNearestMinute()

        return context.getString(
            R.string.ttivi_time_labeled_duration_format,
            minutes,
            context.getString(R.string.ttivi_time_minute_indicator)
        )
    }

    private fun toStringLessThen1DayThreshold(context: Context): String {
        val formattedHours = duration.toHours()
        val formattedMinutes = duration.minusHours(formattedHours).roundToNearestMinute()

        if (TimeUnit.MINUTES.toHours(formattedMinutes) > 0) {
            return context.getString(
                R.string.ttivi_time_labeled_duration_format,
                formattedHours + 1,
                context.getString(R.string.ttivi_time_hour_indicator)
            )
        }

        return context.getString(
            R.string.ttivi_time_labeled_hour_minute_duration_format,
            formattedHours,
            context.getString(R.string.ttivi_time_hour_indicator),
            formattedMinutes,
            context.getString(R.string.ttivi_time_minute_indicator)
        )
    }

    private fun toStringLessThen2DaysThreshold(context: Context): String {
        val hours = duration.roundToNearestHour()
        return context.getString(
            R.string.ttivi_time_labeled_duration_format,
            hours,
            context.getString(R.string.ttivi_time_hour_indicator)
        )
    }

    private fun toStringMoreThan2DaysThreshold(context: Context): String {
        val numHalfDays = duration.roundToNearestHalfDay()
        val numFullDays = numHalfDays / 2

        if (numHalfDays % 2 == 1L) {
            return context.getString(
                R.string.ttivi_time_labeled_fractional_days_duration_format,
                numFullDays,
                context.getString(R.string.ttivi_time_half_day_indicator),
                context.getString(R.string.ttivi_time_day_indicator)
            )
        }

        return context.getString(
            R.string.ttivi_time_labeled_duration_format,
            numFullDays,
            context.getString(R.string.ttivi_time_day_indicator)
        )
    }
}
