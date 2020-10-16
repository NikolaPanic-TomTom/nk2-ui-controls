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

package com.tomtom.nk2.core.common.util

import com.tomtom.nk2.core.common.util.KClassExtensionsTest.NestedClass.NestedNestedClass
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import org.junit.Test

class KClassExtensionsTest : TomTomTestCase() {

    private class NestedClass {
        class NestedNestedClass
    }

    private inner class InnerNestedClass

    companion object

    @Test
    fun `package name from class`() {
        // GIVEN
        val classUnderTest = this

        // WHEN

        // THEN
        assertEquals("com.tomtom.nk2.core.common.util", classUnderTest::class.packageName)
    }

    @Test
    fun `enclosing class of nested class`() {
        assertEquals(KClassExtensionsTest::class, NestedClass::class.enclosingClass)
    }

    @Test
    fun `enclosing class of nested nested class`() {
        assertEquals(NestedClass::class, NestedNestedClass::class.enclosingClass)
    }

    @Test
    fun `enclosing class of nested inner class`() {
        assertEquals(KClassExtensionsTest::class, InnerNestedClass::class.enclosingClass)
    }

    @Test
    fun `enclosing class of companion object`() {
        assertEquals(KClassExtensionsTest::class, Companion::class.enclosingClass)
    }

    @Test
    fun `no enclosing class`() {
        assertNull(KClassExtensionsTest::class.enclosingClass)
    }
}
