package com.vlohachov.shared.ui.core.utils

import androidx.compose.runtime.Composable
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.decimal_format
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
internal object DecimalUtils {

    @Composable
    fun format(number: Float): String =
        stringResource(resource = Res.string.decimal_format, number)

}
