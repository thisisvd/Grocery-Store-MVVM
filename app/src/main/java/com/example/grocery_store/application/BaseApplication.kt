package com.example.grocery_store.application

import android.app.Application
import com.example.grocery_store.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            if (true) {
                Timber.plant(Timber.DebugTree())
            }
        }
    }
}