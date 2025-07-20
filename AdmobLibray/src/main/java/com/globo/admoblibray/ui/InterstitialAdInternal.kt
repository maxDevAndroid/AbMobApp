package com.globo.admoblibray.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.globo.admoblibray.model.AdEvent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
internal fun InterstitialAdInternal(
    adUnitId: String,
    onEvent: (AdEvent) -> Unit
) {
    val context = LocalContext.current

    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }

    LaunchedEffect(adUnitId) {
        onEvent(AdEvent.Loading)
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    onEvent(AdEvent.Loaded)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    onEvent(AdEvent.Failed(error.message))
                }
            }
        )
    }
}
