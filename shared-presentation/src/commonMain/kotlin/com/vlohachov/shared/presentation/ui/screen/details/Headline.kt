package com.vlohachov.shared.presentation.ui.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.vlohachov.shared.domain.model.Company
import com.vlohachov.shared.domain.model.Country
import com.vlohachov.shared.domain.model.Language
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.presentation.ui.component.Poster
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import com.vlohachov.shared.presentation.utils.DateUtils
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.directed_by
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Headline(
    director: String,
    details: MovieDetails,
    onPosterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) = HeadlineFrame(
    modifier = modifier,
    poster = { posterModifier ->
        var error by remember { mutableStateOf(false) }
        val painter = rememberAsyncImagePainter(
            model = details.posterPath,
            onError = { error = true },
        )

        Poster(
            modifier = posterModifier,
            painter = painter,
            onClick = { onPosterClick(details.posterPath) },
            error = error,
        )
    },
    title = { Text(text = details.title) },
    subtitle = {
        Text(
            text = buildString {
                if (details.releaseDate.isNotBlank()) {
                    append(DateUtils.format(date = details.releaseDate))
                    append(" ${Typography.bullet} ")
                }
                append(details.status)
            }
        )
    },
    info = {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = buildString {
                for ((index, genre) in details.genres.withIndex()) {
                    append(genre.name)
                    if (index < details.genres.size - 1) {
                        append(", ")
                    }
                }
            }
        )
        if (director.isNotBlank()) {
            Text(text = stringResource(resource = Res.string.directed_by, director))
        }
    },
)

@Composable
private fun HeadlineFrame(
    poster: @Composable RowScope.(modifier: Modifier) -> Unit,
    title: @Composable ColumnScope.() -> Unit,
    subtitle: @Composable ColumnScope.() -> Unit,
    info: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier.testTag(tag = HeadlineDefaults.TestTag),
    horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
) {
    poster(
        Modifier
            .weight(weight = 1f)
            .aspectRatio(ratio = .75f),
    )
    Column(modifier = Modifier.weight(weight = 2f)) {
        ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
            title()
        }
        subtitle()
        ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
            ) {
                info()
            }
        }
    }
}

@[Composable Preview(showBackground = true)]
internal fun HeadlinePreview() {
    MoviesPotTheme {
        Headline(
            modifier = Modifier.padding(all = 16.dp),
            director = "Director",
            details = MovieDetails(
                id = 0,
                title = "title",
                originalTitle = "original_title",
                tagline = "tagline",
                overview = "overview",
                posterPath = "poster",
                runtime = 96,
                budget = 12345678,
                releaseDate = "2022-08-18",
                status = "status",
                voteAverage = 6.7f,
                voteCount = 1234,
                genres = listOf(),
                isAdult = false,
                homepage = "homepage",
                originalLanguage = "language",
                spokenLanguages = listOf(
                    Language(
                        name = "name",
                        englishName = "english_name",
                        iso = "iso",
                    )
                ),
                productionCountries = listOf(
                    Country(
                        name = "name",
                        iso = "iso",
                    )
                ),
                productionCompanies = listOf(
                    Company(
                        id = 0,
                        name = "name",
                        logoPath = "path",
                        originCountry = "country",
                    )
                ),
            ),
            onPosterClick = {},
        )
    }
}

internal object HeadlineDefaults {

    const val TestTag = "HeadlineTestTag"

}
