package com.vlohachov.moviespot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.vlohachov.shared.ui.MoviesPotApp

class MainActivity : ComponentActivity() {

    @OptIn(
        ExperimentalAnimationApi::class,
        ExperimentalMaterialNavigationApi::class,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val preferences = get<LocalPreferences>()
//            val applyDynamicTheme by preferences.applyDynamicThemeFlow
//                .collectAsState(initial = false)
//
//            AppTheme(dynamicColor = applyDynamicTheme) {
//                Surface {
//                    DestinationsNavHost(
//                        modifier = Modifier.fillMaxSize(),
//                        navGraph = NavGraphs.root,
//                        startRoute = MainDestination,
//                        engine = rememberAnimatedNavHostEngine(
//                            navHostContentAlignment = Alignment.TopCenter,
//                            rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING,
//                        ),
//                    )
//                }
//            }
            MoviesPotApp()
        }
    }

}
