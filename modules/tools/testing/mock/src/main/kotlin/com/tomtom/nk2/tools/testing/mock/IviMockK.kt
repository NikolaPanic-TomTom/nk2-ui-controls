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

package com.tomtom.nk2.tools.testing.mock

import io.mockk.CapturingSlot
import io.mockk.MockK
import io.mockk.MockKMatcherScope
import io.mockk.MockKVerificationScope
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import kotlin.reflect.KClass

/**
 * A method for a MockK mock that is 'relaxed' by default, meaning not every method used in tests
 * has to be explicitly mocked.
 */
inline fun <reified T : Any> niceMockk(
    name: String? = null,
    relaxed: Boolean = true,
    vararg moreInterfaces: KClass<*>,
    block: T.() -> Unit = {}
): T = MockK.useImpl {
    mockk(name, relaxed, *moreInterfaces, block = block)
}

/**
 * A method for a MockK class mock that is 'relaxed' by default, meaning not every method used in
 * tests has to be explicitly mocked.
 */
fun <T : Any> niceMockkClass(
    type: KClass<T>,
    name: String? = null,
    relaxed: Boolean = true,
    vararg moreInterfaces: KClass<*>,
    block: T.() -> Unit = {}
): T = MockK.useImpl {
    mockkClass(type, name, relaxed, *moreInterfaces, block = block)
}

/**
 * Allows referencing properties that haven't been instantiated yet.
 * E.g.,
 * ```
 * private val mockA: A = mockk {
 *     every { mock.call(lateEq { mockB }) } return 1
 * }
 * private val mockB: B = mockk { }
 * ```
 */
inline fun <reified T : Any> MockKMatcherScope.lateEq(noinline matcher: () -> Any): T =
    matchNullable { matcher.invoke() == it }

/**
 * Instead of doing
 *
 * ```
 * val slot = slot<ArgumentClass>()
 * verify { mockedClass.someCall(capture(slot)) }
 * val capturedArgument = slot.captured
 * ```
 *
 * this utility class allows you to do
 *
 * ```
 * val capturedArgument = getCapturedArgument { mockedClass.someCall(captureSlot()) }
 * ```
 *
 * This is mainly needed because [verifySequence] doesn't allow arguments to be captured and used
 * within the same scope. Ideally, this class should not be needed at all. Hopefully this can be
 * removed once [MockK issue 137](https://github.com/mockk/mockk/issues/137) is resolved.
 */
object MockKCapturer {

    /**
     * A temporary value to store the slot so that it's available within the verifyBlock execution.
     * This is admittedly a dirty hack, but no cleaner alternative could be found because we need
     * the [MockKVerificationScope] available in the [getCapturedArgument] scope.
     */
    var pendingSlot: CapturingSlot<*>? = null

    /**
     * Retrieves an argument from a call on a mocked object. The captured argument should be marked
     * by [captureSlot]. E.g.:
     *
     * ```
     * val capturedArgument = getCapturedArgument { mockedClass.someCall(captureSlot()) }
     * ```
     */
    inline fun <reified T : Any> getCapturedArgument(
        noinline verifyBlock: MockKVerificationScope.() -> Unit
    ): T {
        val slot = slot<T>()
        pendingSlot = slot

        // Execute the verify block so that the captured argument is inserted into the slot
        verify(verifyBlock = verifyBlock)

        pendingSlot = null
        return slot.captured
    }

    /**
     * Indicates the place where the argument should be captured from by [getCapturedArgument].
     * E.g.:
     *
     * ```
     * val capturedArgument = getCapturedArgument { mockedClass.someCall(captureSlot()) }
     * ```
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> MockKVerificationScope.captureSlot(): T {
        if (pendingSlot == null) {
            throw UnsupportedOperationException("Only one captureSlot is currently supported.")
        }

        val capture = capture(pendingSlot as CapturingSlot<T>)
        pendingSlot = null

        return capture
    }
}
