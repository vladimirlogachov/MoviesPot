package com.vlohachov.moviespot.ui.components.movie

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.DummyMovies
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.button.More
import com.vlohachov.moviespot.ui.components.section.Section
import com.vlohachov.moviespot.ui.components.section.SectionColors
import com.vlohachov.moviespot.ui.components.section.SectionDefaults
import com.vlohachov.moviespot.ui.components.section.SectionTextStyles
import com.vlohachov.moviespot.ui.components.section.SectionTitle
import com.vlohachov.shared.theme.MoviesPotTheme

@Composable
fun MoviesSection(
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
                    modifier = Modifier.semantics {
                        testTag = MoviesSectionDefaults.MoreButtonTestTag
                    },
                    onClick = onMore,
                )
            }
        } else {
            null
        }
    Section(
        modifier = modifier
            .semantics {
                testTag = MoviesSectionDefaults.MoviesSectionTestTag
            },
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
                .semantics {
                    testTag = MoviesSectionDefaults.ContentTestTag
                }
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
                        .semantics {
                            testTag = MoviesSectionDefaults.ProgressTestTag
                        }
                        .padding(paddingValues = contentPadding)
                        .align(alignment = Alignment.Center)
                )

            is ViewState.Error ->
                viewState.error?.message?.run {
                    Text(
                        modifier = Modifier
                            .semantics {
                                testTag = MoviesSectionDefaults.ErrorTestTag
                            }
                            .padding(paddingValues = contentPadding),
                        text = this,
                    )
                }

            is ViewState.Success ->
                if (viewState.data.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .semantics {
                                testTag = MoviesSectionDefaults.EmptyTestTag
                            }
                            .padding(paddingValues = contentPadding)
                            .align(alignment = Alignment.Center),
                        text = stringResource(id = R.string.no_results),
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

object MoviesSectionDefaults {

    const val MoviesSectionTestTag = "movies_section"
    const val MoreButtonTestTag = "movies_section_more_button"
    const val ContentTestTag = "movies_section_content"
    const val ProgressTestTag = "movies_section_progress"
    const val ErrorTestTag = "movies_section_error"
    const val EmptyTestTag = "movies_section_empty"

    val TitlePadding = PaddingValues(start = 16.dp, end = 4.dp)
    val ContentPadding = PaddingValues(all = 16.dp)
}

@Preview(showBackground = true)
@Composable
fun MoviesSectionPreview() {
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
