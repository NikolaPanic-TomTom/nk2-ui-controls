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

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType.BLOCK
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

// TODO(IVI-776): Investigate whether lint or ktlint should be used to create rules.
class NoAndroidLogRule : Rule("no-android-log") {

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType == BLOCK && hasAndroidLog(node.text)) {
            emit(node.startOffset, "Android logs should not be used, use: " +
                "com.tomtom.kotlin.traceevents.Tracer.", false)
        }
    }

    private fun hasAndroidLog(text: String): Boolean {
        return androidLogRegex.containsMatchIn(text)
    }

    companion object {
        private val androidLogRegex = """\bLog\s*[.]\s*[a-z]+\b""".toRegex()
    }
}
