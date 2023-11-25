package com.vlohachov.moviespot.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.settings.Settings
import com.vlohachov.moviespot.BuildConfig
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.bar.AppBar
import com.vlohachov.moviespot.ui.components.bar.ErrorBar
import com.vlohachov.moviespot.ui.components.bar.ErrorBarDefaults
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun Settings(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = getViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uriHandler = LocalUriHandler.current
    val viewState by viewModel.viewState.collectAsState(initial = ViewState.Loading)

    ErrorBar(
        error = viewModel.error,
        snackbarHostState = snackbarHostState,
        onDismissed = viewModel::onErrorConsumed,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.settings),
                onBackClick = navigator::navigateUp,
            )

        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        testTag = ErrorBarDefaults.ErrorTestTag
                    }
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        }
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .semantics {
                    testTag = SettingsDefaults.ContentTestTag
                }
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(all = 16.dp),
            viewState = viewState,
            onDynamicTheme = viewModel::applyDynamicTheme,
            onAuthorLink = { uri -> uriHandler.openUri(uri = uri) },
            onError = viewModel::onError,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewState: ViewState<Settings>,
    onDynamicTheme: (dynamicTheme: Boolean) -> Unit,
    onAuthorLink: (uri: String) -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (viewState) {
            ViewState.Loading -> CircularProgressIndicator(modifier = Modifier.semantics {
                testTag = SettingsDefaults.LoadingTestTag
            })

            is ViewState.Error -> viewState.error?.run(onError)
            is ViewState.Success -> Row(
                modifier = Modifier
                    .semantics {
                        testTag = SettingsDefaults.DynamicThemeTestTag
                    }
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
                    Text(text = stringResource(id = R.string.dynamic_theme))
                }
                Switch(
                    modifier = Modifier.semantics {
                        testTag = SettingsDefaults.DynamicThemeToggleTestTag
                    },
                    checked = viewState.data.dynamicTheme,
                    enabled = viewState.data.supportsDynamicTheme,
                    onCheckedChange = onDynamicTheme,
                )
            }
        }
        Spacer(modifier = Modifier.weight(weight = 1f))
        Author(
            modifier = Modifier.semantics {
                testTag = SettingsDefaults.AuthorTestTag
            },
            onClick = onAuthorLink,
        )
        Text(text = stringResource(id = R.string.app_version, BuildConfig.VERSION_NAME))
    }
}

@Composable
private fun Author(
    onClick: (uri: String) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = style.toSpanStyle().copy(color = LocalContentColor.current)) {
            append(stringResource(id = R.string.author))
        }
        pushStringAnnotation(
            tag = "URL", annotation = stringResource(id = R.string.author_link)
        )
        withStyle(
            style = style.toSpanStyle().copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
            )
        ) {
            append(stringResource(id = R.string.author_name))
        }
        pop()
    }

    ClickableText(
        modifier = modifier,
        text = annotatedText,
        onClick = { offset ->
            // We check if there is an *URL* annotation attached to the text
            // at the clicked position
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.run { onClick(item) }
        },
        style = style,
    )
}

object SettingsDefaults {

    const val BackButtonTestTag = "back_button"
    const val ContentTestTag = "settings_content"
    const val LoadingTestTag = "settings_loading"
    const val DynamicThemeTestTag = "settings_dynamic_theme"
    const val DynamicThemeToggleTestTag = "settings_dynamic_theme_toggle"
    const val AuthorTestTag = "app_author"
}
