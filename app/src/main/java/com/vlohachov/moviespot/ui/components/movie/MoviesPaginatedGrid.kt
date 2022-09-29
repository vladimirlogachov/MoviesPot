package com.vlohachov.moviespot.ui.components.movie

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.ui.components.Poster

@Composable
fun MoviesPaginatedGrid(
    columns: GridCells,
    movies: LazyPagingItems<Movie>,
    onError: (error: Throwable) -> Unit,
    modifier: Modifier = Modifier,
    onClick: ((movie: Movie) -> Unit)? = null,
    progress: (LazyGridScope.() -> Unit)? = null,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = MoviesPaginatedGridDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = MoviesPaginatedGridDefaults.VerticalArrangement,
    horizontalArrangement: Arrangement.Horizontal = MoviesPaginatedGridDefaults.HorizontalArrangement,
) {
    val itemModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(ratio = .75f)

    LazyVerticalGrid(
        modifier = modifier
            .semantics {
                testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag
            },
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
            movies[index]?.let { movie ->
                var error by remember { mutableStateOf(false) }
                val painter = rememberAsyncImagePainter(
                    model = movie.posterPath,
                    onError = { error = true },
                )

                if (onClick != null) {
                    Poster(
                        modifier = itemModifier,
                        painter = painter,
                        contentDescription = movie.title,
                        onClick = { onClick(movie) },
                        error = error,
                    )
                } else {
                    Poster(
                        modifier = itemModifier,
                        painter = painter,
                        contentDescription = movie.title,
                        error = error,
                    )
                }
            }
        }

        if (movies.loadState.append is LoadState.Loading) {
            item {
                Progress(
                    modifier = itemModifier
                        .semantics {
                            testTag = MoviesPaginatedGridDefaults.AppendProgressTestTag
                        }
                )
            }
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
private fun Progress(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

object MoviesPaginatedGridDefaults {

    const val MoviesPaginatedGridTestTag = "movies_paginated_grid"
    const val RefreshProgressTestTag = "movies_paginated_grid_refresh_progress"
    const val AppendProgressTestTag = "movies_paginated_grid_append_progress"

    private val ItemsSpace: Dp = 16.dp

    val ContentPadding: PaddingValues = PaddingValues(all = ItemsSpace)

    val VerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(space = ItemsSpace)

    val HorizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(space = ItemsSpace)
}
