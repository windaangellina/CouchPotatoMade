package com.winda.couchpotato

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import com.winda.couchpotato.core.di.CoreModule.databaseModule
import com.winda.couchpotato.core.di.CoreModule.networkModule
import com.winda.couchpotato.core.di.CoreModule.repositoryModule
import com.winda.couchpotato.di.AppModule.useCaseModule
import com.winda.couchpotato.di.AppModule.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MyApplication : Application() {
    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}