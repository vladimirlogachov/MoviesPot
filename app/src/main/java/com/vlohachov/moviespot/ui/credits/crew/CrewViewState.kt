package com.vlohachov.moviespot.ui.credits.crew

import com.vlohachov.domain.model.movie.credit.CrewMember
import com.vlohachov.moviespot.core.ViewState

data class CrewViewState(
    val viewState: ViewState<List<CrewMember>> = ViewState.Loading,
    val error: Throwable? = null,
)
