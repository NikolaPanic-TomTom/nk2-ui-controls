package com.tomtom.nk2.tools.buildsupport.ktlintcustomrules

import com.pinterest.ktlint.core.LintError
import com.pinterest.ktlint.test.lint
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MaxLineLengthOnCommentsRuleTest {

    @Test
    fun testHyperLinksDifferentOrder() {
        assertThat(
            MaxLineLengthOnCommentsRule().lint(
                """
                fun main(vaaaaaaaaaaaaaaaaaaaaaaar: String) {
                    println("text")
                     /** [this is a very long comment which exceed the limit](http://but-contains-a-hyperlink-so-is-allowed) */
                     /** https://this-is-a-super-lonng-hyperlink-followed-by-a-comment [this is a very long comment]() */
                }
                """.trimIndent(),
                userData = mapOf("max_line_length" to "40")
            )
        ).isEmpty()
    }

    @Test
    fun testHyperLinks() {
        assertThat(
            MaxLineLengthOnCommentsRule().lint(
                """
                fun main(vaaaaaaaaaaaaaaaaaaaaaaar: String) {
                    println("text")
                     /** [this is a very long comment](http://but-contains-a-hyperlink-so-is-allowed) */
                     /** [this is a very long comment](https://but-contains-a-hyperlink-so-is-allowed) */
                }
                """.trimIndent(),
                userData = mapOf("max_line_length" to "40")
            )
        ).isEmpty()
    }

    @Test
    fun testLongComments() {
        assertThat(
            MaxLineLengthOnCommentsRule().lint(
                """ 
                // This is a very long comment which exceed the limit check
                fun main(vaaaaaaaaaaaaaaaaaaaaaaar: String) {
                // This is a very long comment which exceed the limit check
                    println("text")

                    println("text")
                    
                    /** 
                     This is also a very long comment which exceed the limit check
                     */
                     
                     /** And this is also a very long comment which exceed the limit check */
                     
                     /* And this is also a very long comment which exceed the limit check */
                }
                """.trimIndent(),
                userData = mapOf("max_line_length" to "40")
            )
        ).isEqualTo(
            listOf(
                LintError(1, 1, "max-line-length-on-comment", "Exceeded max line length (40)"),
                LintError(3, 1, "max-line-length-on-comment", "Exceeded max line length (40)"),
                LintError(9, 1, "max-line-length-on-comment", "Exceeded max line length (40)"),
                LintError(12, 1, "max-line-length-on-comment", "Exceeded max line length (40)"),
                LintError(14, 1, "max-line-length-on-comment", "Exceeded max line length (40)")
            )
        )
    }

    @Test
    fun testErrorSuppression() {
        assertThat(
            MaxLineLengthOnCommentsRule().lint(
                """ 
                // ktlint-disable max-line-length-on-comment This is a very long comment which exceed the limit check
                fun main(vaaaaaaaaaaaaaaaaaaaaaaar: String) { // ktlint-disable max-line-length-on-comment
                    println("text")

                    println("text")

                     /* ktlint-disable max-line-length-on-comment 
                      * But this one doesn't exceed the limit check. 
                      */
                      
                      /** ktlint-disable max-line-length-on-comment 
                      * But this one doesn't exceed the limit check. 
                      */
                }
                """.trimIndent(),
                userData = mapOf("max_line_length" to "40")
            )
        ).isEmpty()
    }
}
