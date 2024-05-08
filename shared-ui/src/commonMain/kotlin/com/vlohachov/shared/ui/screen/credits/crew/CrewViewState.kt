package com.vlohachov.shared.ui.screen.credits.crew

import com.vlohachov.shared.core.ViewState
import com.vlohachov.shared.domain.model.movie.credit.CrewMember

internal data class CrewViewState(
    val viewState: ViewState<List<CrewMember>> = ViewState.Loading,
    val error: Throwable? = null,
)
