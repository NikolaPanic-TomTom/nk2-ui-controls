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

package com.tomtom.nk2.tools.testing.common

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * JUnit rule to be declared in a test whose some test methods need to be executed repeatedly.
 * The test will be executed until first failure.
 *
 * Usage:
 *
 * @get:Rule
 * val repeatRule = RepeatTestRule()
 *
 * @Test
 * @Repeat(10)
 * fun testRepeatTenTimes()
 * {}
 **/
class RepeatTestRule : TestRule {

    private class RepeatStatement(private val times: Int, private val statement: Statement) :
        Statement() {
        override fun evaluate() {
            repeat(times) {
                statement.evaluate()
            }
        }
    }

    override fun apply(statement: Statement, description: Description): Statement {
        val repeat: Repeat = description.getAnnotation(Repeat::class.java)
        return RepeatStatement(repeat.times, statement)
    }
}
