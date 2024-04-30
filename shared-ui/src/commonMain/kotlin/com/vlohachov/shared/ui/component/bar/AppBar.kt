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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.navigate_back
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
public fun AppBar(
    modifier: Modifier,
    title: String,
    showTitle: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .semantics { testTag = AppBarDefaults.BackButtonTestTag },
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(resource = Res.string.navigate_back),
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
public fun LargeAppBar(
    modifier: Modifier,
    title: String,
    showTitle: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onBackClick: () -> Unit,
) {
    LargeTopAppBar(
        modifier = modifier,
        title = {
            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .semantics { testTag = AppBarDefaults.BackButtonTestTag },
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(resource = Res.string.navigate_back),
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

internal object AppBarDefaults {

    const val BackButtonTestTag = "BackButtonTestTag"

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
