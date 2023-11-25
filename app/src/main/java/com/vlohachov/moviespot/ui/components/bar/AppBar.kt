package com.vlohachov.moviespot.ui.components.bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.settings.SettingsDefaults
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
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
                modifier = Modifier.semantics {
                    testTag = SettingsDefaults.BackButtonTestTag
                },
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_back),
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeAppBar(
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
                modifier = Modifier.semantics {
                    testTag = SettingsDefaults.BackButtonTestTag
                },
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_back),
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
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
