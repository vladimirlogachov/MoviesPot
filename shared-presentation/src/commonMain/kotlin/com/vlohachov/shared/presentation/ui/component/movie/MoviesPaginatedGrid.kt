package com.vlohachov.shared.presentation.ui.component.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import coil3.compose.rememberAsyncImagePainter
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.presentation.core.LazyPagingItems
import com.vlohachov.shared.presentation.ui.component.Poster
import com.vlohachov.shared.presentation.ui.component.movie.MoviesPaginatedGridDefaults.spannableProgress

@Composable
internal fun MoviesPaginatedGrid(
    columns: GridCells,
    movies: LazyPagingItems<Movie>,
    onError: (error: Throwable) -> Unit,
    modifier: Modifier = Modifier,
    onClick: ((movie: Movie) -> Unit)? = null,
    state: LazyGridState = rememberLazyGridState(),
    progress: (LazyGridScope.() -> Unit)? = {
        spannableProgress(span = { GridItemSpan(currentLineSpan = 3) })
    },
    contentPadding: PaddingValues = MoviesPaginatedGridDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = MoviesPaginatedGridDefaults.VerticalArrangement,
    horizontalArrangement: Arrangement.Horizontal = MoviesPaginatedGridDefaults.HorizontalArrangement,
) {
    val itemModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(ratio = .75f)

    LazyVerticalGrid(
        modifier = modifier.testTag(tag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag),
        state = state,
        columns = columns,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
    ) {
        if (movies.loadState.refresh is LoadState.Loading) {
            progress?.invoke(this)
        }

        items(count = movies.itemCount) { index ->
            movies[index]?.run {
                MovieItem(
                    modifier = itemModifier,
                    movie = this,
                    onClick = onClick,
                )
            }
        }

        if (movies.loadState.append is LoadState.Loading) {
            spannableProgress(span = null)
        }
    }

    LaunchedEffect(movies.loadState) {
        when {
            movies.loadState.refresh is LoadState.Error ->
                onError((movies.loadState.refresh as LoadState.Error).error)

            movies.loadState.append is LoadState.Error ->
                onError((movies.loadState.append as LoadState.Error).error)
        }
    }
}

@Composable
private fun MovieItem(
    modifier: Modifier,
    movie: Movie,
    onClick: ((movie: Movie) -> Unit)?,
) {
    var error by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(
        model = movie.posterPath,
        onError = { error = true },
    )

    if (onClick != null) {
        Poster(
            modifier = modifier,
            painter = painter,
            contentDescription = movie.title,
            onClick = { onClick(movie) },
            error = error,
        )
    } else {
        Poster(
            modifier = modifier,
            painter = painter,
            contentDescription = movie.title,
            error = error,
        )
    }
}

internal object MoviesPaginatedGridDefaults {

    const val MoviesPaginatedGridTestTag = "movies_paginated_grid"
    const val LoadingTestTag = "movies_paginated_grid_loading"

    private val ItemsSpace: Dp = 16.dp

    val ContentPadding: PaddingValues = PaddingValues(all = ItemsSpace)

    val VerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(space = ItemsSpace)

    val HorizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(space = ItemsSpace)

    fun LazyGridScope.spannableProgress(
        span: (LazyGridItemSpanScope.() -> GridItemSpan)? = null,
    ) = item(span = span) {
        Box(
            modifier = Modifier
                .testTag(tag = LoadingTestTag)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) { CircularProgressIndicator() }
    }

}
