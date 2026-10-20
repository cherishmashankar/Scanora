package com.cherishmashankar.scanora.feature.scanner.presentation.result

import androidx.lifecycle.SavedStateHandle
import com.cherishmashankar.scanora.core.domain.ScanContentDetector
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanContent
import com.cherishmashankar.scanora.core.model.ScanFormat
import com.cherishmashankar.scanora.feature.scanner.FakeScanRepository
import com.cherishmashankar.scanora.feature.scanner.presentation.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.launch




@OptIn(ExperimentalCoroutinesApi::class)
internal class ScanResultViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val contentDetector = ScanContentDetector()

    @Test
    fun `valid scan id loads scan successfully`() = runTest {
        val repository = FakeScanRepository()
        val scan = testScan()

        repository.scanFlow.value = scan

        val viewModel = createViewModel(
            repository = repository,
            scanId = 42L
        )

        val job = backgroundScope.launch {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()

        assertEquals(
            ScanResultUiState.Success(
                scan = scan,
                content = ScanContent.Url("https://example.com"),
                actions = listOf(
                    SmartAction.OPEN_URL,
                    SmartAction.COPY,
                    SmartAction.SHARE
                )
            ),
            viewModel.uiState.value
        )

        job.cancel()
    }

    @Test
    fun `missing scan id returns not found`() {
        val viewModel = ScanResultViewModel(
            scanRepository = FakeScanRepository(),
            savedStateHandle = SavedStateHandle(),
            contentDetector = contentDetector
        )

        assertEquals(
            ScanResultUiState.NotFound,
            viewModel.uiState.value
        )
    }

    @Test
    fun `null scan returns not found`() = runTest {
        val repository = FakeScanRepository()

        val viewModel = createViewModel(
            repository = repository,
            scanId = 42L
        )

        val job = backgroundScope.launch {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()

        assertEquals(
            ScanResultUiState.NotFound,
            viewModel.uiState.value
        )

        job.cancel()
    }

    @Test
    fun `plain text scan provides copy and share actions`() = runTest {
        val repository = FakeScanRepository()
        val scan = testScan().copy(value = "Hello Scanora")

        repository.scanFlow.value = scan

        val viewModel = createViewModel(
            repository = repository,
            scanId = 42L
        )

        val job = backgroundScope.launch {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()

        assertEquals(
            ScanResultUiState.Success(
                scan = scan,
                content = ScanContent.Text("Hello Scanora"),
                actions = listOf(
                    SmartAction.COPY,
                    SmartAction.SHARE
                )
            ),
            viewModel.uiState.value
        )

        job.cancel()
    }

    @Test
    fun `delete success calls deleted callback`() = runTest {
        val repository = FakeScanRepository()
        var deleted = false

        val viewModel = createViewModel(
            repository = repository,
            scanId = 42L
        )

        viewModel.deleteScan {
            deleted = true
        }

        advanceUntilIdle()

        assertTrue(deleted)
        assertFalse(viewModel.isDeleting.value)
        assertFalse(viewModel.deleteError.value)
        assertEquals(1, repository.deleteCallCount)
    }

    @Test
    fun `delete failure exposes delete error`() = runTest {
        val repository = FakeScanRepository().apply {
            deleteResult = Result.failure(
                IllegalStateException("Delete failed")
            )
        }

        val viewModel = createViewModel(
            repository = repository,
            scanId = 42L
        )

        viewModel.deleteScan {}
        advanceUntilIdle()

        assertFalse(viewModel.isDeleting.value)
        assertTrue(viewModel.deleteError.value)
        assertEquals(1, repository.deleteCallCount)
    }

    @Test
    fun `duplicate delete while deleting is ignored`() = runTest {
        val repository = FakeScanRepository().apply {
            blockDelete = true
        }

        val viewModel = createViewModel(
            repository = repository,
            scanId = 42L
        )

        viewModel.deleteScan {}
        runCurrent()

        assertTrue(viewModel.isDeleting.value)

        viewModel.deleteScan {}
        runCurrent()

        assertEquals(
            1,
            repository.deleteCallCount
        )

        repository.deleteGate.complete(Unit)
        advanceUntilIdle()

        assertFalse(viewModel.isDeleting.value)
    }

    @Test
    fun `consume delete error clears error`() = runTest {
        val repository = FakeScanRepository().apply {
            deleteResult = Result.failure(
                IllegalStateException("Delete failed")
            )
        }

        val viewModel = createViewModel(
            repository = repository,
            scanId = 42L
        )

        viewModel.deleteScan {}
        advanceUntilIdle()

        assertTrue(viewModel.deleteError.value)

        viewModel.consumeDeleteError()

        assertFalse(viewModel.deleteError.value)
    }

    @Test
    fun `delete can succeed after previous failure`() = runTest {
        val repository = FakeScanRepository().apply {
            deleteResult = Result.failure(
                IllegalStateException("failed")
            )
        }

        val viewModel = createViewModel(
            repository = repository,
            scanId = 42L
        )

        var deleted = false

        viewModel.deleteScan {
            deleted = true
        }

        advanceUntilIdle()

        assertTrue(viewModel.deleteError.value)
        assertFalse(deleted)

        viewModel.consumeDeleteError()

        repository.deleteResult = Result.success(Unit)

        viewModel.deleteScan {
            deleted = true
        }

        advanceUntilIdle()

        assertTrue(deleted)
        assertFalse(viewModel.deleteError.value)
        assertEquals(2, repository.deleteCallCount)
    }

    private fun createViewModel(
        repository: FakeScanRepository,
        scanId: Long
    ) = ScanResultViewModel(
        scanRepository = repository,
        savedStateHandle = SavedStateHandle(
            mapOf("scanId" to scanId)
        ),
        contentDetector = contentDetector
    )

    private fun testScan() = Scan(
        id = 42L,
        value = "https://example.com",
        format = ScanFormat.QR_CODE,
        timestamp = 1L
    )
}
