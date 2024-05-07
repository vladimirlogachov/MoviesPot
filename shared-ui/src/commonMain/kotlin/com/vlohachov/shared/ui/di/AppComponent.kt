package com.vlohachov.shared.ui.di

import com.vlohachov.shared.ui.screen.keyword.keywordMoviesModule
import com.vlohachov.shared.ui.screen.movies.moviesModule
import com.vlohachov.shared.ui.screen.movies.similar.similarMoviesModule
import com.vlohachov.shared.ui.screen.search.moviesSearchModule
import org.koin.core.module.Module

public val appComponent: List<Module> =
    listOf(
        appModule,
        dataModule,
        useCaseModule,
        viewModelModule,
        // TODO: To be removed once paging multiplatform supports all targets
        moviesSearchModule,
        moviesModule,
        similarMoviesModule,
        keywordMoviesModule,
    )
