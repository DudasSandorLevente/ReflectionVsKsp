package com.sld.reflectionvsksp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.sld.reflectionvsksp.data.local.dao.SettingsDao
import com.sld.reflectionvsksp.data.local.entity.SettingValueEntity
import com.sld.reflectionvsksp.data.local.entity.SettingsEntity
import com.sld.reflectionvsksp.ui.SettingsViewModel
import com.sld.reflectionvsksp.ui.theme.ReflectionVsKspTheme
import com.sld.reflectionvsksp.ui.view.SettingsScreen
import com.sld.reflectionvsksp.vs.ISettingsMapper
import com.sld.reflectionvsksp.vs.enums.SoundOutputMode
import com.sld.reflectionvsksp.vs.ksp.KspSettingsMapper
import com.sld.reflectionvsksp.vs.ksp.KspSoundModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.sld.settings.generated.KspSoundModelInfo
import com.sld.settings.generated.KspDisplayModelInfo
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import com.sld.reflectionvsksp.data.SettingsLocalDataSource
import com.sld.reflectionvsksp.vs.ISettingsModelFactory
class MainActivity : ComponentActivity() {

    private val localDataSource: SettingsLocalDataSource by inject()
    private val settingsModelFactory: ISettingsModelFactory by inject()
    private val settingsDao: SettingsDao by inject()
    private val viewModel: SettingsViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ReflectionVsKspTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingsScreen(
                        name = "Android", viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // kspTest()
        lifecycleScope.launch {
            databaseTest()
            reflectionTest("SOUND")
            reflectionTest("DISPLAY")
        }

    }

    private suspend fun reflectionTest(category: String) {

        val values = localDataSource.getSettings(category) ?: error("$category settings not found")
        val model = when (category){
            "SOUND" -> settingsModelFactory.createSoundModel(values)
            "DISPLAY" -> settingsModelFactory.createDisplayModel(values)
            else -> error("Unknown settings category: $category")
        }
        Log.d("REFLECTION_TEST", "values = $values")
        Log.d("REFLECTION_TEST", "model = $model")
        Log.d("REFLECTION_TEST", "type = ${model::class.qualifiedName}")

    }

    private fun kspTest(){
        Log.d("KSP_TEST - Sound", KspSoundModelInfo.MODEL_NAME)
        Log.d("KSP_TEST - Display", KspDisplayModelInfo.MODEL_NAME)
        val mapper: ISettingsMapper = KspSettingsMapper()
        val items = mapper.map(
            KspSoundModel(
                enabled = true, volume = 75, voiceGuidance = true,
                outputMode = SoundOutputMode.SPEAKER, muteNotifications = false
            )
        )
        items.forEach { Log.d("KSP_TEST", it.toString()) }
    }

    private suspend fun databaseTest(){

        settingsDao.upsertSettings(SettingsEntity(name = "SOUND"))
        settingsDao.upsertSettings(SettingsEntity(name = "DISPLAY"))

        val soundSettings = settingsDao.findSettings("SOUND") ?: error("SOUND settings not found")
        val displaySettings = settingsDao.findSettings("DISPLAY") ?: error("DISPLAY settings not found")

        settingsDao.upsertValues(
            listOf(
                SettingValueEntity(settingsId = soundSettings.id, key = "enabled",           value = "true"),
                SettingValueEntity(settingsId = soundSettings.id, key = "volume",            value = "70"),
                SettingValueEntity(settingsId = soundSettings.id, key = "voiceGuidance",     value = "true"),
                SettingValueEntity(settingsId = soundSettings.id, key = "outputMode",        value = "SYSTEM_DEFAULT"),
                SettingValueEntity(settingsId = soundSettings.id, key = "muteNotifications", value = "false")
            )
        )
        settingsDao.upsertValues(
            listOf(
                SettingValueEntity(settingsId = displaySettings.id, key = "darkMode",    value = "true"),
                SettingValueEntity(settingsId = displaySettings.id, key = "brightness",  value = "70"),
                SettingValueEntity(settingsId = displaySettings.id, key = "showTraffic", value = "false"),
                SettingValueEntity(settingsId = displaySettings.id, key = "theme",       value = "SYSTEM")
            )
        )

        val soundResult = settingsDao.getSettings("SOUND")
        Log.d("ROOM_TEST", "SoundSettings: ${soundResult?.settings}")
        soundResult?.values?.forEach { Log.d("ROOM_TEST", "${it.key} = ${it.value}") }

        val displayResult = settingsDao.getSettings("DISPLAY")
        Log.d("ROOM_TEST", "DisplaySettings: ${displayResult?.settings}")
        displayResult?.values?.forEach { Log.d("ROOM_TEST", "${it.key} = ${it.value}") }

    }

}