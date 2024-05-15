package com.vlohachov.shared.ui.screen.discover.result

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.vlohachov.shared.domain.model.movie.Movie
import org.koin.core.module.Module

@Composable
internal actual fun DiscoverResult(
    year: Int?,
    genres: IntArray?,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    viewModel: DiscoverResultViewModel,
    snackbarHostState: SnackbarHostState,
) {
    TODO("Not yet implemented")
}
