package com.vlohachov.shared.ui.screen.credits.cast

import com.vlohachov.shared.core.ViewState
import com.vlohachov.shared.domain.model.movie.credit.CastMember

internal data class CastViewState(
    val viewState: ViewState<List<CastMember>> = ViewState.Loading,
    val error: Throwable? = null,
)
