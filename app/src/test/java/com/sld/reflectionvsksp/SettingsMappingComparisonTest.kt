package com.sld.reflectionvsksp

import com.sld.reflectionvsksp.data.local.model.SettingsData
import com.sld.reflectionvsksp.settings.reflection.ReflectionNetworkModel
import com.sld.reflectionvsksp.settings.reflection.ReflectionSettingsMapper
import com.sld.reflectionvsksp.settings.reflection.ReflectionSettingsModelFactory
import com.sld.reflectionvsksp.settings.ksp.KspSettingsMapper
import com.sld.reflectionvsksp.settings.ksp.KspSettingsModelFactory
import com.sld.reflectionvsksp.settings.SliderItem
import com.sld.reflectionvsksp.settings.SpinnerItem
import com.sld.reflectionvsksp.settings.SwitchItem
import com.sld.reflectionvsksp.settings.reflection.ReflectionDisplayModel
import com.sld.reflectionvsksp.settings.reflection.ReflectionSoundModel
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsMappingComparisonTest {

    private val reflectionFactory = ReflectionSettingsModelFactory(
        modelClasses = listOf(
            ReflectionSoundModel::class,
            ReflectionDisplayModel::class,
            ReflectionNetworkModel::class)
    )
    private val reflectionMapper = ReflectionSettingsMapper()
    private val kspFactory = KspSettingsModelFactory()
    private val kspMapper = KspSettingsMapper()

    @Test
    fun soundMappingShouldBeIdentical() {

        val data = SettingsData(name = "SOUND",
            values = mapOf(
                "enabled" to "true", "volume" to "70", "voiceGuidance" to "true",
                "outputMode" to "SYSTEM_DEFAULT", "muteNotifications" to "false"
            )
        )

        val reflectionItems = reflectionMapper.map(reflectionFactory.createModel(data))
        val kspItems = kspMapper.map(kspFactory.createModel(data))

        assertEquals(reflectionItems, kspItems)

        val expectedItems = listOf(
            SwitchItem("enabled", "Sound enabled", true),
            SliderItem("volume", "Volume", 70, 0, 100),
            SwitchItem("voiceGuidance", "Voice guidance", true),
            SpinnerItem(
                "outputMode", "Output mode", "SYSTEM_DEFAULT",
                reflectionItems.filterIsInstance<SpinnerItem>().first {
                    it.id == "outputMode"
                }.options
            ),
            SwitchItem("muteNotifications", "Mute notifications", false)
        )

        assertEquals(expectedItems, reflectionItems)

    }

    @Test
    fun displayMappingShouldBeIdentical() {

        val data = SettingsData(name = "DISPLAY",
            values = mapOf(
                "darkMode" to "true", "brightness" to "70",
                "showTraffic" to "false", "theme" to "SYSTEM"
            )
        )

        val reflectionItems = reflectionMapper.map(reflectionFactory.createModel(data))
        val kspItems = kspMapper.map(kspFactory.createModel(data))

        assertEquals(reflectionItems, kspItems)

        val expectedItems = listOf(
            SwitchItem("darkMode", "Dark mode", true),
            SliderItem("brightness", "Brightness", 70, 0, 100),
            SwitchItem("showTraffic", "Show traffic", false),
            SpinnerItem(
                "theme", "Display theme", "SYSTEM",
                reflectionItems.filterIsInstance<SpinnerItem>().first {
                    it.id == "theme"
                }.options
            )
        )

        assertEquals(expectedItems, reflectionItems)

    }

    @Test
    fun networkMappingShouldBeIdentical() {

        val data = SettingsData(name = "NETWORK",
            values = mapOf(
                "wifiEnabled" to "true",
                "mobileDataEnabled" to "false",
                "connectionTimeout" to "30"
            )
        )

        val reflectionModel = reflectionFactory.createModel(data)
        val reflectionItems = reflectionMapper.map(reflectionModel)

        val kspModel = kspFactory.createModel(data)
        val kspItems = kspMapper.map(kspModel)

        assertEquals(reflectionItems, kspItems)

        val expectedItems = listOf(
            SwitchItem(id = "wifiEnabled", title = "Wi-Fi enabled", value = true),
            SwitchItem(id = "mobileDataEnabled", title = "Mobile data enabled", value = false),
            SliderItem(
                id = "connectionTimeout", title = "Connection timeout",
                value = 30, min = 5, max = 60
            )
        )

        assertEquals(expectedItems, reflectionItems)
        assertEquals(expectedItems, kspItems)

    }



}