package com.globo.admoblibray.di

import AdMobManagerImpl
import com.globo.admoblibray.ad.AdViewModel
import com.globo.admoblibray.ads.IAdMobManager
import com.globo.admoblibray.model.AdType
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val adMobModule = module {
    viewModel { (adType: AdType) -> AdViewModel(adType) }
    single<IAdMobManager> { AdMobManagerImpl() }
}