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
import com.tomtom.nk2.core.common.math.roundToNearestSecond
import java.time.Duration
import kotlinx.android.parcel.Parcelize

/**
 * A [StringResolver] that takes a duration and resolves that to a [String] representation that
 * shows the number of minutes and seconds, such as `m:ss`.
 *
 * A negative value of [duration] will result in a negative representation, such as `-m:ss`.
 */
@Parcelize
data class MinuteSecondStringResolver(private val duration: Duration) : StringResolver {

    override fun get(context: Context): String {
        val timeSign = when {
            duration.isNegative ->
                context.getString(R.string.ttivi_time_minute_second_negative_time)
            else -> ""
        }

        val absoluteDuration = duration.abs()
        val minutes = absoluteDuration.toMinutes()
        val seconds = absoluteDuration.minusMinutes(minutes).roundToNearestSecond()

        return context.getString(
            R.string.ttivi_time_minute_second_time_format,
            timeSign,
            minutes,
            seconds
        )
    }
}
