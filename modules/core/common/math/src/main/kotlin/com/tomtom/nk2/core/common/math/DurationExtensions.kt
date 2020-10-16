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

package com.tomtom.nk2.core.common.math

import java.time.Duration

fun Duration.roundToNearestSecond() =
    roundToNearest(this, Duration.ofSeconds(1)).seconds

fun Duration.roundToNearestMinute() =
    roundToNearest(this, Duration.ofMinutes(1)).toMinutes()

fun Duration.roundToNearestHour() =
    roundToNearest(this, Duration.ofHours(1)).toHours()

fun Duration.roundToNearestHalfDay() =
    roundToNearest(this, Duration.ofHours(12)).toHours() / 12
