package com.vlohachov.data.remote.schema.genre

import com.google.gson.annotations.SerializedName

data class GenresSchema(
    @SerializedName("genres")
    val genres: List<GenreSchema>
)