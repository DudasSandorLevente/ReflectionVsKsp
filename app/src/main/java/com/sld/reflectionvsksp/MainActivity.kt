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
import com.sld.reflectionvsksp.generated.GeneratedKspSoundModelMapper
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

class MainActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReflectionVsKspTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingsScreen(
                        name = "Android",
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
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
}