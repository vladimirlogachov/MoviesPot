package com.vlohachov.moviespot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.vlohachov.moviespot.ui.screens.MainScreen
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesPotTheme {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}