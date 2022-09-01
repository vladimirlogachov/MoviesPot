package com.vlohachov.moviespot.ui.credits.cast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.credit.CastMember
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.Profile
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun Cast(
    navigator: DestinationsNavigator,
    movieId: Long,
    viewModel: CastViewModel = getViewModel { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val unknownErrorText = stringResource(id = R.string.uknown_error)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = stringResource(id = R.string.cast)) },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
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
                viewState = uiState.viewState,
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
    viewState: ViewState<List<CastMember>>,
    onCredit: (creditId: Long) -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        when (viewState) {
            ViewState.Loading ->
                item(span = { GridItemSpan(currentLineSpan = 2) }) {
                    CircularProgressIndicator(modifier = Modifier)
                }
            is ViewState.Error ->
                viewState.error?.run(onError)
            is ViewState.Success ->
                items(items = viewState.data) { member ->
                    Profile(
                        modifier = Modifier
                            .width(width = 164.dp)
                            .aspectRatio(ratio = 0.75f),
                        title = member.name,
                        body = member.character,
                        painter = rememberAsyncImagePainter(model = member.profilePath),
                        onClick = { onCredit(member.id) },
                    )
                }
        }
    }
}