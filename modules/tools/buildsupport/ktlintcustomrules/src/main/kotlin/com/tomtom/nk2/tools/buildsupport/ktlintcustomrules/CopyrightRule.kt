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
import com.pinterest.ktlint.core.ast.ElementType.FILE
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

/**
 * Enforce the presence of the copyright statement in all Kotlin files (source and Gradle).
 *
 * To improve this check, it could be rewritten as a Lint check, to also cover XML and other types
 * of files.
 * Until then, the Android Studio default options should cover the creation of new files.
 */
class CopyrightRule : Rule("file-copyright") {

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType != FILE) {
            return
        }
        if (!node.text.startsWith(EXPECTED_COPYRIGHT_BLOCK)) {
            emit(0, LICENSE_ERROR_MESSAGE, false)
        } else if (node.textLength > EXPECTED_COPYRIGHT_BLOCK.length &&
            node.text[EXPECTED_COPYRIGHT_BLOCK.length] != '\n') {
            emit(EXPECTED_COPYRIGHT_BLOCK.length, SPACING_ERROR_MESSAGE, false)
        }
    }

    companion object {
        const val LICENSE_ERROR_MESSAGE = "File does not start with the required license block."
        const val SPACING_ERROR_MESSAGE = "There should be an empty line after the license block."

        val EXPECTED_COPYRIGHT_BLOCK = """
            |/*
            | * Copyright (c) 2020 - 2020 TomTom N.V. All rights reserved.
            | *
            | * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be
            | * used for internal evaluation purposes or commercial use strictly subject to separate
            | * licensee agreement between you and TomTom. If you are the licensee, you are only permitted
            | * to use this Software in accordance with the terms of your license agreement. If you are
            | * not the licensee then you are not authorised to use this software in any manner and should
            | * immediately return it to TomTom N.V.
            | */
            |""".trimMargin()
    }
}
