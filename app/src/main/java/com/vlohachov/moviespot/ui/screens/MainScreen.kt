package com.vlohachov.moviespot.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier) {
    Scaffold(
        modifier = modifier,
    ) { paddingValues ->

    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MoviesPotTheme {
        MainScreen(modifier = Modifier.fillMaxSize())
    }
}