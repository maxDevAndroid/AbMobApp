package com.globo.abmobapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.globo.admoblibray.model.AdEvent
import com.globo.admoblibray.model.AdType
import com.globo.admoblibray.ui.AdBanner

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
            ) {
                AbMobScreen()
            }
        }
    }

    @Composable
    fun AbMobScreen() {

        AdBanner(
            adUnitId = BuildConfig.ADMOB_BANNER_ID,
            adType = AdType.Banner,
            onEvent = { event ->
                when (event) {
                    is AdEvent.Loading -> Log.d("AdBanner", "Banner carregando")
                    is AdEvent.Loaded -> Log.d("AdBanner", "Banner carregado")
                    is AdEvent.Failed -> Log.e("AdBanner", "Banner erro: ${event.message}")
                    else -> {}
                }
            }
        )
    }
}