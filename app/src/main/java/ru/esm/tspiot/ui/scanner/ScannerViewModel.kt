package ru.esm.tspiot.ui.scanner

import android.util.Log
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
    private val _scanResult = MutableStateFlow<Set<ScanResult>?>(value = null)
    val scanResult = _scanResult.asStateFlow()
    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()
    val errorMessage = mutableStateOf<String?>(null)

    init {
        Log.d("ScannerViewModel", "init")
    }

    override fun onCleared() {
        Log.d("ScannerViewModel", "onCleared")
        super.onCleared()
    }

    private fun stopScanning() {
        _isScanning.value = false
    }

    fun onScanResult(result: ScanResult) {
        viewModelScope.launch {
            val scanResult = scanResult.value ?: mutableSetOf()
            val charToRemove = "\u001D"
            val trimmedMark = result.text.removePrefix(charToRemove)
            _scanResult.value = scanResult.plus(result.copy(text = trimmedMark))
            Log.d("ScannerViewModel", trimmedMark)
            stopScanning()
        }
    }

    fun onScanError(error: String) {
        errorMessage.value = error
        stopScanning()
    }

    fun scan() {
        viewModelScope.launch {
            _isScanning.value = true
        }
    }
    fun clearResult() {
        viewModelScope.launch {
            _scanResult.value = null
            errorMessage.value = null
            _isScanning.value = true
        }
    }
}