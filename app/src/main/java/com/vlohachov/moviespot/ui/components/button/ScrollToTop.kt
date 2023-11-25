package com.vlohachov.moviespot.ui.components.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.vlohachov.moviespot.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScrollToTop(
    visible: Boolean,
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
    ) {
        FloatingActionButton(
            modifier = modifier.semantics {
                testTag = ScrollToTopDefaults.ScrollToTopTestTag
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
}

object ScrollToTopDefaults {
    const val ScrollToTopTestTag = "ScrollToTopTestTag"
}
