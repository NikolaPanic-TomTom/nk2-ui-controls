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

package com.tomtom.nk2.api.common.menu

import com.tomtom.nk2.api.common.resourceresolution.drawable.ResourceDrawableResolver
import com.tomtom.nk2.api.common.resourceresolution.string.ResourceStringResolver
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class MenuItemTest : TomTomTestCase() {

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(MenuItem::class.java)
        EqualsVerifier.forClass(MenuItem.Id::class.java)
    }

    @Test
    fun `overloaded constructor`() {
        // GIVEN
        val menuItemWithRegularConstructor = MenuItem(
            MenuItem.Id(ID),
            ResourceDrawableResolver(ICON_RES),
            ResourceStringResolver(LABEL_RES)
        )
        val menuItemWithOverloadedConstructor = MenuItem(
            ID,
            ICON_RES,
            LABEL_RES
        )

        // THEN
        assertEquals(menuItemWithRegularConstructor, menuItemWithOverloadedConstructor)
        assertNotEquals(menuItemWithRegularConstructor, MenuItem("a", 0, 0))
    }

    companion object {
        const val ID = "abc"
        const val ICON_RES = 1
        const val LABEL_RES = 2
    }
}
