package com.globo.admoblibray.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.globo.admoblibrary.ui.BannerAdInternal
import com.globo.admoblibray.ad.AdViewModel
import com.globo.admoblibray.model.AdEvent
import com.globo.admoblibray.model.AdType
import com.globo.admoblibray.util.getActivity
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun AdBanner(
    adUnitId: String,
    adType: AdType = AdType.Banner,
    modifier: Modifier = Modifier,
    onEvent: ((AdEvent) -> Unit)? = null,
    viewModel: AdViewModel = koinViewModel(parameters = { parametersOf(adType) })
) {
    val state by viewModel.adState.collectAsState()
    val context = LocalContext.current
    val activity = remember(context) { context.getActivity() }

    LaunchedEffect(Unit) {
        viewModel.loadAd(adUnitId, context)
    }

    LaunchedEffect(state) {
        if (state is AdEvent.Loaded && activity != null) {
            viewModel.showAdIfNeeded(activity)
        }
    }

    val heightModifier = when (adType) {
        is AdType.Banner -> Modifier.height(50.dp)
        is AdType.Custom -> {
            val px = adType.adSize.getHeightInPixels(context)
            Modifier.height((px / context.resources.displayMetrics.density).dp)
        }

        else -> Modifier.height(1.dp)
    }

    Box(
        modifier
            .fillMaxWidth()
            .then(heightModifier),
        contentAlignment = Alignment.Center
    ) {
        if (adType is AdType.Banner) {
            BannerAdInternal(adUnitId, onEvent = {
                viewModel.update(it)
                onEvent?.invoke(it)
            })
        }

        if (adType is AdType.Custom) {
            CustomAdInternal(adUnitId, adType.adSize, onEvent = {
                viewModel.update(it)
                onEvent?.invoke(it)
            })
        }

        if (adType is AdType.Interstitial) {
            InterstitialAdInternal(adUnitId,onEvent = {
                viewModel.update(it)
                onEvent?.invoke(it)
            })
        }

        if (adType is AdType.Rewarded) {
            RewardedAdInternal(adUnitId, onEvent = {
                viewModel.update(it)
                onEvent?.invoke(it)
            })
        }

        when (state) {
            is AdEvent.Loading -> CircularProgressIndicator(
                modifier = Modifier
                    .height(24.dp)
                    .testTag("ad_loading_indicator")
            )

            is AdEvent.Failed -> Text("Erro ao carregar anúncio", color = Color.Red)
            else -> Unit
        }
    }
}
