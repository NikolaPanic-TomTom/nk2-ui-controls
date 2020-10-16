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

import com.pinterest.ktlint.core.RuleSet
import com.pinterest.ktlint.core.RuleSetProvider
import com.pinterest.ktlint.ruleset.experimental.MultiLineIfElseRule

class CustomRuleSetProvider : RuleSetProvider {

    override fun get(): RuleSet = RuleSet(
        KTLINT_CUSTOM_RULE_ID,
        NoAndroidLogRule(),
        MaxLineLengthOnCommentsRule(),
        MultiLineIfElseRule(),
        CopyrightRule()
    )

    companion object {
        const val KTLINT_CUSTOM_RULE_ID = "ktlint-custom-rules"
    }
}
