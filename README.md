![Static Badge](https://img.shields.io/badge/android-3DDC84?label=target) ![Static Badge](https://img.shields.io/badge/iOS-A2AAAD?label=target)

[![Validation](https://github.com/vladimirlogachov/MoviesPot/actions/workflows/validation.yml/badge.svg?branch=master)](https://github.com/vladimirlogachov/MoviesPot/actions/workflows/validation.yml)

# Movies Pot

Kotlin + Compose multiplatform project that allows users to explore the world of movies with
ease. It utilizes the TMDB API to fetch movie data and presents it in a visually appealing manner.
Jetpack Navigation ensures smooth navigation between screens, while the Paging library enables
efficient data loading for a seamless user experience. The user interface adheres to Material3
design guidelines, providing a modern and intuitive way to discover and engage with movie content.

## Target audience

- Developers interested in learning more about Kotlin and Compose multiplatform.
- Designers and UI enthusiasts seeking inspiration for Material3-based interfaces.

## Journey

![App Journey](/media/app_journey.gif)

## Screenshots

#### App icon

| Default                                          | Themed (light)                                                | Themed (dark)                                               |
|:-------------------------------------------------|:--------------------------------------------------------------|:------------------------------------------------------------|
| ![Default App Icon](/media/default_app_icon.png) | ![Themed App Icon (light)](/media/dynamic_app_icon_light.png) | ![Themed App Icon (dark)](/media/dynamic_app_icon_dark.png) |

#### App Theme

| Default (light)                                                | Default (dark)                                               |
|:---------------------------------------------------------------|:-------------------------------------------------------------|
| ![Themed App Icon (light)](/media/default_app_theme_light.png) | ![Themed App Icon (dark)](/media/default_app_theme_dark.png) |

| Dynamic (light)                                                | Dynamic (dark)                                               |
|:---------------------------------------------------------------|:-------------------------------------------------------------|
| ![Themed App Icon (light)](/media/dynamic_app_theme_light.png) | ![Themed App Icon (dark)](/media/dynamic_app_theme_dark.png) |

## Features

- Data and Domain layers implemented
  using [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- Presentation layer implemented
  using [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
  and [Material 3](https://m3.material.io/) design guidelines
- Supports [Dynamic Colors](https://m3.material.io/styles/color/dynamic-color/overview) schema
- Adaptive
  launcher [Themed Icon](https://developer.android.com/develop/ui/views/launch/icon_design_adaptive) (
  for Android 13+)
- Navigation based
  on [Jetpack Navigation](https://developer.android.com/jetpack/androidx/releases/navigation)
- Uses [TMDB](https://www.themoviedb.org) open API
- Infinite lists (pagination) implemented
  using [Jetpack Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- Data flow implemented
  using [Kotlin Coroutines Flow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/)
- Multiplatform unit tests
- Multiplatform UI tests

## Tools

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Jetpack Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Jetpack Navigation](https://developer.android.com/jetpack/androidx/releases/navigation)
- [Ktor](https://ktor.io/)
- [Coil](https://coil-kt.github.io/coil/)
- [Koin](https://insert-koin.io/)
- [BuildKonfig](https://github.com/yshrsmz/BuildKonfig)
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings)
- [Mokkery](https://mokkery.dev/)
- [Turbine](https://github.com/google/turbine)
- [Detekt](https://detekt.dev/)
