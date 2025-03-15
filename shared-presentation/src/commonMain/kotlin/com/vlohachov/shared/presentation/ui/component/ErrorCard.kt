package com.vlohachov.shared.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.dismiss
import moviespot.shared_presentation.generated.resources.error_common_title
import moviespot.shared_presentation.generated.resources.unknown_error_local
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ErrorCard(
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    title: String = stringResource(resource = Res.string.error_common_title),
    message: String = stringResource(resource = Res.string.unknown_error_local),
    contentPadding: PaddingValues = PaddingValues(all = 12.dp),
): Unit = Card(
    modifier = modifier.testTag(tag = ErrorCardDefaults.ErrorTestTag),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
    )
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues = contentPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        onDismiss?.run {
            IconButton(
                modifier = Modifier
                    .testTag(tag = ErrorCardDefaults.DismissTestTag)
                    .size(size = 24.dp),
                onClick = this,
            ) {
                Icon(
                    modifier = Modifier.size(size = 18.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(resource = Res.string.dismiss),
                )
            }
        }
    }
}

internal object ErrorCardDefaults {

    const val ErrorTestTag: String = "error_card"
    const val DismissTestTag: String = "error_card_dismiss"

}
