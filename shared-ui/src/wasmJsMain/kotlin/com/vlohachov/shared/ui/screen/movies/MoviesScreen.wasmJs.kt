package com.vlohachov.shared.ui.screen.movies

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCategory
import org.koin.core.module.Module

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal actual fun Movies(
    category: MovieCategory,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    gridState: LazyGridState,
    snackbarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TODO("Not yet implemented")
}

internal actual val moviesModule: Module
    get() = TODO("Not yet implemented")
