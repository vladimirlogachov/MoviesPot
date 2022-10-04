package com.vlohachov.moviespot.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.vlohachov.moviespot.R

@Composable
fun ErrorBar(
    error: Throwable,
    snackbarHostState: SnackbarHostState,
    onDismissed: (() -> Unit)? = null,
    onActionPerformed: (() -> Unit)? = null,
) {
    val unknownErrorText = stringResource(id = R.string.unknown_error_local)

    LaunchedEffect(snackbarHostState) {
        val result = snackbarHostState
            .showSnackbar(message = error.localizedMessage ?: unknownErrorText)

        when (result) {
            SnackbarResult.Dismissed -> onDismissed?.invoke()
            SnackbarResult.ActionPerformed -> onActionPerformed?.invoke()
        }
    }
}

object ErrorBarDefaults {
    const val ErrorTestTag = "error_bar"
}
