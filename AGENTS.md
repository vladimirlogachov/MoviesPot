# AI Agent Personas

When I reference a specific agent, adopt the following persona and focus areas:

## @Architect

- **Role**: System Designer & KMP Expert.
- **Focus**: Module boundaries, Domain modeling, and Data flow.
- **Behavior**: Do not write implementation code. Review the `shared-domain` layer. Ensure business
  logic is platform-agnostic. Warn if I try to put logic in the UI.

## @Implementer

- **Role**: Senior Kotlin Developer.
- **Focus**: Writing production-ready code in `shared-presentation` and `shared-data`.
- **Behavior**: Follow the "Common Tasks" strictly. Always handle `Result` types. Use
  `koinViewModel` and `ViewState`.

## @Reviewer

- **Role**: QA & Code Cleaner.
- **Focus**: Finding smells, memory leaks, and architectural violations.
- **Behavior**: Check that ViewModels do not hold references to Android context. Verify that
  `scheme` objects are mapped to `domain` objects. Suggest unit tests.

## @iOS_Check

- **Role**: iOS Interop Specialist.
- **Focus**: Ensuring shared code works for the iOS target.
- **Behavior**: Warn about Kotlin features that don't interop well with Swift (e.g., specific Flow
  types, default arguments in interfaces, `suspend` functions exposed to Obj-C).

---

# Project Overview

MoviesPot is a **Kotlin Multiplatform (KMP)** project that shares logic and UI between Android and
iOS. It follows **Clean Architecture** principles and uses **Jetpack Compose Multiplatform** for the
UI.

## Tech Stack

- **Language**: Kotlin 2.3.0
- **UI**: Compose Multiplatform (Material 3)
- **Dependency Injection**: Koin
- **Networking**: Ktor
- **Image Loading**: Coil
- **Asynchronous Programming**: Coroutines & Flow
- **Navigation**: Jetpack Navigation (Compose)

## KMP Constraints

- **Standard Library**: Use strictly `kotlin-stdlib`. Do not use `java.*` imports in `shared`
  modules.
- **Resources**: All strings/images must be accessed via `composeResources` (Moko resources or
  official Jetbrains Resources).
- **Context**: Do not pass Android `Context` into ViewModels or UseCases.

## Module Structure

The project is divided into the following modules:

- **`:androidApp`**: Android application entry point.
- **`:shared-presentation`**: Contains the UI logic, ViewModels, Screens, and Navigation.
- **`:shared-domain`**: Contains the Business Logic, UseCases, Domain Models, and Repository
  Interfaces.
- **`:shared-data`**: Contains the Repository Implementations, API Data Transfer Objects (Schemes),
  and Networking logic.

## Architecture & Patterns

### 1. Presentation Layer (`:shared-presentation`)

**ViewModels:**

- Inherit from `androidx.lifecycle.ViewModel`.
- Expose a single `uiState` as a `StateFlow<ViewState>`.
- Use `combine` to aggregate data from multiple flows/UseCases.
- Use `stateIn` with `WhileUiSubscribed` (5 seconds timeout).
- Handle errors using a separate `_error` flow or within the `ViewState`.
- Use `koinViewModel` for injection in Composable.

**UI (Compose):**

- **Screens**: Defined as objects inheriting from `Screen<Params>`. They encapsulate route logic and
  arguments.
- **Navigation**:
    - Uses a route-based approach via `MoviesPotNavHost`.
    - `Screen` objects provide a `composable` extension function for the `NavGraphBuilder`.
    - The central navigation host (`MoviesPotNavHost`) registers all screens.
- **State Handling**: UI observes `uiState` using `collectAsState`.
- **ViewState**: Uses a generic `ViewState<T>` sealed interface (`Loading`, `Success`, `Error`).
- **Resources**:
    - Managed via Compose Multiplatform Resources (`composeResources`).
    - `Res.string.some_string` and `Res.drawable.some_image` are used.
    - Resource files located in `shared-presentation/src/commonMain/composeResources/`.

### 2. Domain Layer (`:shared-domain`)

**UseCases:**

- Define a class with an `operator fun invoke`.
- Return `Result<Output>` or `Flow<Result<Output>>`.
- Encapsulate a single business action.
- Naming convention: `VerbSubject` (e.g., `LoadDetails`, `LoadDirector`).

**Models:**

- Pure Kotlin data classes.
- Independent of frameworks/libraries (except basic Kotlin types).

### 3. Data Layer (`:shared-data`)

**Repositories:**

- Implement interfaces defined in the Domain layer.
- Use `HttpClient` (Ktor) to fetch data.
- Return `Flow` of domain models.

**Schemes (DTOs):**

- Represent the API response structure.
- Located in `scheme` package.
- Include `toDomain()` extension functions to map to Domain models.

## Code Style & Conventions

- **Indentation**: 4 spaces.
- **Visibility**: Use `internal` for classes/functions that shouldn't be exposed outside the module.
- **Annotations**: Use `@Stable` for UI state classes and ViewModels to optimize Compose
  performance.
- **Extensions**: Extensive use of extension functions for mapping and scope-specific logic.
- **Files**: One class per file (generally).

## DI (Koin) Setup

- **Modules**: Located in `shared-presentation/src/commonMain/kotlin/.../di`.
    - `ViewModelModule`: Registers ViewModels.
    - `UseCaseModule`: Registers UseCases.
    - `DataModule`: Registers Repositories.
    - `AppModule`: General app dependencies.
- **Injection**:
    - Use `koinViewModel()` in Compose for ViewModels.
    - Use constructor injection for classes.

## Testing

### Tech Stack

- **Framework**: `kotlin-test` (assertions, `@Test`).
- **Mocking**: `Mokkery` (for mocking interfaces like Repositories).
- **Coroutines**: `kotlinx-coroutines-test` (`runTest`).
- **Flow Testing**: `Turbine` (`test` extension).
- **Networking**: `Ktor MockEngine` (for simulating API responses).
- **JS Interop**: `@JsName` annotation used for test function names.

### Patterns

**ViewModel Tests:**

- Use `runTest` for coroutine support.
- Mock repositories using `Mokkery`.
- Use `Turbine` to test `StateFlow` emissions (`viewModel.uiState.test { ... }`).
- Verify initial loading state, success state, and error handling.
- Use `skipItems()` to skip initial states when necessary.

**Repository Tests:**

- Mock `HttpClient` with success/failure responses.
- Test `Flow` emissions using `Turbine`.
- Verify mapping from Scheme to Domain models.

## CI/CD (GitHub Actions)

- **Workflow**: `validation.yml` triggers on PRs and pushes to `master`.
- **Jobs**:
    - `assemble-android`: Builds Debug/Release APKs.
    - `detekt`: Runs static code analysis.
    - `test-data`: Runs tests for `:shared-data`.
    - `test-domain`: Runs tests for `:shared-domain`.
    - `test-presentation`: Runs tests for `:shared-presentation` (Desktop target).

## Common Tasks

### Creating a New Screen

1. Define a `Screen` object in `shared-presentation` inheriting `Screen<Params>`.
2. Create a `ViewModel` inheriting `ViewModel` and injecting necessary UseCases.
3. Create a `ViewState` data class.
4. Implement the UI in a Composable function (usually in the same file as the Screen object or a
   separate file if large).
5. Register the ViewModel in Koin module (`ViewModelModule.kt`).
6. Add the screen composable to `MoviesPotNavHost`.

### Adding a New UseCase

1. Define the class in `shared-domain/usecase`.
2. Define an `operator fun invoke` returning `Result<T>` or `Flow<Result<T>>`.
3. Inject the Repository.
4. Register in Koin module (`UseCaseModule.kt`).

### Adding a New API Endpoint

1. Add the response Scheme (DTO) in `shared-data/scheme`.
2. Add the mapping extension `toDomain()`.
3. Update the Repository interface in `shared-domain`.
4. Implement the method in `RemoteMovieRepository` in `shared-data`.

### Adding Resources

1. Add string resources to `shared-presentation/src/commonMain/composeResources/values/strings.xml`.
2. Add image resources to `shared-presentation/src/commonMain/composeResources/drawable/`.
3. Access using `Res.string.key` or `Res.drawable.filename`.
