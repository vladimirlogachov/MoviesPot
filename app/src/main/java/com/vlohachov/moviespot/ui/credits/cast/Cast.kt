package com.vlohachov.moviespot.ui.credits.cast

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.vlohachov.moviespot.ui.components.Profile
import com.vlohachov.moviespot.ui.components.bar.AppBar
import com.vlohachov.moviespot.ui.components.bar.ErrorBar
import com.vlohachov.moviespot.ui.components.button.ScrollToTop
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

private const val VISIBLE_ITEMS_THRESHOLD = 3

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
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > VISIBLE_ITEMS_THRESHOLD } }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.cast),
                scrollBehavior = scrollBehavior,
                onBackClick = navigator::navigateUp,
            )
        },
        floatingActionButton = {
            ScrollToTop(visible = showScrollToTop, gridState = gridState)
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
