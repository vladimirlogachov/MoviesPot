package com.vlohachov.shared.ui.screen.keyword

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.vlohachov.shared.domain.model.movie.Movie

@Composable
internal actual fun KeywordMoviesScreen(
    keyword: String,
    keywordId: Int,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    viewModel: KeywordMoviesViewModel,
    snackbarHostState: SnackbarHostState,
) {
    TODO("Not yet implemented")
}
