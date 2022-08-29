package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.vlohachov.domain.model.Movie

@Composable
fun MoviesPaginatedGrid(
    columns: GridCells,
    movies: LazyPagingItems<Movie>,
    swipeRefreshState: SwipeRefreshState,
    onRefresh: () -> Unit,
    onError: (error: Throwable) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = MoviesPaginatedGridDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = MoviesPaginatedGridDefaults.VerticalArrangement,
    horizontalArrangement: Arrangement.Horizontal = MoviesPaginatedGridDefaults.HorizontalArrangement,
) {
    val itemModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(ratio = .75f)
    SwipeRefresh(state = swipeRefreshState, onRefresh = onRefresh) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = columns,
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            horizontalArrangement = horizontalArrangement,
        ) {
            items(movies.itemCount) { index ->
                movies[index]?.let { movie ->
                    Movie(
                        modifier = itemModifier,
                        movie = movie,
                    )
                }
            }

            movies.apply {
                when {
                    loadState.append is LoadState.Loading ->
                        item { Progress(modifier = modifier) }
                    loadState.refresh is LoadState.Error ->
                        onError((loadState.refresh as LoadState.Error).error)
                    loadState.append is LoadState.Error ->
                        onError((loadState.append as LoadState.Error).error)
                }
            }
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
    private val ItemsSpace: Dp = 16.dp

    val ContentPadding: PaddingValues = PaddingValues(all = ItemsSpace)

    val VerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(
        space = ItemsSpace,
        alignment = Alignment.CenterVertically,
    )

    val HorizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        space = ItemsSpace,
        alignment = Alignment.CenterHorizontally,
    )
}