package ru.esm.tspiot.ui.scanner

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.esm.tspiot.domain.ScanResult
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor() : ViewModel() {
    private val _scanResult = MutableStateFlow<ScanResult?>(value = DEFAULT_SCAN_RESULT)
    val scanResult = _scanResult.asStateFlow()
    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()
    val errorMessage = mutableStateOf<String?>(null)

    fun stopScanning() {
        _isScanning.value = false
    }

    fun onScanResult(result: ScanResult) {
        viewModelScope.launch {
            val charToRemove = "\u001D"
            val trimmedMark = result.text.removePrefix(charToRemove)
            _scanResult.emit(result.copy(text = trimmedMark))
            stopScanning()
        }
    }

    fun onScanError(error: String) {
        errorMessage.value = error
        stopScanning()
    }

    fun clearResult() {
        viewModelScope.launch {
            _scanResult.emit(null)
            errorMessage.value = null
        }
    }

    companion object {
        val DEFAULT_SCAN_RESULT = ScanResult(
            text = "0104670540176099215'W9Um\u001d93dGVz",
            format = "DATA MATRIX",
            timestamp = System.currentTimeMillis()
        )
    }
}