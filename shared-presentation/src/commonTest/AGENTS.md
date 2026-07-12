# Testing Best Practices for @Reviewer and @Implementer

This document contains common patterns and troubleshooting tips identified during the transition to
the **v2 UI Testing API** in the `shared-presentation` module.

## 1. Asynchronous State Transitions (The "Shallow Wait")

**Problem**: `waitForIdle()` may return before a ViewModel `Flow` has emitted a new value or a
`LaunchedEffect` (like a Snackbar/ErrorBar) has been processed.
**Pattern**: Tests for `ViewState.Error` or `ViewState.Success` fail because the UI hasn't updated
yet.
**Solution**: Use `waitUntil` with a predicate that checks for the actual node's state.

```kotlin
// Robust way to wait for an Error Snackbar/Bar
waitUntil(timeoutMillis = 5000) {
    onAllNodesWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
        .fetchSemanticsNodes().any { node ->
            node.size.width > 0 && node.size.height > 0
        }
}
onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag).assertIsDisplayed()
```

## 2. Semantics Tree Merging

**Problem**: `onNodeWithTag` fails to find a node even though it's in the code.
**Cause**: Material3 components (Buttons, Chips, Sections) often set `mergeDescendants = true`,
hiding the `testTag` of their children from the default merged tree.
**Solution**: Use `useUnmergedTree = true` in the finder.

```kotlin
onNodeWithTag(testTag = "my_tag", useUnmergedTree = true)
    .assertExists()
```

## 3. ViewModel StateFlow Testing (Turbine)

**Problem**: "Unconsumed events found" error in Turbine.
**Cause**: `StateFlow` always has an initial value. Turbine's `test` extension catches this initial
emission immediately.
**Solution**: Use `skipItems(1)` to bypass the initial state or `cancelAndIgnoreRemainingEvents()`
if additional emissions are expected but not relevant to the test.

```kotlin
viewModel.uiState.test {
    skipItems(1) // Skip the initial Loading state
    assertIs<ViewState.Success>(awaitItem())
}
```

## 4. runComposeUiTest v2

**Context**: We strictly use `androidx.compose.ui.test.v2.runComposeUiTest`.

* This version uses `StandardTestDispatcher` by default.
* It is more deterministic but requires more explicit control over timing (like using `waitUntil`).
* Ensure all tests are updated to this import.
