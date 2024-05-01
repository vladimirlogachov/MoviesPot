package com.vlohachov.moviespot.ui.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.shared.ui.component.bar.AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun FullscreenImage(
    navigator: DestinationsNavigator,
    path: String,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                modifier = Modifier.fillMaxWidth(),
                title = "",
                onBackClick = navigator::navigateUp,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            var error by remember { mutableStateOf(false) }
            val painter = rememberAsyncImagePainter(
                model = path,
                onError = { error = true },
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painter,
                contentDescription = path,
            )

            if (error) {
                Icon(
                    modifier = Modifier
                        .size(size = 96.dp)
                        .align(alignment = Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_round_broken_image_24),
                    contentDescription = stringResource(id = R.string.image_loading_failed),
                )
            }
        }
    }
}
