package com.cherishmashankar.scanora.feature.history.presentation

import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope

@OptIn(ExperimentalCoroutinesApi::class)
internal class HistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeScanRepository
    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setup() {
        repository = FakeScanRepository()
        viewModel = HistoryViewModel(repository)
    }

    @Test
    fun `initial state is empty`() {
        val state = viewModel.uiState.value

        assertTrue(state.scans.isEmpty())
        assertEquals("", state.searchQuery)
        assertFalse(state.isSearchActive)
    }

    @Test
    fun `initial scans are exposed`() = runTest {
        repository.scansFlow.value = listOf(
            testScan(1L, "first"),
            testScan(2L, "second")
        )

        collectUiState()

        advanceUntilIdle()

        assertEquals(
            2,
            viewModel.uiState.value.scans.size
        )
    }

    @Test
    fun `history updates when repository emits new scans`() = runTest {
        collectUiState()

        repository.scansFlow.value = listOf(
            testScan(1L, "first")
        )

        advanceUntilIdle()

        assertEquals(
            1,
            viewModel.uiState.value.scans.size
        )

        repository.scansFlow.value = listOf(
            testScan(1L, "first"),
            testScan(2L, "second")
        )

        advanceUntilIdle()

        assertEquals(
            2,
            viewModel.uiState.value.scans.size
        )
    }

    @Test
    fun `search click activates search mode`() = runTest {
        collectUiState()

        viewModel.onSearchClick()
        advanceUntilIdle()

        assertTrue(
            viewModel.uiState.value.isSearchActive
        )
    }

    @Test
    fun `search query updates ui state`() = runTest {
        collectUiState()

        viewModel.onSearchQueryChanged("google")
        advanceUntilIdle()

        assertEquals(
            "google",
            viewModel.uiState.value.searchQuery
        )
    }

    @Test
    fun `non blank query uses search results`() = runTest {
        repository.scansFlow.value = listOf(
            testScan(1L, "full history")
        )

        repository.searchFlow.value = listOf(
            testScan(2L, "https://google.com")
        )

        collectUiState()

        viewModel.onSearchQueryChanged("google")
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(
            "google",
            state.searchQuery
        )

        assertEquals(
            1,
            state.scans.size
        )

        assertEquals(
            "https://google.com",
            state.scans.first().value
        )
    }

    @Test
    fun `empty search result exposes empty list`() = runTest {
        repository.searchFlow.value = emptyList()

        collectUiState()

        viewModel.onSearchClick()
        viewModel.onSearchQueryChanged("missing")

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.isSearchActive)
        assertEquals("missing", state.searchQuery)
        assertTrue(state.scans.isEmpty())
    }

    @Test
    fun `blank query returns full history`() = runTest {
        repository.scansFlow.value = listOf(
            testScan(1L, "one"),
            testScan(2L, "two")
        )

        repository.searchFlow.value = listOf(
            testScan(3L, "searched")
        )

        collectUiState()

        viewModel.onSearchQueryChanged("search")
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("")
        advanceUntilIdle()

        assertEquals(
            2,
            viewModel.uiState.value.scans.size
        )
    }

    @Test
    fun `whitespace query behaves as blank search`() = runTest {
        repository.scansFlow.value = listOf(
            testScan(1L, "one"),
            testScan(2L, "two")
        )

        repository.searchFlow.value = listOf(
            testScan(3L, "search result")
        )

        collectUiState()

        viewModel.onSearchQueryChanged("   ")
        advanceUntilIdle()

        assertEquals(
            2,
            viewModel.uiState.value.scans.size
        )
    }

    @Test
    fun `changing search query switches to latest results`() = runTest {
        collectUiState()

        repository.searchFlow.value = listOf(
            testScan(1L, "first result")
        )

        viewModel.onSearchQueryChanged("first")
        advanceUntilIdle()

        assertEquals(
            "first result",
            viewModel.uiState.value.scans.first().value
        )

        repository.searchFlow.value = listOf(
            testScan(2L, "second result")
        )

        viewModel.onSearchQueryChanged("second")
        advanceUntilIdle()

        assertEquals(
            "second",
            viewModel.uiState.value.searchQuery
        )

        assertEquals(
            "second result",
            viewModel.uiState.value.scans.first().value
        )
    }

    @Test
    fun `closing search clears query`() = runTest {
        collectUiState()

        viewModel.onSearchClick()
        viewModel.onSearchQueryChanged("test")

        advanceUntilIdle()

        viewModel.onCloseSearch()
        advanceUntilIdle()

        assertEquals(
            "",
            viewModel.uiState.value.searchQuery
        )
    }

    @Test
    fun `closing search disables search mode`() = runTest {
        collectUiState()

        viewModel.onSearchClick()
        advanceUntilIdle()

        assertTrue(
            viewModel.uiState.value.isSearchActive
        )

        viewModel.onCloseSearch()
        advanceUntilIdle()

        assertFalse(
            viewModel.uiState.value.isSearchActive
        )
    }

    @Test
    fun `closing search restores full history`() = runTest {
        repository.scansFlow.value = listOf(
            testScan(1L, "one"),
            testScan(2L, "two")
        )

        repository.searchFlow.value = listOf(
            testScan(3L, "searched")
        )

        collectUiState()

        viewModel.onSearchClick()
        viewModel.onSearchQueryChanged("searched")
        advanceUntilIdle()

        assertEquals(
            1,
            viewModel.uiState.value.scans.size
        )

        viewModel.onCloseSearch()
        advanceUntilIdle()

        assertEquals(
            2,
            viewModel.uiState.value.scans.size
        )
    }

    @Test
    fun `search mode can be opened when history is empty`() = runTest {
        repository.scansFlow.value = emptyList()

        collectUiState()

        viewModel.onSearchClick()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.isSearchActive)
        assertTrue(state.scans.isEmpty())
    }

    private fun TestScope.collectUiState(): Job =
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }

    private fun testScan(
        id: Long,
        value: String
    ) = Scan(
        id = id,
        value = value,
        format = ScanFormat.QR_CODE,
        timestamp = id
    )
}