package com.vlohachov.moviespot.ui.movies

import com.vlohachov.moviespot.R

enum class MoviesSection(val textRes: Int) {
    Upcoming(textRes = R.string.upcoming),
    NowPlaying(textRes = R.string.now_playing),
    Popular(textRes = R.string.popular),
    TopRated(textRes = R.string.top_rated),
}