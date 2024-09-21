package com.vlohachov.moviespot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vlohachov.shared.presentation.ui.MoviesPotApp
import org.koin.core.context.stopKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MoviesPotApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }

}
