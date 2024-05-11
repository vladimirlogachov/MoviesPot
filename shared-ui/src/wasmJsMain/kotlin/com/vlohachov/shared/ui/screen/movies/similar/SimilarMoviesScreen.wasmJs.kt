package com.vlohachov.shared.ui.screen.movies.similar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.vlohachov.shared.domain.model.movie.Movie
import org.koin.core.module.Module

@Composable
internal actual fun SimilarMovies(
    movieId: Long,
    movieTitle: String,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    TODO("Not yet implemented")
}

internal actual val similarMoviesModule: Module
    get() = TODO("Not yet implemented")
