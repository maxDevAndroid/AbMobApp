package com.globo.admoblibray.model

sealed class AdType {
    data object Banner : AdType()
    data object Interstitial : AdType()
    data object Rewarded : AdType()
    data class Custom(val adSize: com.google.android.gms.ads.AdSize) : AdType()
}