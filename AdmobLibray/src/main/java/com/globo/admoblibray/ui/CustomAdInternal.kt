package com.globo.admoblibray.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.globo.admoblibray.model.AdEvent
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun CustomAdInternal(
    adUnitId: String,
    adSize: AdSize,
    onEvent: (AdEvent) -> Unit
) {
    val context = LocalContext.current
    val pxHeight = adSize.getHeightInPixels(context)
    val dpHeight = pxHeight / context.resources.displayMetrics.density

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(dpHeight.dp),
        factory = {
            AdView(it).apply {
                setAdSize(adSize)
                this.adUnitId = adUnitId
                onEvent(AdEvent.Loading)
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() = onEvent(AdEvent.Loaded)
                override fun onAdFailedToLoad(error: LoadAdError) =
                    onEvent(AdEvent.Failed(error.message))
            }
        }
    )
}
