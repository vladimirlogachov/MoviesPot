package com.vlohachov.moviespot

import android.app.Application
import com.vlohachov.moviespot.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MoviesPotApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MoviesPotApp)
            modules(appComponent)
        }
    }
}