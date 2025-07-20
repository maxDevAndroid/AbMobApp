package com.globo.admoblibray.util

import com.globo.admoblibray.model.AdEvent
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError

internal object AdListeners {
    fun bannerListener(onEvent: ((AdEvent) -> Unit)?): AdListener {
        return object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                onEvent?.invoke(AdEvent.Loaded)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                onEvent?.invoke(AdEvent.Failed(error.message))
            }

            override fun onAdOpened() {
                super.onAdOpened()
                onEvent?.invoke(AdEvent.Opened)
            }

            override fun onAdClosed() {
                super.onAdClosed()
                onEvent?.invoke(AdEvent.Closed)
            }
        }
    }
}
