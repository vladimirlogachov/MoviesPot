package com.vlohachov.moviespot.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.settings.Settings
import com.vlohachov.moviespot.BuildConfig
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.keyword.KeywordMoviesDefaults
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun Settings(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = getViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val unknownErrorText = stringResource(id = R.string.unknown_error_local)
    val viewState by viewModel.viewState.collectAsState(initial = ViewState.Loading)

    viewModel.error?.run {
        this.printStackTrace()
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
            viewModel.onErrorConsumed()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        testTag = KeywordMoviesDefaults.ContentErrorTestTag
                    }
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        }
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(all = 16.dp),
            viewState = viewState,
            onDynamicTheme = viewModel::updateDynamicTheme,
            onError = viewModel::onError,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewState: ViewState<Settings>,
    onDynamicTheme: (dynamicTheme: Boolean) -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (viewState) {
            ViewState.Loading ->
                CircularProgressIndicator()
            is ViewState.Error ->
                viewState.error?.run(onError)
            is ViewState.Success ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
                        Text(text = stringResource(id = R.string.dynamic_theme))
                    }
                    Switch(
                        checked = viewState.data.dynamicTheme,
                        enabled = viewState.data.supportsDynamicTheme,
                        onCheckedChange = onDynamicTheme,
                    )
                }
        }
        Spacer(modifier = Modifier.weight(weight = 1f))
        Text(text = stringResource(id = R.string.app_version, BuildConfig.VERSION_NAME))
    }
}
