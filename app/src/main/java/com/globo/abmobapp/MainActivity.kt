package com.globo.abmobapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.globo.admoblibray.ads.IAdMobManager
import com.globo.admoblibray.model.AdEvent
import com.globo.admoblibray.model.AdType
import org.koin.compose.getKoin

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
        val adError by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "👋 Bem-vindo ao teste de banner!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (adError != null) {
                Text(
                    text = "Erro ao carregar anúncio: $adError",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            val adManager: IAdMobManager = getKoin().get()

            adManager.init(
                adUnitId = BuildConfig.ADMOB_BANNER_ID,
                adType = AdType.Banner,
                modifier = Modifier,
                onEvent = { event ->
                    when (event) {
                        is AdEvent.Loading -> Log.d("AdBanner", "Carregando...")
                        is AdEvent.Loaded -> {
                            Log.d("AdBanner", "Anúncio carregado")
                            // setState(true)
                        }

                        is AdEvent.Failed -> {
                            Log.e("AdBanner", "Erro: ${event.message}")
                            // setError(event.message)
                        }

                        AdEvent.Closed -> {
                            Log.d("AdBanner", "Fechado")
                            // setState(false)
                        }

                        AdEvent.Opened -> {
                            Log.d("AdBanner", "Aberto")
                        }
                    }
                }
            )
        }
    }
}