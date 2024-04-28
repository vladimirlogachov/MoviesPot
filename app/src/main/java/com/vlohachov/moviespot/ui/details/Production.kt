package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.components.Company
import com.vlohachov.moviespot.ui.components.section.Section
import com.vlohachov.moviespot.ui.components.section.SectionDefaults
import com.vlohachov.moviespot.ui.components.section.SectionTitle
import com.vlohachov.shared.domain.model.Company
import com.vlohachov.shared.ui.theme.MoviesPotTheme

@Composable
fun Production(
    companies: List<Company>,
    modifier: Modifier = Modifier,
) {
    Section(
        modifier = modifier.semantics { testTag = ProductionDefaults.TestTag },
        title = {
            SectionTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(id = R.string.production),
            )
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        textStyles = SectionDefaults.smallTextStyles(
            contentTextStyle = MaterialTheme.typography.bodyMedium,
        ),
    ) {
        if (companies.isEmpty()) {
            Text(
                modifier = Modifier.padding(all = 16.dp),
                text = stringResource(id = R.string.no_results),
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(all = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                items(items = companies) { company ->
                    var error by remember { mutableStateOf(false) }
                    val painter = rememberAsyncImagePainter(
                        model = company.logoPath,
                        onError = { error = true },
                    )

                    Company(
                        modifier = Modifier.width(width = 96.dp),
                        painter = painter,
                        name = company.name,
                        error = error,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductionPreview() {
    MoviesPotTheme {
        Production(
            companies = listOf(
                Company(
                    id = 1,
                    logoPath = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/5UQsZrfbfG2dYJbx8DxfoTr2Bvu.jpg",
                    name = "Marvel Studios",
                    originCountry = "US",
                ),
            ),
        )
    }
}

object ProductionDefaults {
    const val TestTag = "ProductionTestTag"
}
