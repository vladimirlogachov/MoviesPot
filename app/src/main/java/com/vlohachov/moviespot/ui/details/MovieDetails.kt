package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.SetSystemBarsColor
import com.vlohachov.moviespot.ui.theme.Typography
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MovieDetails(
    navigator: DestinationsNavigator,
    movieId: Long,
    movieTitle: String,
    viewModel: MovieDetailsViewModel = getViewModel { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = remember {
        TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    },
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors(),
) {
    val colorTransitionFraction = scrollBehavior.state.overlappedFraction
    val appBarContainerColor by topAppBarColors.containerColor(colorTransitionFraction)
    val unknownErrorText = stringResource(id = R.string.uknown_error)

    if (viewModel.viewState is ViewState.Error) {
        (viewModel.viewState as ViewState.Error).error?.run {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
                viewModel.onErrorConsumed()
            }
        }
    }

    SetSystemBarsColor(color = appBarContainerColor)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = movieTitle) },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            state = rememberSwipeRefreshState(isRefreshing = viewModel.viewState is ViewState.Loading),
            swipeEnabled = false,
            onRefresh = { },
        ) {
            if (viewModel.viewState is ViewState.Success) {
                Content(
                    modifier = Modifier.fillMaxSize(),
                    movieDetails = (viewModel.viewState as ViewState.Success<MovieDetails>).data,
                )
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    movieDetails: MovieDetails,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 16.dp),
    ) {
        item {
            Headline(
                modifier = Modifier.fillMaxWidth(),
                originalTitle = movieDetails.originalTitle,
                posterPath = movieDetails.posterPath,
            )
        }
    }
}

@Composable
private fun Headline(
    modifier: Modifier,
    originalTitle: String,
    posterPath: String,
    posterShape: Shape = RectangleShape,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Image(
            modifier = Modifier
                .weight(weight = 1f)
                .aspectRatio(ratio = 0.75f)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = posterShape,
                )
                .clip(shape = posterShape),
            painter = rememberAsyncImagePainter(model = posterPath),
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .weight(weight = 2f)
        ) {
            Text(
                text = originalTitle,
                style = Typography.titleMedium,
            )
        }
    }
}