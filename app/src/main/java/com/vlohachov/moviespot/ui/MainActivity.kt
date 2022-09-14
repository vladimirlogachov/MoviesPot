package com.vlohachov.moviespot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.vlohachov.moviespot.ui.destinations.MainScreenDestination
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesPotTheme {
                Surface {
                    DestinationsNavHost(
                        modifier = Modifier.fillMaxSize(),
                        navGraph = NavGraphs.root,
                        startRoute = MainScreenDestination,
                        engine = rememberAnimatedNavHostEngine(
                            navHostContentAlignment = Alignment.TopCenter,
                            rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING,
                        ),
                    )
                }
            }
        }
    }
}