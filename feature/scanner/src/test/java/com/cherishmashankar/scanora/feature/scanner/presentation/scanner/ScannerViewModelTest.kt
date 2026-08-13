package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat
import com.cherishmashankar.scanora.feature.scanner.presentation.FakeScanRepository
import com.cherishmashankar.scanora.feature.scanner.presentation.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ScannerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeScanRepository
    private lateinit var viewModel: ScannerViewModel

    @Before
    fun setup() {
        repository = FakeScanRepository()
        viewModel = ScannerViewModel(repository)
    }

    @Test
    fun `barcode detected shows result and stops scanning`() {
        val scan = testScan()

        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(scan)
        )

        val state = viewModel.uiState.value

        assertFalse(state.isScanning)
        assertEquals(scan, state.scanResult)
        assertEquals(
            ScannerStatus.SUCCESS,
            state.scannerStatus
        )
        assertFalse(state.isSaved)
    }

    @Test
    fun `save scan success marks result as saved`() = runTest {
        val scan = testScan()

        repository.saveResult =
            Result.success(42L)

        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(scan)
        )

        viewModel.onEvent(
            ScannerEvent.SaveScan
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.isSaved)
        assertFalse(state.isSaving)
        assertEquals(
            42L,
            state.scanResult?.id
        )
        assertNull(state.saveError)
        assertEquals(
            1,
            repository.saveCallCount
        )
    }

    @Test
    fun `save scan failure exposes error and keeps result`() = runTest {
        val scan = testScan()

        repository.saveResult =
            Result.failure(
                IllegalStateException("Database failed")
            )

        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(scan)
        )

        viewModel.onEvent(
            ScannerEvent.SaveScan
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isSaved)
        assertFalse(state.isSaving)
        assertEquals(scan, state.scanResult)
        assertEquals(
            ScannerError.SaveFailed,
            state.saveError
        )
    }

    @Test
    fun `scan again clears result and restarts scanner`() {
        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(
                testScan()
            )
        )

        viewModel.onEvent(
            ScannerEvent.ScanAgain
        )

        val state = viewModel.uiState.value

        assertTrue(state.isScanning)
        assertNull(state.scanResult)
        assertFalse(state.isSaved)
        assertEquals(
            ScannerStatus.READY,
            state.scannerStatus
        )
    }

    @Test
    fun `retry camera clears camera error and increments retry key`() {
        viewModel.onEvent(
            ScannerEvent.CameraFailed
        )

        val beforeRetry =
            viewModel.uiState.value.cameraRetryKey

        viewModel.onEvent(
            ScannerEvent.RetryCamera
        )

        val state = viewModel.uiState.value

        assertNull(state.cameraError)
        assertTrue(state.isScanning)
        assertEquals(
            beforeRetry + 1,
            state.cameraRetryKey
        )
    }

    @Test
    fun `duplicate save only calls repository once`() = runTest {
        val scan = testScan()

        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(scan)
        )

        viewModel.onEvent(ScannerEvent.SaveScan)
        viewModel.onEvent(ScannerEvent.SaveScan)

        advanceUntilIdle()

        assertEquals(
            1,
            repository.saveCallCount
        )
    }

    @Test
    fun `detecting changes scanner status`() {
        viewModel.onEvent(
            ScannerEvent.Detecting
        )

        assertEquals(
            ScannerStatus.DETECTING,
            viewModel.uiState.value.scannerStatus
        )
    }

    @Test
    fun `detecting is ignored when scanner is stopped`() {
        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(
                testScan()
            )
        )

        viewModel.onEvent(
            ScannerEvent.Detecting
        )

        assertEquals(
            ScannerStatus.SUCCESS,
            viewModel.uiState.value.scannerStatus
        )
    }

    @Test
    fun `toggle flash changes flash state`() {
        assertFalse(
            viewModel.uiState.value.isFlashEnabled
        )

        viewModel.onEvent(
            ScannerEvent.ToggleFlash
        )

        assertTrue(
            viewModel.uiState.value.isFlashEnabled
        )

        viewModel.onEvent(
            ScannerEvent.ToggleFlash
        )

        assertFalse(
            viewModel.uiState.value.isFlashEnabled
        )
    }

    @Test
    fun `camera failure stops scanning and exposes error`() {
        viewModel.onEvent(
            ScannerEvent.CameraFailed
        )

        val state = viewModel.uiState.value

        assertFalse(state.isScanning)
        assertEquals(
            ScannerStatus.ERROR,
            state.scannerStatus
        )
        assertEquals(
            ScannerError.CameraUnavailable,
            state.cameraError
        )
    }

    @Test
    fun `save error consumed clears error`() = runTest {
        repository.saveResult =
            Result.failure(
                IllegalStateException("Database error")
            )

        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(
                testScan()
            )
        )

        viewModel.onEvent(
            ScannerEvent.SaveScan
        )

        advanceUntilIdle()

        assertEquals(
            ScannerError.SaveFailed,
            viewModel.uiState.value.saveError
        )

        viewModel.onEvent(
            ScannerEvent.SaveErrorConsumed
        )

        assertNull(
            viewModel.uiState.value.saveError
        )
    }

    @Test
    fun `granted camera permission sets scanner ready`() {
        viewModel.onEvent(
            ScannerEvent.CameraPermissionChanged(
                CameraPermissionState.GRANTED
            )
        )

        val state = viewModel.uiState.value

        assertEquals(
            CameraPermissionState.GRANTED,
            state.cameraPermissionState
        )

        assertEquals(
            ScannerStatus.READY,
            state.scannerStatus
        )
    }

    @Test
    fun `denied camera permission sets scanner error`() {
        viewModel.onEvent(
            ScannerEvent.CameraPermissionChanged(
                CameraPermissionState.DENIED
            )
        )

        val state = viewModel.uiState.value

        assertEquals(
            CameraPermissionState.DENIED,
            state.cameraPermissionState
        )

        assertEquals(
            ScannerStatus.ERROR,
            state.scannerStatus
        )
    }

    @Test
    fun `permanently denied camera permission sets scanner error`() {
        viewModel.onEvent(
            ScannerEvent.CameraPermissionChanged(
                CameraPermissionState.PERMANENTLY_DENIED
            )
        )

        assertEquals(
            ScannerStatus.ERROR,
            viewModel.uiState.value.scannerStatus
        )
    }

    @Test
    fun `barcode detection is ignored after scanner already stopped`() {
        val firstScan = testScan()

        val secondScan = Scan(
            value = "second",
            format = ScanFormat.BARCODE,
            timestamp = 2L
        )

        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(firstScan)
        )

        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(secondScan)
        )

        assertEquals(
            firstScan,
            viewModel.uiState.value.scanResult
        )
    }

    @Test
    fun `save can succeed after previous failure`() = runTest {
        val scan = testScan()

        repository.saveResult =
            Result.failure(IllegalStateException("failed"))

        viewModel.onEvent(
            ScannerEvent.BarcodeDetected(scan)
        )

        viewModel.onEvent(
            ScannerEvent.SaveScan
        )

        advanceUntilIdle()

        assertEquals(
            ScannerError.SaveFailed,
            viewModel.uiState.value.saveError
        )

        viewModel.onEvent(
            ScannerEvent.SaveErrorConsumed
        )

        repository.saveResult =
            Result.success(42L)

        viewModel.onEvent(
            ScannerEvent.SaveScan
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.isSaved)
        assertEquals(42L, state.scanResult?.id)
        assertNull(state.saveError)
        assertEquals(2, repository.saveCallCount)
    }


    private fun testScan() =
        Scan(
            value = "https://example.com",
            format = ScanFormat.QR_CODE,
            timestamp = 1L
        )
}