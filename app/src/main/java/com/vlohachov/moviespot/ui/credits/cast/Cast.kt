package com.vlohachov.moviespot.ui.credits.cast

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.credit.CastMember
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.ErrorBar
import com.vlohachov.moviespot.ui.components.Profile
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Destination
@Composable
fun Cast(
    navigator: DestinationsNavigator,
    movieId: Long,
    viewModel: CastViewModel = getViewModel { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > 3 } }
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
                    IconButton(onClick = navigator::navigateUp) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                FloatingActionButton(
                    modifier = Modifier.semantics {
                        testTag = CastDefaults.ScrollToTopTestTag
                    },
                    onClick = {
                        coroutineScope.launch {
                            gridState.scrollToItem(index = 0)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_arrow_upward_24),
                        contentDescription = stringResource(id = R.string.scroll_to_top),
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        testTag = CastDefaults.ContentErrorTestTag
                    }
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            Content(
                modifier = Modifier
                    .semantics {
                        testTag = CastDefaults.ContentTestTag
                    }
                    .fillMaxSize(),
                gridState = gridState,
                viewState = uiState.viewState,
                onCredit = { },
                onError = viewModel::onError,
            )

            uiState.error?.run {
                ErrorBar(
                    error = this,
                    snackbarHostState = snackbarHostState,
                    onDismissed = viewModel::onErrorConsumed,
                )
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    gridState: LazyGridState,
    viewState: ViewState<List<CastMember>>,
    onCredit: (creditId: Long) -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        state = gridState,
        columns = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        when (viewState) {
            ViewState.Loading -> item(span = { GridItemSpan(currentLineSpan = 2) }) {
                Box(
                    modifier = Modifier.semantics {
                        testTag = CastDefaults.ContentLoadingTestTag
                    },
                    contentAlignment = Alignment.TopCenter,
                ) {
                    CircularProgressIndicator()
                }
            }

            is ViewState.Error ->
                viewState.error?.run(onError)

            is ViewState.Success ->
                items(items = viewState.data) { member ->
                    var error by remember { mutableStateOf(false) }
                    val painter = rememberAsyncImagePainter(
                        model = member.profilePath,
                        onError = { error = true },
                    )
                    Profile(
                        modifier = Modifier
                            .width(width = 164.dp)
                            .aspectRatio(ratio = 0.75f),
                        title = member.name,
                        body = member.character,
                        painter = painter,
                        onClick = { onCredit(member.id) },
                        error = error,
                    )
                }
        }
    }
}

object CastDefaults {

    const val ContentTestTag = "content"
    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"
    const val ScrollToTopTestTag = "scroll_to_top"
}
