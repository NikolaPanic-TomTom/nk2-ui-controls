// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.tomtom.nk2.buildsrc.environment.Libraries
import com.tomtom.nk2.buildsrc.environment.Versions
import com.tomtom.nk2.buildsrc.extensions.android
import com.tomtom.nk2.buildsrc.extensions.getGradleProperty
import com.tomtom.nk2.buildsrc.extensions.kotlinOptions
import com.tomtom.nk2.buildsrc.environment.BuildVersioning
allprojects {
    repositories {
        google()
        jcenter()
    }
}
// Set up the git version for Android and CI
val buildVersions = BuildVersioning(rootProject)
val versionCode: Int by extra(buildVersions.versionCode)
val versionName: String by extra(buildVersions.versionName)
plugins {
    `kotlin-dsl`
    `maven-publish`
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jlleitschuh.gradle.ktlint") apply true
}
buildscript {
    val kotlin_version by extra("1.3.72")
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}
subprojects {
    print(this.name)
    val isApplicationProject by extra(getGradleProperty("isApplicationProject", false))
    val isLibraryProject by extra(getGradleProperty("isLibraryProject", false))
    val isAndroidLibraryProject by extra(!isApplicationProject && !isLibraryProject)
    val customKtlintRulesIncluded by extra(getGradleProperty("includeKtlintCustomRules", true))
    print("PANIC IsApplicationProject: $isAndroidLibraryProject")
    print("PANIC isLibraryProject: $isLibraryProject")
    print("PANIC isAndroidLibraryProject: $isAndroidLibraryProject")
    print("PANIC customKtlintRulesIncluded: $customKtlintRulesIncluded")
    configurations.all {
        // javolution gets duplicated when adding jscience as dependency
        exclude(group = "org.javolution", module = "javolution")
    }

    when {
        isApplicationProject -> {
            apply(plugin = "com.android.application")
            apply(plugin = "kotlin-android")
            apply(plugin = "kotlin-android-extensions")
        }
        isAndroidLibraryProject -> {
            apply(plugin = "com.android.library")
            apply(plugin = "kotlin-android")
            apply(plugin = "kotlin-android-extensions")
        }
        isLibraryProject -> {
            apply(plugin = "java-library")
            apply(plugin = "kotlin")
        }
    }

    apply(from = rootProject.file("buildSrc/repositories.gradle.kts"))

    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    if (customKtlintRulesIncluded) {
        dependencies {
            ktlintRuleset(project(":tools_buildsupport_ktlintcustomrules"))
        }
    }

    dependencies {
        compileOnly(kotlin("stdlib-jdk8:${Versions.KOTLIN}"))
        testImplementation(kotlin("test:${Versions.KOTLIN}"))
    }

    if (isApplicationProject || isAndroidLibraryProject) {
        dependencies {
            implementation(Libraries.Android.ANNOTATION)
            implementation(Libraries.Android.KTX)

            // Extension function `lintChecks` is not resolved here, so let's add dependency
            // directly.
            add("lintChecks", project(":tools_buildsupport_lintcustomrules"))
        }

        android {
            compileSdkVersion(Versions.COMPILE_SDK)
            buildToolsVersion = Versions.BUILD_TOOLS

            defaultConfig {
                minSdkVersion(Versions.MIN_SDK)
                targetSdkVersion(Versions.TARGET_SDK)
                if (isApplicationProject) {
                    versionCode = rootProject.extra.get("versionCode") as Int
                    versionName = rootProject.extra.get("versionName") as String
                }
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = Versions.JAVA_COMPATIBILITY
                targetCompatibility = Versions.JAVA_COMPATIBILITY
            }

            kotlinOptions {
                jvmTarget = Versions.JVM
                allWarningsAsErrors = project.hasProperty("warningsAsErrors")
            }

            lintOptions {
                lintConfig = File("lint.xml")
                isAbortOnError = true
                isCheckAllWarnings = true
                isCheckDependencies = true
                isWarningsAsErrors = true
                xmlOutput = File(buildDir, "reports/lint/report.xml")
                htmlOutput = File(buildDir, "reports/lint/report.html")

                disable(
                    // These issue IDs can cause Lint to crash while performing an analysis, so
                    //  disable these checks for now.
                    "WebViewApiAvailability",
                    "InlinedApi",
                    "ObsoleteSdkInt",
                    "Override",
                    "NewApi",
                    "UnusedAttribute",
                    // New dependency version checking is done during development.
                    "NewerVersionAvailable",
                    // Use of synthetic accessors is accepted, assuming we'll be using multidexing.
                    "SyntheticAccessor",
                    // The product does require protected system permissions.
                    "ProtectedPermissions",
                    // Accessibility text is not useful in this product.
                    "ContentDescription",
                    // The product does not target Chrome OS. -->
                    "PermissionImpliesUnsupportedChromeOsHardware",
                    // We don't need to be indexable by Google Search.
                    "GoogleAppIndexingApiWarning",
                    // 'supportsRtl' is defined in the product manifest.
                    "RtlEnabled",
                    // Ignore duplicate strings, which are most likely due to using the same
                    // string in different contexts.
                    "DuplicateStrings",
                    // We should use 'dp' for TextViews, so this warning doesn't apply.
                    "SpUsage"
                )
            }

            packagingOptions {
                // For NavKit 2, pick the first binary found when there are multiple.
                pickFirst("lib/**/*.so")
                pickFirst("META-INF/io.netty.versions.properties")
                exclude("META-INF/INDEX.LIST")
                // Prevent a Gradle warning when compiling android tests: this file gets created both for
                // main and androidTest configurations.
                pickFirst("META-INF/*.kotlin_module")
            }

            val projectSourceSets by extra(mutableSetOf<String>())
            sourceSets {
                setOf(
                    "main",
                    "test",
                    "debug",
                    "androidTest"
                ).forEach { sourceSetName ->
                    "src/$sourceSetName/kotlin".let { path ->
                        sourceSets.getByName(sourceSetName) {
                            java.srcDirs(path)
                        }
                        File(projectDir, path).takeIf { it.exists() }?.let {
                            projectSourceSets += it.absolutePath
                        }
                    }
                }
            }

            testOptions {
                val FAILURES_SUBDIR = "failures"
                val TEST_RECORDINGS_SUBDIR = "test_recordings"
                val SCREENSHOT_NAME = "%1\$s_%2\$s.png"
                animationsDisabled = true
                unitTests.apply {
                    buildTypes.all {
                        buildConfigField("int", "TARGET_SDK", "${Versions.TARGET_SDK}")
                    }
                    isIncludeAndroidResources = true
                    all(KotlinClosure1<Test, Test>({
                        systemProperty("robolectric.logging.enabled", "true")
                        systemProperty("robolectric.logging", "stdout")
                        this
                    }, owner = this))
                }
                buildTypes.all {
                    buildConfigField(
                        "String", "FAILURES_SUBDIR",
                        "\"$FAILURES_SUBDIR\""
                    )
                    buildConfigField(
                        "String", "SCREENSHOT_NAME",
                        "\"$SCREENSHOT_NAME\""
                    )
                    buildConfigField(
                        "String", "TEST_RECORDINGS_SUBDIR",
                        "\"$TEST_RECORDINGS_SUBDIR\""
                    )
                }
            }
        }
    }
}
