package ru.esm.tspiot.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.esm.tspiot.data.EsmServiceClient
import ru.esm.tspiot.domain.EsmResult
import ru.esm.tspiot.domain.EsmResult.Error
import ru.esm.tspiot.domain.EsmResult.Success
import ru.esp.esm.api.model.EanCheckResponse
import toPrettyString
import javax.inject.Inject

@HiltViewModel
class EanCheckViewModel @Inject constructor(
    private val esmServiceClient: EsmServiceClient
) : ViewModel() {
    private val _checkResult = MutableStateFlow<EsmResult<String>?>(null)
    val checkResult: StateFlow<EsmResult<String>?> = _checkResult

    init {
        Log.d("CodesCheckViewModel", "init")
    }

    override fun onCleared() {
        Log.d("CodesCheckViewModel", "onCleared")
        super.onCleared()
    }

    fun eansCheck(codes: List<String>) {
        viewModelScope.launch {
            Log.d(TAG, "eanCheck called")
            _checkResult.value = EsmResult.Loading

            val esmResult = esmServiceClient.eansCheck(codes)
            Log.d(TAG, "codesCheck response $esmResult")
            when (esmResult) {
                is Success -> {
                    val bundle = esmResult.data
                    bundle.classLoader = EanCheckResponse::class.java.classLoader
                    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(
                            "key",
                            EanCheckResponse::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        bundle.getParcelable("key")
                    }
                    _checkResult.value = Success("Успех: ${result?.toPrettyString()}")
                }

                is Error -> {
                    _checkResult.value = Error(esmResult.code, esmResult.message)
                }

                EsmResult.ServiceUnavailable -> {
                    _checkResult.value = EsmResult.ServiceUnavailable
                }

                EsmResult.Loading -> {} // do nothing
            }
        }
    }

    fun clearCheckResult() {
        viewModelScope.launch {
            _checkResult.value = null
        }
    }

    fun requestCheck(codes: List<String>) {
        viewModelScope.launch {
            Log.d(TAG, "requestCheck called")
        }
    }

    companion object {
        private const val TAG = "CodesCheckViewModel"
    }
}