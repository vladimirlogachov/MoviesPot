package com.vlohachov.shared.presentation.ui.component.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.presentation.core.ViewState
import com.vlohachov.shared.presentation.ui.component.button.More
import com.vlohachov.shared.presentation.ui.component.section.Section
import com.vlohachov.shared.presentation.ui.component.section.SectionColors
import com.vlohachov.shared.presentation.ui.component.section.SectionDefaults
import com.vlohachov.shared.presentation.ui.component.section.SectionTextStyles
import com.vlohachov.shared.presentation.ui.component.section.SectionTitle
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import com.vlohachov.shared.presentation.utils.DummyMovies
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.no_results
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MoviesSection(
    title: String,
    viewState: ViewState<List<Movie>>,
    modifier: Modifier = Modifier,
    onMovieClick: ((movie: Movie) -> Unit)? = null,
    onMore: (() -> Unit)? = null,
    titlePadding: PaddingValues = MoviesSectionDefaults.TitlePadding,
    contentPadding: PaddingValues = MoviesSectionDefaults.ContentPadding,
    textStyles: SectionTextStyles = SectionDefaults.mediumTextStyles(),
    colors: SectionColors = SectionDefaults.sectionColors(),
) {
    val isNotEmpty = viewState is ViewState.Success && viewState.data.isNotEmpty()
    val moreButton: @Composable (() -> Unit)? =
        if (onMore != null && isNotEmpty) {
            @Composable {
                More(
                    modifier = Modifier.testTag(tag = MoviesSectionDefaults.MoreButtonTestTag),
                    onClick = onMore,
                )
            }
        } else {
            null
        }
    Section(
        modifier = modifier.testTag(tag = MoviesSectionDefaults.MoviesSectionTestTag),
        title = {
            SectionTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = titlePadding),
                text = title,
                trailing = moreButton,
                horizontalArrangement = Arrangement.SpaceBetween,
            )
        },
        textStyles = textStyles,
        colors = colors,
    ) {
        Movies(
            modifier = Modifier
                .testTag(tag = MoviesSectionDefaults.ContentTestTag)
                .fillMaxWidth(),
            viewState = viewState,
            onMovieClick = onMovieClick,
            contentPadding = contentPadding,
        )
    }
}

@Composable
private fun Movies(
    viewState: ViewState<List<Movie>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    onMovieClick: ((movie: Movie) -> Unit)? = null,
) {
    Box(modifier = modifier) {
        when (viewState) {
            ViewState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier
                        .testTag(tag = MoviesSectionDefaults.ProgressTestTag)
                        .padding(paddingValues = contentPadding)
                        .align(alignment = Alignment.Center)
                )

            is ViewState.Error ->
                viewState.error?.message?.run {
                    Text(
                        modifier = Modifier
                            .testTag(tag = MoviesSectionDefaults.ErrorTestTag)
                            .padding(paddingValues = contentPadding),
                        text = this,
                    )
                }

            is ViewState.Success ->
                if (viewState.data.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .testTag(tag = MoviesSectionDefaults.EmptyTestTag)
                            .padding(paddingValues = contentPadding)
                            .align(alignment = Alignment.Center),
                        text = stringResource(resource = Res.string.no_results),
                    )
                } else {
                    MoviesLazyRow(
                        modifier = Modifier
                            .height(height = 168.dp)
                            .fillMaxWidth(),
                        movies = viewState.data,
                        contentPadding = contentPadding,
                        onClick = onMovieClick,
                    )
                }
        }
    }
}

internal object MoviesSectionDefaults {

    const val MoviesSectionTestTag: String = "movies_section"
    const val MoreButtonTestTag: String = "movies_section_more_button"
    const val ContentTestTag: String = "movies_section_content"
    const val ProgressTestTag: String = "movies_section_progress"
    const val ErrorTestTag: String = "movies_section_error"
    const val EmptyTestTag: String = "movies_section_empty"

    val TitlePadding: PaddingValues = PaddingValues(start = 16.dp, end = 4.dp)
    val ContentPadding: PaddingValues = PaddingValues(all = 16.dp)

}

@Preview
@Composable
internal fun MoviesSectionPreview() {
    MoviesPotTheme {
        Column {
            MoviesSection(title = "Title", viewState = ViewState.Loading)
            MoviesSection(
                title = "Title",
                viewState = ViewState.Error(error = Throwable("Error text")),
            )
            MoviesSection(title = "Title", viewState = ViewState.Success(data = DummyMovies))
            MoviesSection(
                title = "Title",
                viewState = ViewState.Success(data = DummyMovies),
                onMore = {},
            )
        }
    }
}
