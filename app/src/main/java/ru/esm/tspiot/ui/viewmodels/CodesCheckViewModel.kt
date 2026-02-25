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
import ru.esp.esm.api.model.ClientInfo
import ru.esp.esm.api.model.CodesCheckRequest
import ru.esp.esm.api.model.CodesCheckResponse
import toPrettyString
import javax.inject.Inject

@HiltViewModel
class CodesCheckViewModel @Inject constructor(
    private val esmServiceClient: EsmServiceClient
) : ViewModel() {
    private val _resultV1 = MutableStateFlow<EsmResult<String>?>(null)
    val resultV1: StateFlow<EsmResult<String>?> = _resultV1

    private val _resultV2 = MutableStateFlow<EsmResult<String>?>(null)
    val resultV2: StateFlow<EsmResult<String>?> = _resultV2

    val clientInfo = ClientInfo(
        "ESM Test",
        "1.0",
        "90911ffe-47da-4a71-86bb-be455f3d9614",
        "90911ffe-47da-4a71-86bb-be455f3d9614",
        null
    )

    fun codesCheck(codes: List<String>) {
        viewModelScope.launch {
            Log.d(TAG, "codesCheck called")
            val esmResult = esmServiceClient.codesCheck(
                CodesCheckRequest(
                    codes,
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
                    _resultV2.value = null
                    _resultV1.value = EsmResult.Success("Успех: ${result?.toPrettyString()}")
                }

                is EsmResult.Error -> {
                    _resultV1.value = EsmResult.Error(esmResult.code, esmResult.message)
                }

                EsmResult.ServiceUnavailable -> {
                    _resultV1.value = EsmResult.ServiceUnavailable
                }
            }
        }
    }

    fun requestCheck(codes: List<String>) {
        viewModelScope.launch {
            Log.d(TAG, "requestCheck called")
//            val esmResult = esmServiceClient.codesCheck(
//                CodesCheckRequest(
//                    codes,
//                    clientInfo
//                )
//            )
//            Log.d(TAG, "requestCheck response $esmResult")
//            when (esmResult) {
//                is EsmResult.Success -> {
//                    val bundle = esmResult.data
//                    bundle.classLoader = CodesCheckResponse::class.java.classLoader
//                    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                        bundle.getParcelable(
//                            "key",
//                            CodesCheckResponse::class.java
//                        )
//                    } else {
//                        @Suppress("DEPRECATION")
//                        bundle.getParcelable("key")
//                    }
//                    _resultV2.value = null
//                    _resultV1.value = EsmResult.Success("Успех: ${result?.toPrettyString()}")
//                }
//
//                is EsmResult.Error -> {
//                    _resultV1.value = EsmResult.Error(esmResult.code, esmResult.message)
//                }
//
//                EsmResult.ServiceUnavailable -> {
//                    _resultV1.value = EsmResult.ServiceUnavailable
//                }
//            }
        }
    }

    companion object {
        private const val TAG = "CodesCheckViewModel"
    }
}