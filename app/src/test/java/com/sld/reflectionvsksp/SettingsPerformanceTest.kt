package com.sld.reflectionvsksp

import com.sld.reflectionvsksp.data.local.model.SettingsData
import com.sld.reflectionvsksp.settings.ISettingsMapper
import com.sld.reflectionvsksp.settings.ISettingsModelFactory
import com.sld.reflectionvsksp.settings.reflection.*
import com.sld.reflectionvsksp.settings.ksp.*
import org.junit.Test
import kotlin.math.roundToLong

class SettingsPerformanceTest {

    private val reflectionFactory = ReflectionSettingsModelFactory(
        listOf(
            ReflectionSoundModel::class,
            ReflectionDisplayModel::class,
            ReflectionNetworkModel::class
        )
    )

    private val reflectionMapper = ReflectionSettingsMapper()
    private val kspFactory = KspSettingsModelFactory()
    private val kspMapper = KspSettingsMapper()

    private val warmupIterations = 10_000
    private val measurementIterations = 50_000
    private val measurementRounds = 7

    private val testData = listOf(
        SettingsData(name = "SOUND",
            values = mapOf(
                "enabled" to "true", "volume" to "70", "voiceGuidance" to "true",
                "outputMode" to "SYSTEM_DEFAULT", "muteNotifications" to "false"
            )
        ),
        SettingsData(name = "DISPLAY",
            values = mapOf(
                "darkMode" to "true", "brightness" to "70",
                "showTraffic" to "false", "theme" to "SYSTEM"
            )
        ),
        SettingsData(name = "NETWORK",
            values = mapOf(
                "wifiEnabled" to "true", "mobileDataEnabled" to "false", "connectionTimeout" to "30"
            )
        )
    )

    // Megakadályozza, hogy a mért eredmény teljesen
    // felhasználatlan maradjon.
    companion object {
        @Volatile
        private var blackhole: Any? = null

    }

    private enum class Operation {
        FACTORY, MAPPER, TOTAL

    }

    private data class BenchmarkCase(
        val name: String, val factory: ISettingsModelFactory, val mapper: ISettingsMapper
    )

    private val implementations = listOf(
        BenchmarkCase("Reflection", reflectionFactory, reflectionMapper),
        BenchmarkCase("KSP", kspFactory, kspMapper)
    )

    @Test
    fun comparePerformance() {

        println("\n===== REFLECTION VS KSP BENCHMARK V2 =====")

        for (data in testData) {

            println("\n========== ${data.name} ==========")

            for (operation in Operation.entries) {

                val results = mutableMapOf<String, MutableList<Double>>()

                // Minden implementációt előre bemelegítünk.
                implementations.forEach { implementation ->

                    val model = implementation.factory.createModel(data)
                    repeat(warmupIterations) {
                        val result = execute(operation, implementation, data, model)
                        blackhole = result
                    }

                }

                repeat(measurementRounds) { round ->

                    // A mérési sorrend váltakozik.
                    val orderedImplementations = if (round % 2 == 0) {
                        implementations
                    } else {
                        implementations.reversed()
                    }

                    orderedImplementations.forEach { implementation ->

                        val model = implementation.factory.createModel(data)
                        val elapsedNs = System.nanoTime()
                        var result: Any? = null

                        repeat(measurementIterations) {
                            result = execute(operation, implementation, data, model)
                        }

                        val totalNs = System.nanoTime() - elapsedNs

                        blackhole = result

                        val nsPerOperation = totalNs.toDouble() / measurementIterations

                        results.getOrPut(implementation.name) {
                            mutableListOf()
                        }.add(nsPerOperation)

                    }

                }

                val reflectionMedian = median(results.getValue("Reflection"))
                val kspMedian = median(results.getValue("KSP"))
                val speedup = reflectionMedian / kspMedian

                println(
                    "%-8s Reflection: %9.2f ns/op | KSP: %9.2f ns/op | Speedup: %6.2fx"
                        .format(
                            operation.name,
                            reflectionMedian,
                            kspMedian,
                            speedup
                        )
                )

            }

        }

    }

    private fun execute(
        operation: Operation, implementation: BenchmarkCase, data: SettingsData, model: Any
    ): Any {
        return when (operation) {
            Operation.FACTORY -> implementation.factory.createModel(data)
            Operation.MAPPER -> implementation.mapper.map(model)
            Operation.TOTAL -> implementation.mapper.map(implementation.factory.createModel(data))
        }
    }

    private fun median(values: List<Double>): Double {

        val sorted = values.sorted()
        val middle = sorted.size / 2

        return if (sorted.size % 2 == 0) {
            (sorted[middle - 1] + sorted[middle]) / 2.0
        } else {
            sorted[middle]
        }

    }

}