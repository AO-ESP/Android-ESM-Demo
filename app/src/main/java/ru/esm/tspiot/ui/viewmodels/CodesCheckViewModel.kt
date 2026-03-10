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
import ru.esp.esm.api.model.Cis
import ru.esp.esm.api.model.CisList
import ru.esp.esm.api.model.ClientInfo
import ru.esp.esm.api.model.CodesCheckRequest
import ru.esp.esm.api.model.CodesCheckResponse
import toPrettyString
import javax.inject.Inject

@HiltViewModel
class CodesCheckViewModel @Inject constructor(
    private val esmServiceClient: EsmServiceClient
) : ViewModel() {
    private val _checkResult = MutableStateFlow<EsmResult<String>?>(null)
    val checkResult: StateFlow<EsmResult<String>?> = _checkResult

    val clientInfo = ClientInfo(
        "ESM Test",
        "1.0",
        "90911ffe-47da-4a71-86bb-be455f3d9614",
        "90911ffe-47da-4a71-86bb-be455f3d9614",
        null
    )

    init {
        Log.d("CodesCheckViewModel", "init")
    }

    override fun onCleared() {
        Log.d("CodesCheckViewModel", "onCleared")
        super.onCleared()
    }

    fun codesCheck(codes: List<Cis>, tz: Int? = null) {
        viewModelScope.launch {
            Log.d(TAG, "codesCheck called")
            _checkResult.value = EsmResult.Loading
            val cisList = CisList(codes, tz)

            val esmResult = esmServiceClient.codesCheck(
                CodesCheckRequest(
                    cisList,
                    clientInfo
                )
            )
            Log.d(TAG, "codesCheck response $esmResult")
            when (esmResult) {
                is EsmResult.Success -> {
                    val bundle = esmResult.data
                    bundle.classLoader = CodesCheckResponse::class.java.classLoader
                    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(
                            "key",
                            CodesCheckResponse::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        bundle.getParcelable("key")
                    }
                    _checkResult.value = Success("Успех: ${result?.toPrettyString()}")
                }

                is EsmResult.Error -> {
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