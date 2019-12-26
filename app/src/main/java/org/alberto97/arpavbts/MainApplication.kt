package org.alberto97.arpavbts

import android.app.Application
import org.alberto97.arpavbts.modules.appModule
import org.alberto97.arpavbts.modules.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()

            // Android context
            androidContext(this@MainApplication)

            // modules
            modules(networkModule + appModule)
        }
    }
}