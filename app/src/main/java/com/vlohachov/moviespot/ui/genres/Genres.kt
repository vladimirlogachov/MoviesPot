package com.vlohachov.moviespot.ui.genres

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel

@Composable
fun Genres(
    modifier: Modifier = Modifier,
    viewModel: GenresViewModel = getViewModel(),
) {
    Column(modifier = modifier) {
        ScrollableTabRow(selectedTabIndex = 0) {

        }
    }
}

