package com.globo.admoblibray.di

import android.content.Context
import com.google.android.gms.ads.MobileAds
import org.koin.core.module.Module

object AdMob {
    fun initKoinModules(): List<Module> = listOf(adMobModule)

    fun initializeSdk(context: Context) {
        MobileAds.initialize(context)
    }
}
