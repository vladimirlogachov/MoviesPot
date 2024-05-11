package com.vlohachov.shared.ui.component.bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.navigate_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun AppBar(
    modifier: Modifier,
    title: String,
    onBackClick: () -> Unit,
    showTitle: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        modifier = modifier.testTag(tag = AppBarDefaults.AppBarTestTag),
        title = { AppBarTitle(title = title, showTitle = showTitle) },
        navigationIcon = { AppBarBackButton(onBackClick = onBackClick) },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LargeAppBar(
    modifier: Modifier,
    title: String,
    onBackClick: () -> Unit,
    showTitle: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    LargeTopAppBar(
        modifier = modifier.testTag(tag = AppBarDefaults.AppBarTestTag),
        title = { AppBarTitle(title = title, showTitle = showTitle) },
        navigationIcon = { AppBarBackButton(onBackClick = onBackClick) },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun AppBarTitle(
    title: String,
    showTitle: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = showTitle,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Text(
            modifier = modifier.testTag(tag = AppBarDefaults.TitleTestTag),
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AppBarBackButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier.testTag(tag = AppBarDefaults.BackButtonTestTag),
        onClick = onBackClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(resource = Res.string.navigate_back),
        )
    }
}

public object AppBarDefaults {

    public const val AppBarTestTag: String = "app_bar"
    public const val TitleTestTag: String = "app_bar_title"
    public const val BackButtonTestTag: String = "back_button"

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun AppBarPreview() {
    MoviesPotTheme {
        AppBar(
            modifier = Modifier,
            title = "Title",
            showTitle = true,
            scrollBehavior = null,
            onBackClick = {},
        )
    }
}
