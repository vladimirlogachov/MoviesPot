package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.utils.DecimalUtils
import com.vlohachov.moviespot.core.utils.TimeUtils
import com.vlohachov.moviespot.ui.components.section.Section
import com.vlohachov.moviespot.ui.components.section.SectionDefaults
import com.vlohachov.moviespot.ui.components.section.SectionTitle
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import com.vlohachov.moviespot.ui.theme.Typography

@Composable
fun ShortSummary(
    voteAverage: Float,
    voteCount: Int,
    isAdult: Boolean,
    runtime: Int,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    CompositionLocalProvider(LocalContentColor provides tint) {
        Row(
            modifier = modifier
                .then(other = Modifier.height(intrinsicSize = IntrinsicSize.Min)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            VoteAverage(
                modifier = Modifier.weight(weight = 1f),
                voteAverage = voteAverage,
                voteCount = voteCount,
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width = 1.dp)
                    .padding(vertical = 8.dp)
            )
            Audience(
                modifier = Modifier.weight(weight = 1f),
                isAdult = isAdult
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width = 1.dp)
                    .padding(vertical = 8.dp)
            )
            Duration(
                modifier = Modifier.weight(weight = 1f),
                runtime = runtime,
            )
        }
    }
}

@Composable
private fun VoteAverage(
    modifier: Modifier,
    voteAverage: Float,
    voteCount: Int,
) {
    Section(
        modifier = modifier,
        title = {
            SectionTitle(
                text = DecimalUtils.format(number = voteAverage),
                trailing = {
                    Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
                }
            )
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        textStyles = SectionDefaults.smallTextStyles(),
    ) {
        Text(text = stringResource(id = R.string.reviews, voteCount))
    }
}

@Composable
private fun Audience(
    modifier: Modifier,
    isAdult: Boolean,
) {
    Section(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .height(height = 32.dp)
                    .padding(vertical = 6.dp)
                    .border(
                        width = 1.dp,
                        color = LocalContentColor.current,
                        shape = ShapeDefaults.ExtraSmall,
                    )
                    .padding(horizontal = 4.dp),
                text = if (isAdult) "R" else "G"
            )
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        textStyles = SectionDefaults.smallTextStyles(
            titleTextStyle = Typography.titleSmall,
        ),
    ) {
        Text(text = stringResource(id = R.string.audience))
    }
}

@Composable
private fun Duration(
    modifier: Modifier,
    runtime: Int,
) {
    Section(
        modifier = modifier,
        title = {
            SectionTitle(
                text = stringResource(
                    id = R.string.format_duration,
                    TimeUtils.hours(runtime),
                    TimeUtils.minutes(runtime),
                ),
            )
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        textStyles = SectionDefaults.smallTextStyles(),
    ) {
        Text(text = stringResource(id = R.string.duration))
    }
}

@Preview(showBackground = true)
@Composable
fun BriefInfoPreview() {
    MoviesPotTheme {
        ShortSummary(
            modifier = Modifier
                .padding(all = 16.dp),
            voteAverage = 7.25f,
            voteCount = 567,
            isAdult = true,
            runtime = 127,
        )
    }
}
