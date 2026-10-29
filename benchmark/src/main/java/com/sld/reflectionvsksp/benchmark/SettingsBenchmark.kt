package com.sld.reflectionvsksp.benchmark

import androidx.benchmark.BlackHole
import androidx.benchmark.ExperimentalBlackHoleApi
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sld.reflectionvsksp.data.local.model.SettingsData
import com.sld.reflectionvsksp.settings.reflection.ReflectionSettingsModelFactory
import com.sld.reflectionvsksp.settings.reflection.ReflectionSoundModel
import com.sld.reflectionvsksp.settings.ksp.KspSettingsModelFactory
import com.sld.reflectionvsksp.settings.reflection.ReflectionSettingsMapper
import com.sld.reflectionvsksp.settings.ksp.KspSettingsMapper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val soundSettings = SettingsData(
        name = "SOUND",
        values = mapOf(
            "enabled" to "true",
            "volume" to "70",
            "voiceGuidance" to "true",
            "outputMode" to "SYSTEM_DEFAULT",
            "muteNotifications" to "false"
        )
    )
    private val reflectionFactory = ReflectionSettingsModelFactory(
        modelClasses = listOf(ReflectionSoundModel::class)
    )

    private val kspFactory = KspSettingsModelFactory()
    private val reflectionMapper = ReflectionSettingsMapper()
    private val kspMapper = KspSettingsMapper()

    private val reflectionSoundModel = reflectionFactory.createModel(soundSettings)
    private val kspSoundModel = kspFactory.createModel(soundSettings)

    @Test
    fun baselineBenchmark() {
        benchmarkRule.measureRepeated {
            var result = 0
            repeat(100) { result += it }
            check(result == 4950)
        }
    }

    @Test
    @OptIn(ExperimentalBlackHoleApi::class)
    fun reflectionModelCreationBenchmark() {
        benchmarkRule.measureRepeated {
            val model = reflectionFactory.createModel(soundSettings)
            BlackHole.consume(model)
        }
    }

    @Test
    @OptIn(ExperimentalBlackHoleApi::class)
    fun kspModelCreationBenchmark() {
        benchmarkRule.measureRepeated {
            val model = kspFactory.createModel(soundSettings)
            BlackHole.consume(model)
        }
    }

    @Test
    @OptIn(ExperimentalBlackHoleApi::class)
    fun reflectionMappingBenchmark() {
        benchmarkRule.measureRepeated {
            val items = reflectionMapper.map(reflectionSoundModel)
            BlackHole.consume(items)
        }
    }

    @Test
    @OptIn(ExperimentalBlackHoleApi::class)
    fun kspMappingBenchmark() {
        benchmarkRule.measureRepeated {
            val items = kspMapper.map(kspSoundModel)
            BlackHole.consume(items)
        }
    }

    @Test
    @OptIn(ExperimentalBlackHoleApi::class)
    fun reflectionFullPipelineBenchmark() {
        benchmarkRule.measureRepeated {
            val model = reflectionFactory.createModel(soundSettings)
            val items = reflectionMapper.map(model)
            BlackHole.consume(items)
        }
    }

    @Test
    @OptIn(ExperimentalBlackHoleApi::class)
    fun kspFullPipelineBenchmark() {
        benchmarkRule.measureRepeated {
            val model = kspFactory.createModel(soundSettings)
            val items = kspMapper.map(model)
            BlackHole.consume(items)
        }
    }

}