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

/**
 * An composition of a number of [Releasable]s, that will all be released upon releasing the
 * [CompositeReleasable]. Added [Releasable]s will be released in reverse order to uphold symmetry.
 *
 * This class is not thread-safe.
 */
class CompositeReleasable : Releasable {
    private val releasables = mutableListOf<Releasable>()

    operator fun plusAssign(releasable: Releasable) {
        releasables += releasable
    }

    operator fun plusAssign(releasable: () -> Unit) {
        plusAssign(object : Releasable {
            override fun release() {
                releasable()
            }
        })
    }

    operator fun minusAssign(releasable: Releasable) {
        releasables -= releasable
    }

    fun isEmpty() = releasables.isEmpty()

    fun isNotEmpty() = releasables.isNotEmpty()

    override fun release() {
        releasables.run {
            reversed().forEach(Releasable::release)
            clear()
        }
    }
}
