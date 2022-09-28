package com.vlohachov.moviespot.ui.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R

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
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
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