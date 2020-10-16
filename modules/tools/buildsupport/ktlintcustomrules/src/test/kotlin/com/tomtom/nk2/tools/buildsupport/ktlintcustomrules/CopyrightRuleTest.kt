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

class CopyrightRuleTest {

    private fun getTestClass(copyrightBlock: String): String =
        """
        |$copyrightBlock
        |class Test {
        |  // Nothing here.
        |}
        |""".trimMargin()

    private fun assertFailure(copyrightBlock: String) {
        assertThat(
            CopyrightRule().lint(
                getTestClass(copyrightBlock)
            )
        ).containsExactly(
            LintError(
                1, 1, "file-copyright", CopyrightRule.LICENSE_ERROR_MESSAGE
            )
        )
    }

    @Test
    fun `correct copyright`() {
        assertThat(
            CopyrightRule().lint(getTestClass(CopyrightRule.EXPECTED_COPYRIGHT_BLOCK))
        ).isEmpty()
    }

    @Test
    fun `missing copyright block`() {
        assertFailure("")
    }

    @Test
    fun `wrong copyright statement`() {
        assertFailure(CopyrightRule.EXPECTED_COPYRIGHT_BLOCK.replace("TomTom", "Garmin"))
    }

    @Test
    fun `copyright not at start of file`() {
        assertFailure(
            """
            import com.someone.SomeClass

            ${CopyrightRule.EXPECTED_COPYRIGHT_BLOCK}
        """.trimIndent()
        )
    }

    @Test
    fun `no newline after copyright`() {
        assertThat(
            CopyrightRule().lint(
                getTestClass(CopyrightRule.EXPECTED_COPYRIGHT_BLOCK + "import com.test.Class")
            )
        ).containsExactly(
            LintError(
                CopyrightRule.EXPECTED_COPYRIGHT_BLOCK.lines().size,
                1,
                "file-copyright",
                CopyrightRule.SPACING_ERROR_MESSAGE
            )
        )
    }

    @Test
    fun `copyright in empty file`() {
        assertThat(
            CopyrightRule().lint(CopyrightRule.EXPECTED_COPYRIGHT_BLOCK)
        ).isEmpty()
    }
}
