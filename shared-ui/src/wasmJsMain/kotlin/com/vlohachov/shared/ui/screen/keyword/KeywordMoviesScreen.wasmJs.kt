package com.vlohachov.shared.ui.screen.keyword

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.vlohachov.shared.domain.model.movie.Movie
import org.koin.core.module.Module

@Composable
internal actual fun KeywordMoviesScreen(
    keyword: String,
    keywordId: Int,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    TODO("Not yet implemented")
}

internal actual val keywordMoviesModule: Module
    get() = TODO("Not yet implemented")
