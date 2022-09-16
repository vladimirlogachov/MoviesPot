package com.vlohachov.moviespot.util

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext

class PagingDataCollector<T : Any>(coroutineContext: CoroutineContext = Dispatchers.Main) {

    companion object {
        private val PendingLoadState = LoadState.Loading
        private val IncompleteLoadState = LoadState.NotLoading(false)

        val InitialLoadStates = LoadStates(
            PendingLoadState,
            IncompleteLoadState,
            IncompleteLoadState,
        )
    }

    var snapshotList = ItemSnapshotList<T>(0, 0, emptyList())
        private set

    var loadStates = CombinedLoadStates(
        refresh = InitialLoadStates.refresh,
        prepend = InitialLoadStates.prepend,
        append = InitialLoadStates.append,
        source = InitialLoadStates
    )
        private set

    private val differCallback = object : DifferCallback {
        override fun onChanged(position: Int, count: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }

    private val dataDiffer = object : PagingDataDiffer<T>(
        differCallback = differCallback,
        mainContext = coroutineContext,
    ) {
        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit
        ): Int? {
            onListPresentable()
            updateSnapshotList()
            return null
        }
    }

    private fun updateSnapshotList() {
        snapshotList = dataDiffer.snapshot()
    }

    suspend fun collectData(pagingData: PagingData<T>) {
        dataDiffer.collectFrom(pagingData = pagingData)
    }

    suspend fun collectStates() {
        loadStates = dataDiffer.loadStateFlow.first()
    }
}

suspend fun <T : Any> PagingData<T>.collector(): PagingDataCollector<T> =
    PagingDataCollector<T>().apply {
        collectData(pagingData = this@collector)
        collectStates()
    }
