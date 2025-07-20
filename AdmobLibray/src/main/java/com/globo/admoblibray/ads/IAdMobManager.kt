package com.globo.admoblibray.ads

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.globo.admoblibray.model.AdEvent
import com.globo.admoblibray.model.AdType

interface IAdMobManager {
    fun initialize(context: Context)
    @Composable
    fun init(
        adUnitId: String,
        adType: AdType,
        modifier: Modifier,
        onEvent: ((AdEvent) -> Unit)?
    )
}
