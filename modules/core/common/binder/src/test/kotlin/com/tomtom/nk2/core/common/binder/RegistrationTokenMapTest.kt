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

package com.tomtom.nk2.core.common.binder

import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import org.junit.Assert
import org.junit.Test

class RegistrationTokenMapTest : TomTomTestCase() {

    @Test
    fun `new tokens`() {
        // GIVEN
        val sut = RegistrationTokenMap<String>()

        // WHEN
        val token1 = sut.getNewToken()
        val token2 = sut.getNewToken()

        // THEN
        assertNotEquals(token1, token2)
    }

    @Test
    fun `set and get token entries`() {
        // GIVEN
        val sut = RegistrationTokenMap<String>()
        val token1 = sut.getNewToken()
        val token2 = sut.getNewToken()

        // WHEN
        sut.setTokenEntry(token1, "Foo")
        sut.setTokenEntry(token2, "Bar")

        val entry1 = sut.getTokenEntry(token1)
        val entry2 = sut.getTokenEntry(token2)

        // THEN
        assertEquals("Foo", entry1)
        assertEquals("Bar", entry2)
    }

    @Test
    fun `add new entries`() {
        // GIVEN
        val sut = RegistrationTokenMap<String>()

        // WHEN
        val token1 = sut.addNewEntry("Foo")
        val token2 = sut.addNewEntry("Bar")

        val entry1 = sut.getTokenEntry(token1)
        val entry2 = sut.getTokenEntry(token2)

        // THEN
        assertEquals("Foo", entry1)
        assertEquals("Bar", entry2)
    }

    @Test
    fun `remove entries`() {
        // GIVEN
        val sut = RegistrationTokenMap<String>()
        val token1 = sut.addNewEntry("Foo")
        val token2 = sut.addNewEntry("Bar")

        // WHEN
        sut.removeTokenEntry(token1)

        // THEN
        assertNull(sut.getTokenEntry(token1))
        assertEquals("Bar", sut.getTokenEntry(token2))

        // WHEN
        sut.removeTokenEntry(token2)

        // THEN
        assertNull(sut.getTokenEntry(token2))
    }

    @Test
    fun `apply on token entries`() {
        // GIVEN
        val sut = RegistrationTokenMap<String>()
        val token1 = sut.addNewEntry("Foo")
        val token2 = sut.addNewEntry("Bar")
        var value: String? = null

        // WHEN
        sut.applyOnTokenEntryAndRemove(token1) {
            value = it
        }

        // THEN
        assertEquals("Foo", value)

        // WHEN
        sut.applyOnTokenEntryAndRemove(token2) {
            value = it
        }

        // THEN
        assertEquals("Bar", value)
    }

    @Test
    fun `apply on removed token entry`() {
        // GIVEN
        val sut = RegistrationTokenMap<String>()
        val token1 = sut.addNewEntry("Foo")
        sut.removeTokenEntry(token1)
        var value: String? = null

        // WHEN
        sut.applyOnTokenEntryAndRemove(token1) {
            value = it
        }

        // THEN
        assertNull(value)
    }

    @Test
    fun `token rollover`() {
        // GIVEN
        val sut = RegistrationTokenMap<String>()
        sut.setLastTokenValue(Int.MAX_VALUE)

        // WHEN
        val lastToken = sut.getNewToken()

        // THEN
        assertEquals(Int.MIN_VALUE, lastToken.token)
    }
}

/**
 * Use reflection to access private parts to reduce test duration.
 */
private fun <T> RegistrationTokenMap<T>.setLastTokenValue(value: Int) {
    val lastTokenField = this::class.memberProperties.find { it.name == "lastToken" }
    Assert.assertNotNull(lastTokenField)
    lastTokenField!!.let {
        it.isAccessible = true
        val token = it.getter.call(this) as AtomicInteger
        token.set(value)
    }
}
