package com.vlohachov.moviespot.ui.credits.cast

import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.shared.domain.model.movie.credit.CastMember

data class CastViewState(
    val viewState: ViewState<List<CastMember>> = ViewState.Loading,
    val error: Throwable? = null,
)
