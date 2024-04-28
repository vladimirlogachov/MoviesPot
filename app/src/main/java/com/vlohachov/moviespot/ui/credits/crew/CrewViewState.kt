package com.vlohachov.moviespot.ui.credits.crew

import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.shared.domain.model.movie.credit.CrewMember

data class CrewViewState(
    val viewState: ViewState<List<CrewMember>> = ViewState.Loading,
    val error: Throwable? = null,
)
