package com.globo.admoblibray.ad

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.globo.admoblibray.model.AdEvent
import com.globo.admoblibray.model.AdType
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdViewModel(private val adType: AdType) : ViewModel() {

    private val _adState = MutableStateFlow<AdEvent>(AdEvent.Loading)
    val adState: StateFlow<AdEvent> = _adState.asStateFlow()

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    fun update(event: AdEvent) {
        _adState.value = event
    }

    fun loadAd(adUnitId: String, context: Context) {
        when (adType) {
            is AdType.Banner,
            is AdType.Custom -> {
                update(AdEvent.Loaded)
            }

            is AdType.Interstitial -> loadInterstitialAd(adUnitId, context)
            is AdType.Rewarded -> loadRewardedAd(adUnitId, context)
        }
    }

    fun showAdIfNeeded(activity: Activity) {
        when (adType) {
            is AdType.Interstitial -> showInterstitialAd(activity)
            is AdType.Rewarded -> showRewardedAd(activity) {}
            else -> Unit
        }
    }

    private fun loadInterstitialAd(adUnitId: String, context: Context) {
        update(AdEvent.Loading)
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    update(AdEvent.Loaded)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    update(AdEvent.Failed(error.message))
                }
            }
        )
    }

    private fun showInterstitialAd(activity: Activity) {
        interstitialAd?.show(activity)
        interstitialAd = null
    }

    private fun loadRewardedAd(adUnitId: String, context: Context) {
        update(AdEvent.Loading)
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    update(AdEvent.Loaded)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    update(AdEvent.Failed(error.message))
                }
            }
        )
    }

    private fun showRewardedAd(
        activity: Activity,
        listener: OnUserEarnedRewardListener
    ) {
        rewardedAd?.show(activity, listener)
        rewardedAd = null
    }
}
