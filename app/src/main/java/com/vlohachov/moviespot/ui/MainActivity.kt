package com.vlohachov.moviespot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.vlohachov.data.local.LocalPreferences
import com.vlohachov.moviespot.ui.destinations.MainDestination
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    @OptIn(
        ExperimentalAnimationApi::class,
        ExperimentalMaterialNavigationApi::class,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferences = get<LocalPreferences>()
            val applyDynamicTheme by preferences.applyDynamicTheme
                .collectAsState(initial = preferences.isDynamicThemeAvailable)

            MoviesPotTheme(dynamicColor = applyDynamicTheme) {
                Surface {
//                    Playground()
                    DestinationsNavHost(
                        modifier = Modifier.fillMaxSize(),
                        navGraph = NavGraphs.root,
                        startRoute = MainDestination,
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
