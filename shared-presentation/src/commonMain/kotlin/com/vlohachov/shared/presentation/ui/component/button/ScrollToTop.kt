package com.vlohachov.shared.presentation.ui.component.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.scroll_to_top
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ScrollToTop(
    visible: Boolean,
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
    ) {
        FloatingActionButton(
            modifier = modifier.testTag(tag = ScrollToTopDefaults.ScrollToTopTestTag),
            onClick = {
                coroutineScope.launch { gridState.scrollToItem(index = 0) }
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowUpward,
                contentDescription = stringResource(resource = Res.string.scroll_to_top),
            )
        }
    }
}

internal object ScrollToTopDefaults {

    const val ScrollToTopTestTag: String = "scroll_to_top"

}
