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
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import java.time.Duration
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class LabeledDurationStringResolverTest : TomTomTestCase() {

    private fun testWith(expectedResult: String, duration: Duration) {
        val mockContext = niceMockk<Context> {
            every { getString(R.string.ttivi_time_minute_indicator) } returns "min"
            every { getString(R.string.ttivi_time_hour_indicator) } returns "hr"
            every { getString(R.string.ttivi_time_day_indicator) } returns "d"
            every { getString(R.string.ttivi_time_half_day_indicator) } returns "½"
            every { getString(R.string.ttivi_time_labeled_duration_format, *anyVararg()) } answers {
                val varargs = secondArg<Array<Any>>()
                String.format("%1\$d %2\$s", varargs[0], varargs[1])
            }
            every {
                getString(
                    R.string.ttivi_time_labeled_hour_minute_duration_format,
                    *anyVararg()
                )
            } answers {
                val varargs = secondArg<Array<Any>>()
                String.format(
                    "%1\$d %2\$s %3\$d %4\$s",
                    varargs[0],
                    varargs[1],
                    varargs[2],
                    varargs[3]
                )
            }
            every {
                getString(
                    R.string.ttivi_time_labeled_fractional_days_duration_format,
                    *anyVararg()
                )
            } answers {
                val varargs = secondArg<Array<Any>>()
                String.format("%1\$d%2\$s %3\$s", varargs[0], varargs[1], varargs[2])
            }
        }

        // GIVEN
        val tested = LabeledDurationStringResolver(duration)

        // WHEN and THEN
        assertEquals(expectedResult, tested.get(mockContext))
    }

    @Test
    fun `10 seconds test`() = testWith("1 min", Duration.ofSeconds(10))

    @Test
    fun `20 seconds test`() = testWith("1 min", Duration.ofSeconds(20))

    @Test
    fun `30 seconds test`() = testWith("1 min", Duration.ofSeconds(30))

    @Test
    fun `70 seconds test`() = testWith("1 min", Duration.ofSeconds(70))

    @Test
    fun `10 minutes test`() = testWith("10 min", Duration.ofMinutes(10))

    @Test
    fun `30 minutes test`() = testWith("30 min", Duration.ofMinutes(30))

    @Test
    fun `40 minutes test`() = testWith("40 min", Duration.ofMinutes(40))

    @Test
    fun `round to 1 hour test`() = testWith("1 hr", Duration.ofMinutes(59).plusSeconds(30))

    @Test
    fun `less than 1 day test`() =
        testWith("13 hr 32 min", Duration.ofHours(13).plusMinutes(32))

    @Test
    fun `round to 1 day test`() =
        testWith("24 hr", Duration.ofHours(23).plusMinutes(59).plusSeconds(30))

    @Test
    fun `less than 2 days test`() =
        testWith("31 hr", Duration.ofHours(30).plusMinutes(30))

    @Test
    fun `round to 2 days test`() = testWith("2 d", Duration.ofHours(47).plusMinutes(30))

    @Test
    fun `fractional day test`() = testWith("2½ d", Duration.ofHours(60))

    @Test
    fun `more than 2 days test`() = testWith("3 d", Duration.ofDays(3))

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(LabeledDurationStringResolver::class.java).verify()
    }
}
