package com.vlohachov.shared.presentation.ui.component.bar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.unknown_error_local
import org.jetbrains.compose.resources.stringResource

@Composable
@Deprecated(message = "Use ErrorCard instead")
public fun ErrorBar(
    error: Throwable?,
    snackbarHostState: SnackbarHostState,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onDismissed: (() -> Unit)? = null,
    onActionPerformed: (() -> Unit)? = null,
) {
    if (error == null) return

    val unknownErrorText = stringResource(resource = Res.string.unknown_error_local)

    LaunchedEffect(error) {
        when (
            snackbarHostState.showSnackbar(
                message = error.message ?: unknownErrorText,
                duration = duration,
            )
        ) {
            SnackbarResult.Dismissed -> onDismissed?.invoke()
            SnackbarResult.ActionPerformed -> onActionPerformed?.invoke()
        }
    }
}

internal object ErrorBarDefaults {

    const val ErrorTestTag: String = "error_bar"

}
