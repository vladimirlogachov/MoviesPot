package com.vlohachov.moviespot.ui.discover

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscoverParam(
    val year: Int?,
    val selectedGenres: List<Int>?,
) : Parcelable
