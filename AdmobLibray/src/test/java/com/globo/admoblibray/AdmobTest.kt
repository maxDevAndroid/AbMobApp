package com.globo.admoblibray

import android.app.Activity
import app.cash.turbine.test
import com.globo.admoblibray.ad.AdViewModel
import com.globo.admoblibray.model.AdEvent
import com.globo.admoblibray.model.AdType
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardedAd
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        val viewModel = AdViewModel(AdType.Banner)
        viewModel.adState.test {
            assertEquals(AdEvent.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Failed when update is called`() = runTest {
        val viewModel = AdViewModel(AdType.Banner)
        viewModel.adState.test {
            skipItems(1) // skip initial Loading
            val error = AdEvent.Failed("error")
            viewModel.update(error)
            assertEquals(error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Loaded when update is called`() = runTest {
        val viewModel = AdViewModel(AdType.Banner)
        viewModel.adState.test {
            skipItems(1)
            viewModel.update(AdEvent.Loaded)
            assertEquals(AdEvent.Loaded, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Loaded for Banner and Custom`() = runTest {
        val context = mockk<android.content.Context>(relaxed = true)

        val bannerVM = AdViewModel(AdType.Banner)
        bannerVM.adState.test {
            skipItems(1)
            bannerVM.loadAd("banner_id", context)
            assertEquals(AdEvent.Loaded, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        val customVM = AdViewModel(AdType.Custom(mockk(relaxed = true)))
        customVM.adState.test {
            skipItems(1)
            customVM.loadAd("custom_id", context)
            assertEquals(AdEvent.Loaded, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should call showInterstitialAd if ad is ready`() = runTest {
        val activity = mockk<Activity>(relaxed = true)
        val interstitialAd = mockk<InterstitialAd>(relaxed = true)

        val viewModel = AdViewModel(AdType.Interstitial)
        viewModel.javaClass.getDeclaredField("interstitialAd").apply {
            isAccessible = true
            set(viewModel, interstitialAd)
        }

        viewModel.showAdIfNeeded(activity)
        verify(exactly = 1) { interstitialAd.show(activity) }
    }

    @Test
    fun `should call showRewardedAd if ad is ready`() = runTest {
        val activity = mockk<Activity>(relaxed = true)
        val rewardedAd = mockk<RewardedAd>(relaxed = true)

        val viewModel = AdViewModel(AdType.Rewarded)
        viewModel.javaClass.getDeclaredField("rewardedAd").apply {
            isAccessible = true
            set(viewModel, rewardedAd)
        }

        viewModel.showAdIfNeeded(activity)
        verify(exactly = 1) { rewardedAd.show(activity, any()) }
    }
}