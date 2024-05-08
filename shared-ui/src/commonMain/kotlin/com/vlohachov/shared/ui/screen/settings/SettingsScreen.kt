package com.vlohachov.shared.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.ui.BuildConfig
import com.vlohachov.shared.ui.component.bar.AppBar
import com.vlohachov.shared.ui.component.bar.ErrorBar
import com.vlohachov.shared.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.ui.screen.Screen
import com.vlohachov.shared.ui.state.ViewState
import com.vlohachov.shared.ui.theme.isDynamicThemeAvailable
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.app_version
import moviespot.shared_ui.generated.resources.author
import moviespot.shared_ui.generated.resources.author_link
import moviespot.shared_ui.generated.resources.author_name
import moviespot.shared_ui.generated.resources.dynamic_theme
import moviespot.shared_ui.generated.resources.settings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

internal data object SettingsScreen : Screen<Unit>() {

    override val path: String = "settings"

    override val arguments: List<NamedNavArgument> = emptyList()

    override fun route(params: Unit): String = path

    override fun NavGraphBuilder.screen(navController: NavController) {
        composable(route = path) {
            Settings(onBack = navController::navigateUp)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Settings(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = koinInject(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uriHandler = LocalUriHandler.current
    val viewState by viewModel.viewState.collectAsState(initial = ViewState.Loading)

    ErrorBar(
        error = viewModel.error,
        snackbarHostState = snackbarHostState,
        onDismissed = viewModel::onErrorConsumed,
    )

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        AppBar(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(resource = Res.string.settings),
            onBackClick = onBack,
        )
    }, snackbarHost = {
        SnackbarHost(
            modifier = Modifier.testTag(tag = ErrorBarDefaults.ErrorTestTag)
                .navigationBarsPadding(),
            hostState = snackbarHostState,
        )
    }) { paddingValues ->
        Content(
            modifier = Modifier.testTag(tag = SettingsDefaults.ContentTestTag).fillMaxSize()
                .padding(paddingValues = paddingValues).padding(all = 16.dp),
            viewState = viewState,
            onDynamicTheme = viewModel::applyDynamicTheme,
            onAuthorLink = uriHandler::openUri,
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
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    when (viewState) {
        ViewState.Loading -> CircularProgressIndicator(
            modifier = Modifier.testTag(tag = SettingsDefaults.LoadingTestTag)
        )

        is ViewState.Error -> LaunchedEffect(key1 = viewState.error) {
            viewState.error?.run(onError)
        }

        is ViewState.Success -> Row(
            modifier = Modifier.testTag(tag = SettingsDefaults.DynamicThemeTestTag)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
                Text(text = stringResource(resource = Res.string.dynamic_theme))
            }
            Switch(
                modifier = Modifier.testTag(tag = SettingsDefaults.DynamicThemeToggleTestTag),
                checked = viewState.data.dynamicTheme,
                enabled = isDynamicThemeAvailable(),
                onCheckedChange = onDynamicTheme,
            )
        }
    }
    Spacer(modifier = Modifier.weight(weight = 1f))
    Author(
        modifier = Modifier.testTag(tag = SettingsDefaults.AuthorTestTag),
        onClick = onAuthorLink,
    )
    Text(text = stringResource(resource = Res.string.app_version) + " ${BuildConfig.VERSION_NAME}")
}

@Composable
private fun Author(
    onClick: (uri: String) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = style.toSpanStyle().copy(color = LocalContentColor.current)) {
            append(stringResource(resource = Res.string.author))
        }
        pushStringAnnotation(
            tag = "URL", annotation = stringResource(resource = Res.string.author_link)
        )
        withStyle(
            style = style.toSpanStyle().copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
            )
        ) {
            append(stringResource(resource = Res.string.author_name))
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

internal object SettingsDefaults {

    const val ContentTestTag = "settings_content"
    const val LoadingTestTag = "settings_loading"
    const val DynamicThemeTestTag = "settings_dynamic_theme"
    const val DynamicThemeToggleTestTag = "settings_dynamic_theme_toggle"
    const val AuthorTestTag = "app_author"

}
