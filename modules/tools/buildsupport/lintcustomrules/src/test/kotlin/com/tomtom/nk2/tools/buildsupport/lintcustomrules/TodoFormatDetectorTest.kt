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

package com.tomtom.nk2.tools.buildsupport.lintcustomrules

import TodoFormatDetector
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue

@Suppress("UnstableApiUsage")
class TodoFormatDetectorTest : LintDetectorTest() {
    fun testBasic() {
        lint()
            .allowMissingSdk()
            .files(
                kotlin(
                    """
                    package test.pkg
                    
                    /*
                     * TODO: File documentation.
                     */
                    
                    /**
                     * TODO: Class documentation.
                     */
                    class TestClass {
                        // TODO(IVI-123): This is a valid format in a single-line comment.
                        // TODO: Missing a ticket.
                        // TODO(IVI-123) Missing colon.
                        // Comment mentions TODO in the middle.
                        // todo(IVI-123): Lower case.
                        // ToDo(IVI-123): Wonky casing.
                        // TODO(username): A username is no substitute for a ticket.
                        // TODO(IVI-123): 
                        // Mastodon should not match.
                        // Tododo should not match.
                        /** TODO(IVI-123): This is a valid format in a single-line KDoc. */
                        /**
                         * Some documentation.
                         * TODO(IVI-123): This is a valid format in a multi-line KDoc. 
                         */
                        /** TODO: Issue in single-line KDoc. */
                        /**
                         * Some documentation.
                         * TODO: Issue in multi-line KDoc.
                         */
                        val ignoreTODOinNames = "Ignore TODO in string"
                        
                        init {
                            /**
                             * TODO: KDoc in function.
                             */
                        }
                    }
                    """
                ).indented(),
                xml(
                    "/res/values/test.xml",
                    """<?xml version="1.0" encoding="utf-8"?>
                    <resources>
                        <!-- TODO: In single-line XML comment. -->
                        <!-- 
                          ~ TODO: In multi-line XML comment. 
                          -->
                    </resources>
                    """
                )
            )
            .run()
            .expect(
                """
                src/test/pkg/TestClass.kt:4: Error: Missing JIRA ticket reference. [TodoFormat]
                 * TODO: File documentation.
                   ~~~~~
                src/test/pkg/TestClass.kt:8: Error: Missing JIRA ticket reference. [TodoFormat]
                 * TODO: Class documentation.
                   ~~~~~
                src/test/pkg/TestClass.kt:12: Error: Missing JIRA ticket reference. [TodoFormat]
                    // TODO: Missing a ticket.
                       ~~~~~
                src/test/pkg/TestClass.kt:13: Error: Missing colon. [TodoFormat]
                    // TODO(IVI-123) Missing colon.
                       ~~~~~~~~~~~~~
                src/test/pkg/TestClass.kt:14: Error: Missing JIRA ticket reference. [TodoFormat]
                    // Comment mentions TODO in the middle.
                                        ~~~~
                src/test/pkg/TestClass.kt:15: Error: Wrong casing. [TodoFormat]
                    // todo(IVI-123): Lower case.
                       ~~~~~~~~~~~~~~
                src/test/pkg/TestClass.kt:16: Error: Wrong casing. [TodoFormat]
                    // ToDo(IVI-123): Wonky casing.
                       ~~~~~~~~~~~~~~
                src/test/pkg/TestClass.kt:17: Error: Missing JIRA ticket reference. [TodoFormat]
                    // TODO(username): A username is no substitute for a ticket.
                       ~~~~
                src/test/pkg/TestClass.kt:18: Error: Missing description. [TodoFormat]
                    // TODO(IVI-123): 
                       ~~~~~~~~~~~~~~
                src/test/pkg/TestClass.kt:26: Error: Missing JIRA ticket reference. [TodoFormat]
                    /** TODO: Issue in single-line KDoc. */
                        ~~~~~
                src/test/pkg/TestClass.kt:29: Error: Missing JIRA ticket reference. [TodoFormat]
                     * TODO: Issue in multi-line KDoc.
                       ~~~~~
                src/test/pkg/TestClass.kt:35: Error: Missing JIRA ticket reference. [TodoFormat]
                         * TODO: KDoc in function.
                           ~~~~~
                res/values/test.xml:3: Error: Missing JIRA ticket reference. [TodoFormat]
                                        <!-- TODO: In single-line XML comment. -->
                                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                res/values/test.xml:4: Error: Missing JIRA ticket reference. [TodoFormat]
                                        <!-- 
                                        ^
                14 errors, 0 warnings
                """
            )
    }

    override fun getDetector(): Detector {
        return TodoFormatDetector()
    }

    override fun getIssues(): List<Issue> {
        return listOf(TodoFormatDetector.ISSUE)
    }
}
