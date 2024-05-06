package com.vlohachov.shared.ui.di

import com.vlohachov.shared.ui.screen.credits.cast.CastViewModel
import com.vlohachov.shared.ui.screen.credits.crew.CrewViewModel
import com.vlohachov.shared.ui.screen.details.MovieDetailsViewModel
import com.vlohachov.shared.ui.screen.main.MainViewModel
import com.vlohachov.shared.ui.screen.settings.SettingsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val viewModelModule = module {
    singleOf(::MainViewModel)
    singleOf(::SettingsViewModel)
    factoryOf(::MovieDetailsViewModel)
    factoryOf(::CastViewModel)
    factoryOf(::CrewViewModel)
}
