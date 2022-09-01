package com.vlohachov.moviespot.ui.credits.cast

import com.vlohachov.domain.model.movie.credit.CastMember
import com.vlohachov.moviespot.core.ViewState

data class CastViewState(
    val viewState: ViewState<List<CastMember>> = ViewState.Loading,
    val error: Throwable? = null,
)
