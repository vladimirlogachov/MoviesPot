package com.vlohachov.moviespot

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.vlohachov.shared.presentation.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MoviesPotApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        initKoin {
            androidContext(androidContext = this@MoviesPotApp)
            androidLogger(level = Level.INFO)
        }
    }

}
