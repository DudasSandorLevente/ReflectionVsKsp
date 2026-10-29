package com.sld.settings.processor

import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsProcessorGradleIntegrationTest {

    private fun createTestProject(): java.io.File {

        val projectDir = java.nio.file.Files.createTempDirectory(
            "settings-ksp-integration-"
        ).toFile()

        val repositoryRoot = java.io.File(System.getProperty("user.dir"))
            .parentFile
            .canonicalFile

        projectDir.resolve("settings.gradle.kts").writeText(
            """
        pluginManagement {
            repositories {
                google()
                mavenCentral()
                gradlePluginPortal()
            }
        }

        dependencyResolutionManagement {
            repositories {
                google()
                mavenCentral()
            }
        }

        rootProject.name = "settings-ksp-test"

        includeBuild("${repositoryRoot.invariantSeparatorsPath}") {
            dependencySubstitution {
                substitute(module("com.sld:settings-annotations"))
                    .using(project(":settings-annotations"))

                substitute(module("com.sld:settings-processor"))
                    .using(project(":settings-processor"))
            }
        }
        """.trimIndent()
        )
        projectDir.resolve("build.gradle.kts").writeText(
            """
        plugins {
            kotlin("jvm") version "2.2.20"
            id("com.google.devtools.ksp") version "2.3.10"
        }

        repositories {
            mavenCentral()
        }

        kotlin {
            jvmToolchain(17)
        }

        dependencies {
            implementation("com.sld:settings-annotations:1.0")
            add("ksp", "com.sld:settings-processor:1.0")
        }
        """.trimIndent()
        )

        return projectDir

    }

    @Test
    fun temporaryGradleProjectShouldBuild() {

        val projectDir = createTestProject()
        val sourceDir = projectDir.resolve("src/main/kotlin/test")
        sourceDir.mkdirs()

        sourceDir.resolve("ValidSliderModel.kt").writeText(
            """
        package test

        import com.sld.settings.annotations.ksp.SettingsModel
        import com.sld.settings.annotations.ksp.KspSliderSetting

        @SettingsModel(name = "TEST")
        data class ValidSliderModel(
            @KspSliderSetting(
                title = "Timeout",
                order = 1,
                min = 5,
                max = 60
            )
            val timeout: Int
        )
        """.trimIndent()
        )

        val settingsDir = projectDir.resolve(
            "src/main/kotlin/com/sld/reflectionvsksp/settings"
        )
        settingsDir.mkdirs()

        settingsDir.resolve("SettingItem.kt").writeText(
            """
        package com.sld.reflectionvsksp.settings

        sealed interface SettingItem {
            val id: String
            val title: String
        }
        """.trimIndent()
            )

            settingsDir.resolve("SliderItem.kt").writeText(
                """
        package com.sld.reflectionvsksp.settings

        data class SliderItem(
            override val id: String,
            override val title: String,
            val value: Int,
            val min: Int,
            val max: Int
        ) : SettingItem
        """.trimIndent()
        )

        val result = GradleRunner.create().withProjectDir(projectDir).withArguments(
            "compileKotlin", "--stacktrace"
        ).build()

        assertTrue(result.output.contains("BUILD SUCCESSFUL"))

        val generatedDir = projectDir.resolve("build/generated/ksp/main/kotlin")
        val generatedMapper = generatedDir.resolve(
            "com/sld/reflectionvsksp/generated/GeneratedValidSliderModelMapper.kt"
        )
        val generatedRegistry = generatedDir.resolve(
            "com/sld/reflectionvsksp/vs/ksp/generated/GeneratedSettingsModelRegistry.kt"
        )

        assertTrue(
            "Generated mapper file was not created", generatedMapper.isFile
        )
        assertTrue(

            "Generated registry file was not created", generatedRegistry.isFile
        )

    }

    @Test
    fun invalidSliderRangeShouldFailGradleCompilation() {

        val projectDir = createTestProject()
        val sourceDir = projectDir.resolve("src/main/kotlin/test")
        sourceDir.mkdirs()

        // Change the valid source into an invalid slider declaration.
        sourceDir.resolve("ValidSliderModel.kt").writeText(
            """
        package test

        import com.sld.settings.annotations.ksp.SettingsModel
        import com.sld.settings.annotations.ksp.KspSliderSetting

        @SettingsModel(name = "TEST")
        data class ValidSliderModel(
            @KspSliderSetting(
                title = "Timeout",
                order = 1,
                min = 60,
                max = 5
            )
            val timeout: Int
        )
        """.trimIndent()
        )

        val failedResult = GradleRunner.create().withProjectDir(projectDir).withArguments(
            "compileKotlin", "--stacktrace"
        ).buildAndFail()

        assertTrue(failedResult.output.contains(
            "Invalid slider range for 'timeout': min (60) must not exceed max (5)."
        ))

    }

}