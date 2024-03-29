package com.example.legocatalog

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
//        else Timber.plant(CrashReportingTree())
    }
}