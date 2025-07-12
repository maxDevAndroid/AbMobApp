package com.globo.admoblibrary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.globo.admoblib.model.AdEvent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

@Composable
fun RewardedAdInternal(
    adUnitId: String,
    onEvent: (AdEvent) -> Unit
) {
    val context = LocalContext.current
    var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }

    LaunchedEffect(adUnitId) {
        onEvent(AdEvent.Loading)
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    onEvent(AdEvent.Loaded)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    onEvent(AdEvent.Failed(error.message))
                }
            }
        )
    }
}
