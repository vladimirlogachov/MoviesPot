package com.vlohachov.moviespot.ui.credits

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.MovieCredits
import com.vlohachov.domain.model.movie.credit.CastMember
import com.vlohachov.domain.model.movie.credit.CrewMember
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.Profile
import com.vlohachov.moviespot.ui.components.section.Section
import com.vlohachov.moviespot.ui.components.section.SectionDefaults
import com.vlohachov.moviespot.ui.components.section.SectionTitle
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MovieCredits(
    navigator: DestinationsNavigator,
    movieId: Long,
    viewModel: MovieCreditsViewModel = getViewModel { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val unknownErrorText = stringResource(id = R.string.uknown_error)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = stringResource(id = R.string.credits)) },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                viewState = uiState.creditsViewState,
                onCredit = { creditId ->

                },
                onError = viewModel::onError,
            )

            uiState.error?.run {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
                    viewModel.onErrorConsumed()
                }
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewState: ViewState<MovieCredits>,
    onCredit: (creditId: Long) -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    Box(modifier = modifier) {
        when (viewState) {
            ViewState.Loading ->
                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
            is ViewState.Error ->
                viewState.error?.run(onError)
            is ViewState.Success ->
                Credits(
                    modifier = Modifier
                        .fillMaxSize(),
                    credits = viewState.data,
                    onCredit = onCredit,
                )
        }
    }
}

@Composable
private fun Credits(
    modifier: Modifier,
    credits: MovieCredits,
    onCredit: (creditId: Long) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item {
            Section(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    SectionTitle(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.cast, credits.cast.size),
                    )
                },
                textStyles = SectionDefaults.smallTextStyles(),
            ) {
                Cast(
                    modifier = Modifier
                        .height(height = 384.dp)
                        .fillMaxWidth(),
                    cast = credits.cast,
                    onClick = onCredit,
                )
            }
        }
        item {
            Section(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    SectionTitle(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.crew, credits.crew.size),
                    )
                },
                textStyles = SectionDefaults.smallTextStyles(),
            ) {
                Crew(
                    modifier = Modifier.fillMaxWidth(),
                    crew = credits.crew,
                    onClick = onCredit,
                )
            }
        }
    }
}

@Composable
private fun Cast(
    modifier: Modifier,
    cast: List<CastMember>,
    onClick: (memberId: Long) -> Unit,
    columns: GridCells = GridCells.Fixed(count = 3),
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(space = 16.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(space = 16.dp),
) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = 16.dp,
        crossAxisSpacing = 16.dp,
    ) {
        for (member in cast) {
            Profile(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 0.75f),
                title = member.name,
                body = member.character,
                painter = rememberAsyncImagePainter(model = member.profilePath),
                onClick = { onClick(member.id) },
            )
        }
    }
//    LazyVerticalGrid(
//        modifier = modifier,
//        columns = columns,
//        contentPadding = contentPadding,
//        verticalArrangement = verticalArrangement,
//        horizontalArrangement = horizontalArrangement,
//    ) {
//        items(items = cast) { member ->
//
//        }
//    }
}

@Composable
private fun Crew(
    modifier: Modifier,
    crew: List<CrewMember>,
    onClick: (memberId: Long) -> Unit,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(space = 16.dp),
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
    ) {
        items(items = crew) { member ->
            Profile(
                modifier = Modifier
                    .height(height = 168.dp)
                    .aspectRatio(ratio = .75f, matchHeightConstraintsFirst = true),
                painter = rememberAsyncImagePainter(model = member.profilePath),
                title = member.name,
                body = member.job,
                onClick = { onClick(member.id) },
            )
        }
    }
}