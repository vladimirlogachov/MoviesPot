package com.vlohachov.shared.presentation.di

import com.vlohachov.shared.domain.usecase.DiscoverMovies
import com.vlohachov.shared.domain.usecase.LoadGenres
import com.vlohachov.shared.domain.usecase.SearchMovies
import com.vlohachov.shared.domain.usecase.credits.LoadCast
import com.vlohachov.shared.domain.usecase.credits.LoadCrew
import com.vlohachov.shared.domain.usecase.credits.LoadDirector
import com.vlohachov.shared.domain.usecase.movie.LoadCredits
import com.vlohachov.shared.domain.usecase.movie.LoadDetails
import com.vlohachov.shared.domain.usecase.movie.LoadKeywords
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val useCaseModule = module {
    singleOf(constructor = ::LoadMoviesByCategory) {
        createdAtStart()
    }
    singleOf(constructor = ::LoadGenres)
    singleOf(constructor = ::LoadDetails)
    singleOf(constructor = ::LoadCredits)
    singleOf(constructor = ::LoadDirector)
    singleOf(constructor = ::LoadCast)
    singleOf(constructor = ::LoadCrew)
    singleOf(constructor = ::LoadRecommendations)
    singleOf(constructor = ::LoadKeywords)
    singleOf(constructor = ::SearchMovies)
    singleOf(constructor = ::DiscoverMovies)
    singleOf(constructor = ::LoadSettings)
    singleOf(constructor = ::ApplyDynamicTheme)
}
