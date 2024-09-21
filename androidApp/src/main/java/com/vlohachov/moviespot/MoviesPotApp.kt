package com.vlohachov.moviespot

import android.app.Application
import com.google.android.material.color.DynamicColors

class MoviesPotApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

}
