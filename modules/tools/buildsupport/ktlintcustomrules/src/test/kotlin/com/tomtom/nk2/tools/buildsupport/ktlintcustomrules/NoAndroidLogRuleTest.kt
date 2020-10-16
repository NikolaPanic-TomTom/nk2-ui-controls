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

package com.tomtom.nk2.tools.buildsupport.ktlintcustomrules

import com.pinterest.ktlint.core.LintError
import com.pinterest.ktlint.test.lint
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NoAndroidLogRuleTest {

    private val errorMessage = "Android logs should not be used, use: " +
        "com.tomtom.kotlin.traceevents.Tracer."

    private fun assertRule(text: String) {
        assertThat(
            NoAndroidLogRule().lint(
                """
                class Test {
                  init {
                    $text
                  }
                }
                """.trimIndent()
            )
        ).containsExactly(
            LintError(
                2, 8, "no-android-log", errorMessage
            )
        )
    }

    @Test
    fun androidLogVerbose() {
        assertRule("""Log.v("TAG", "message")""")
    }

    @Test
    fun androidLogDebug() {
        assertRule("""Log.d("TAG", "message")""")
    }

    @Test
    fun androidLogInfo() {
        assertRule("""Log.i("TAG", "message")""")
    }

    @Test
    fun androidLogWarning() {
        assertRule("""Log.w("TAG", "message")""")
    }

    @Test
    fun androidLogError() {
        assertRule("""Log.e("TAG", "message")""")
    }

    @Test
    fun androidLogErrorWithSpaceAfterLogKeyword() {
        assertRule("""Log .e("TAG", "message")""")
    }

    @Test
    fun androidLogErrorWithSpaceBeforeLogLevel() {
        assertRule("""Log. e("TAG", "message")""")
    }

    @Test
    fun androidLogErrorWithSpaceBeforeParenthesis() {
        assertRule("""Log.e ("TAG", "message")""")
    }

    @Test
    fun androidLogFullPackageName() {
        assertRule("""android.util.Log.e("TAG", "message")""")
    }

    @Test
    fun logPrintln() {
        assertRule("""Log.println("TAG", "message")""")
    }

    @Test
    fun androidLogReturnLine() {
        assertThat(
            NoAndroidLogRule().lint(
                """
                class Test {
                  init {
                    Log
                    .e("TAG", "message")
                  }
                }
                """.trimIndent()
            )
        ).containsExactly(
            LintError(
                2, 8, "no-android-log", errorMessage
            )
        )
    }

    @Test
    fun noAndroidLog() {
        assertThat(
            NoAndroidLogRule().lint(
                """
                class Test {
                  init {
                  }
                }
                """.trimIndent()
            )
        ).isEmpty()
    }

    @Test
    fun differentLog() {
        assertThat(
            NoAndroidLogRule().lint(
                """
                class Test {
                  init {
                    DifferentLog.v()
                  }
                }
                """.trimIndent()
            )
        ).isEmpty()
    }
}
