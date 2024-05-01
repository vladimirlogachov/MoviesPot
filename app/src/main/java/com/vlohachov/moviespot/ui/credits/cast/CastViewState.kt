package com.vlohachov.moviespot.ui.credits.cast

import com.vlohachov.shared.domain.model.movie.credit.CastMember
import com.vlohachov.shared.ui.state.ViewState

data class CastViewState(
    val viewState: ViewState<List<CastMember>> = ViewState.Loading,
    val error: Throwable? = null,
)
