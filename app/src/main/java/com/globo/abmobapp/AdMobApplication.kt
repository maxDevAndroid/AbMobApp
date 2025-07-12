package com.globo.abmobapp

import android.app.Application
import com.globo.admoblibray.di.adMobModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AdMobApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AdMobApplication)
            modules(adMobModule)
        }
    }
}