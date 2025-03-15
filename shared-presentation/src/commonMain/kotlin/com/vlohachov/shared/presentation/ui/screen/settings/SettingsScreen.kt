package com.vlohachov.shared.presentation.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vlohachov.shared.presentation.BuildConfig
import com.vlohachov.shared.presentation.ui.component.ErrorCard
import com.vlohachov.shared.presentation.ui.component.bar.AppBar
import com.vlohachov.shared.presentation.ui.screen.Screen
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.app_version
import moviespot.shared_presentation.generated.resources.author
import moviespot.shared_presentation.generated.resources.author_link
import moviespot.shared_presentation.generated.resources.author_name
import moviespot.shared_presentation.generated.resources.dynamic_theme
import moviespot.shared_presentation.generated.resources.settings
import moviespot.shared_presentation.generated.resources.unknown_error_local
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

internal data object SettingsScreen : Screen<Unit>() {

    override val path: String = "settings"

    override val arguments: List<NamedNavArgument> = emptyList()

    override fun route(params: Unit): String = path

    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path) {
            val viewModel = koinViewModel<SettingsViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            Settings(
                uiState = uiState,
                onBack = navController::navigateUp,
                onResetError = viewModel::resetError,
                onDynamicTheme = viewModel::applyDynamicTheme,
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
internal fun Settings(
    uiState: SettingsUiState,
    onBack: () -> Unit,
    onResetError: () -> Unit,
    onDynamicTheme: (dynamicTheme: Boolean) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                AppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(resource = Res.string.settings),
                    onBackClick = onBack,
                )
                AnimatedVisibility(visible = uiState.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .testTag(tag = SettingsDefaults.LoadingTestTag)
                            .fillMaxWidth()
                    )
                }
            }
        },
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .testTag(tag = SettingsDefaults.ContentTestTag)
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(all = 16.dp),
            uiState = uiState,
            onResetError = onResetError,
            onDynamicTheme = onDynamicTheme,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    uiState: SettingsUiState,
    onResetError: () -> Unit,
    onDynamicTheme: (dynamicTheme: Boolean) -> Unit,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    AnimatedVisibility(visible = uiState.error != null) {
        ErrorCard(
            modifier = Modifier.fillMaxWidth(),
            message = uiState.error?.message
                ?: stringResource(resource = Res.string.unknown_error_local),
            onDismiss = onResetError,
        )
    }
    Row(
        modifier = Modifier
            .testTag(tag = SettingsDefaults.DynamicThemeTestTag)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
            Text(text = stringResource(resource = Res.string.dynamic_theme))
        }
        Switch(
            modifier = Modifier.testTag(tag = SettingsDefaults.DynamicThemeToggleTestTag),
            checked = uiState.settings.dynamicTheme,
            enabled = uiState.isDynamicThemeAvailable,
            onCheckedChange = onDynamicTheme,
        )
    }
    Spacer(modifier = Modifier.weight(weight = 1f))
    Author(modifier = Modifier.testTag(tag = SettingsDefaults.AppAuthorTestTag))
    Text(
        modifier = Modifier.testTag(tag = SettingsDefaults.AppVersionTestTag),
        text = stringResource(resource = Res.string.app_version) + BuildConfig.VERSION_NAME
    )
}

@Composable
private fun Author(modifier: Modifier = Modifier) = Text(
    modifier = modifier,
    text = buildAnnotatedString {
        append(text = stringResource(resource = Res.string.author))
        withLink(
            link = LinkAnnotation.Url(
                url = stringResource(resource = Res.string.author_link),
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                    )
                ),
            )
        ) { append(text = stringResource(resource = Res.string.author_name)) }
    },
)

internal object SettingsDefaults {

    const val ContentTestTag = "settings_content"
    const val LoadingTestTag = "settings_loading"
    const val DynamicThemeTestTag = "settings_dynamic_theme"
    const val DynamicThemeToggleTestTag = "settings_dynamic_theme_toggle"
    const val AppAuthorTestTag = "app_author"
    const val AppVersionTestTag = "app_version"

}
