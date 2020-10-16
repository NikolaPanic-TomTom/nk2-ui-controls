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

import com.android.ide.common.gradle.model.IdeAndroidProject
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Project
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import java.io.File
import org.intellij.lang.annotations.Language

@Suppress("UnstableApiUsage")
class IllegalDependencyDetectorTest : LintDetectorTest() {

    private val testClient =
        object : com.android.tools.lint.checks.infrastructure.TestLintClient() {
            override fun createProject(dir: File, referenceDir: File): Project {
                return spyk(super.createProject(dir, referenceDir)) {
                    every { gradleProjectModel } returns mockProject
                }
            }
        }

    private val mockProject = mockk<IdeAndroidProject>(relaxed = true) {
        every { name } answers { moduleName!! }
    }

    /** The module name to use for the test. Must be set before performing a Lint check. */
    private var moduleName: String? = null

    private fun assertNoLintViolation(moduleName: String, gradleFileContents: String) {
        this.moduleName = moduleName

        lint()
            .allowMissingSdk()
            .client(testClient)
            .files(kts(gradleFileContents).indented())
            .run()
            .expect("No warnings.")
    }

    private fun assertLintResultEquals(moduleName: String, expectedResult: String) {
        this.moduleName = moduleName

        lint()
            .allowMissingSdk()
            .client(testClient)
            .files(kts(FULL_GRADLE_FILE_CONTENTS).indented())
            .run()
            .expect(expectedResult.trimIndent())
    }

    fun testLegalApi() {
        assertNoLintViolation(
            ":api_common_test",
            """
            import com.tomtom.nk2.buildsrc.environment.Libraries

            dependencies {
                api(project(":api_common_util"))
                api(Libraries.Util.SOME_EXTERNAL_LIBRARY)
                
                implementation(project(":api_common_util"))
                implementation(project(":core_common_util"))
                implementation(project(":stock_common_util"))
                implementation(Libraries.Util.SOME_EXTERNAL_LIBRARY)
            }
            """
        )
    }

    fun testLegalCore() {
        assertNoLintViolation(
            ":core_common_test",
            """
            import com.tomtom.nk2.buildsrc.environment.Libraries

            dependencies {
                api(project(":api_common_util"))
                api(project(":core_common_util"))
                api(Libraries.Util.SOME_EXTERNAL_LIBRARY)
                
                implementation(project(":api_common_util"))
                implementation(project(":core_common_util"))
                implementation(Libraries.Util.SOME_EXTERNAL_LIBRARY)
            }
            """
        )
    }

    fun testLegalIntegration() {
        assertNoLintViolation(
            ":integration_common_test",
            """
            import com.tomtom.nk2.buildsrc.environment.Libraries

            dependencies {
                api(project(":api_common_util"))
                api(project(":core_common_util"))
                api(project(":stock_common_util"))
                api(project(":integration_common_util"))
                api(Libraries.Util.SOME_EXTERNAL_LIBRARY)
                
                implementation(project(":api_common_util"))
                implementation(project(":core_common_util"))
                implementation(project(":stock_common_util"))
                implementation(project(":integration_common_util"))
                implementation(Libraries.Util.SOME_EXTERNAL_LIBRARY)
            }
            """
        )
    }

    fun testLegalIntegrationTest() {
        assertNoLintViolation(
            ":integration_test_example",
            """
            import com.tomtom.nk2.buildsrc.environment.Libraries

            dependencies {
                api(project(":api_common_util"))
                api(project(":core_common_util"))
                api(project(":stock_common_util"))
                api(project(":integration_common_util"))
                api(project(":tools_common_util"))
                api(Libraries.Util.SOME_EXTERNAL_LIBRARY)
                
                implementation(project(":api_common_util"))
                implementation(project(":core_common_util"))
                implementation(project(":stock_common_util"))
                implementation(project(":integration_common_util"))
                implementation(project(":tools_common_util"))
                implementation(Libraries.Util.SOME_EXTERNAL_LIBRARY)
            }
            """
        )
    }

    fun testLegalStock() {
        assertNoLintViolation(
            ":stock_common_test",
            """
            import com.tomtom.nk2.buildsrc.environment.Libraries

            dependencies {
                api(project(":api_common_util"))
                api(project(":core_common_util"))
                api(Libraries.Util.SOME_EXTERNAL_LIBRARY)
                
                implementation(project(":api_common_util"))
                implementation(project(":core_common_util"))
                implementation(Libraries.Util.SOME_EXTERNAL_LIBRARY)
            }
            """
        )
    }

    fun testLegalTools() {
        assertNoLintViolation(
            ":tools_common_test",
            """
            import com.tomtom.nk2.buildsrc.environment.Libraries

            dependencies {
                api(project(":api_common_util"))
                api(project(":core_common_util"))
                api(project(":stock_common_util"))
                api(project(":tools_common_util"))
                api(Libraries.Util.SOME_EXTERNAL_LIBRARY)
                
                implementation(project(":api_common_util"))
                implementation(project(":core_common_util"))
                implementation(project(":stock_common_util"))
                implementation(project(":tools_common_util"))
                implementation(Libraries.Util.SOME_EXTERNAL_LIBRARY)
            }
            """
        )
    }

    fun testIllegalApi() {
        assertLintResultEquals(
            ":api_common_test",
            """
            build.gradle.kts:5: Error: :core_common_util is not allowed as api() dependency of :api_common_test. [IllegalDependency]
                api(project(":core_common_util"))
                ~~~
            build.gradle.kts:6: Error: :stock_common_util is not allowed as api() dependency of :api_common_test. [IllegalDependency]
                api(project(":stock_common_util"))
                ~~~
            build.gradle.kts:7: Error: :integration_common_util is not allowed as api() dependency of :api_common_test. [IllegalDependency]
                api(project(":integration_common_util"))
                ~~~
            build.gradle.kts:8: Error: :tools_common_util is not allowed as api() dependency of :api_common_test. [IllegalDependency]
                api(project(":tools_common_util"))
                ~~~
            build.gradle.kts:14: Error: :integration_common_util is not allowed as implementation() dependency of :api_common_test. [IllegalDependency]
                implementation(project(":integration_common_util"))
                ~~~~~~~~~~~~~~
            build.gradle.kts:15: Error: :tools_common_util is not allowed as implementation() dependency of :api_common_test. [IllegalDependency]
                implementation(project(":tools_common_util"))
                ~~~~~~~~~~~~~~
            6 errors, 0 warnings
            """
        )
    }

    fun testIllegalCore() {
        assertLintResultEquals(
            ":core_common_test",
            """
            build.gradle.kts:6: Error: :stock_common_util is not allowed as api() dependency of :core_common_test. [IllegalDependency]
                api(project(":stock_common_util"))
                ~~~
            build.gradle.kts:7: Error: :integration_common_util is not allowed as api() dependency of :core_common_test. [IllegalDependency]
                api(project(":integration_common_util"))
                ~~~
            build.gradle.kts:8: Error: :tools_common_util is not allowed as api() dependency of :core_common_test. [IllegalDependency]
                api(project(":tools_common_util"))
                ~~~
            build.gradle.kts:13: Error: :stock_common_util is not allowed as implementation() dependency of :core_common_test. [IllegalDependency]
                implementation(project(":stock_common_util"))
                ~~~~~~~~~~~~~~
            build.gradle.kts:14: Error: :integration_common_util is not allowed as implementation() dependency of :core_common_test. [IllegalDependency]
                implementation(project(":integration_common_util"))
                ~~~~~~~~~~~~~~
            build.gradle.kts:15: Error: :tools_common_util is not allowed as implementation() dependency of :core_common_test. [IllegalDependency]
                implementation(project(":tools_common_util"))
                ~~~~~~~~~~~~~~
            6 errors, 0 warnings
            """
        )
    }

    fun testIllegalIntegration() {
        assertLintResultEquals(
            ":integration_common_test",
            """
            build.gradle.kts:8: Error: :tools_common_util is not allowed as api() dependency of :integration_common_test. [IllegalDependency]
                api(project(":tools_common_util"))
                ~~~
            build.gradle.kts:15: Error: :tools_common_util is not allowed as implementation() dependency of :integration_common_test. [IllegalDependency]
                implementation(project(":tools_common_util"))
                ~~~~~~~~~~~~~~
            2 errors, 0 warnings
            """
        )
    }

    fun testIllegalStock() {
        assertLintResultEquals(
            ":stock_common_test",
            """
            build.gradle.kts:6: Error: :stock_common_util is not allowed as api() dependency of :stock_common_test. [IllegalDependency]
                api(project(":stock_common_util"))
                ~~~
            build.gradle.kts:7: Error: :integration_common_util is not allowed as api() dependency of :stock_common_test. [IllegalDependency]
                api(project(":integration_common_util"))
                ~~~
            build.gradle.kts:8: Error: :tools_common_util is not allowed as api() dependency of :stock_common_test. [IllegalDependency]
                api(project(":tools_common_util"))
                ~~~
            build.gradle.kts:13: Error: :stock_common_util is not allowed as implementation() dependency of :stock_common_test. [IllegalDependency]
                implementation(project(":stock_common_util"))
                ~~~~~~~~~~~~~~
            build.gradle.kts:14: Error: :integration_common_util is not allowed as implementation() dependency of :stock_common_test. [IllegalDependency]
                implementation(project(":integration_common_util"))
                ~~~~~~~~~~~~~~
            build.gradle.kts:15: Error: :tools_common_util is not allowed as implementation() dependency of :stock_common_test. [IllegalDependency]
                implementation(project(":tools_common_util"))
                ~~~~~~~~~~~~~~
            6 errors, 0 warnings
            """
        )
    }

    fun testIllegalTools() {
        assertLintResultEquals(
            ":tools_common_test",
            """
            build.gradle.kts:7: Error: :integration_common_util is not allowed as api() dependency of :tools_common_test. [IllegalDependency]
                api(project(":integration_common_util"))
                ~~~
            build.gradle.kts:14: Error: :integration_common_util is not allowed as implementation() dependency of :tools_common_test. [IllegalDependency]
                implementation(project(":integration_common_util"))
                ~~~~~~~~~~~~~~
            2 errors, 0 warnings
            """
        )
    }

    override fun getDetector() = IllegalDependencyDetector()

    override fun getIssues() = listOf(IllegalDependencyDetector.ISSUE)

    companion object {
        @Language("kotlin-script")
        const val FULL_GRADLE_FILE_CONTENTS =
            """
            import com.tomtom.nk2.buildsrc.environment.Libraries

            dependencies {
                api(project(":api_common_util"))
                api(project(":core_common_util"))
                api(project(":stock_common_util"))
                api(project(":integration_common_util"))
                api(project(":tools_common_util"))
                api(Libraries.Util.SOME_EXTERNAL_LIBRARY)
                
                implementation(project(":api_common_util"))
                implementation(project(":core_common_util"))
                implementation(project(":stock_common_util"))
                implementation(project(":integration_common_util"))
                implementation(project(":tools_common_util"))
                implementation(Libraries.Util.SOME_EXTERNAL_LIBRARY)
            }
            """
    }
}
