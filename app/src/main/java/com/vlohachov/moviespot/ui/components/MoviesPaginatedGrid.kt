package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.vlohachov.domain.model.Movie

@Composable
fun MoviesPaginatedGrid(
    columns: GridCells,
    movies: LazyPagingItems<Movie>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = MoviesPaginatedGridDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = MoviesPaginatedGridDefaults.VerticalArrangement,
    horizontalArrangement: Arrangement.Horizontal = MoviesPaginatedGridDefaults.HorizontalArrangement,
) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio = .75f),
                    movie = movie,
                )
            }
        }
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