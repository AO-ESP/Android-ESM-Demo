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
import ru.esp.esm.api.model.lm.LmRegisterResponse
import ru.esp.esm.api.model.lm.LmSoldCommodities
import toPrettyString
import javax.inject.Inject

@HiltViewModel
class LmViewModel @Inject constructor(
    private val esmServiceClient: EsmServiceClient
) : ViewModel() {

    private val _cisSellResult = MutableStateFlow<EsmResult<String>?>(null)
    val cisSellResult: StateFlow<EsmResult<String>?> = _cisSellResult

    private val _cisReturnResult = MutableStateFlow<EsmResult<String>?>(null)
    val cisReturnResult: StateFlow<EsmResult<String>?> = _cisReturnResult

    private val _cisSoldResult = MutableStateFlow<EsmResult<String>?>(null)
    val cisSoldResult: StateFlow<EsmResult<String>?> = _cisSoldResult

    fun cisSell(cisList: List<String>) {
        viewModelScope.launch {
            Log.d(TAG, "cisSell called")

            _cisSellResult.value = null
            _cisReturnResult.value = null
            _cisSoldResult.value = null

            val esmResult = try {
                esmServiceClient.cisSell(cisList)
            } catch (e: Exception) {
                EsmResult.Error(777, e.message)
            }
            Log.d(TAG, "cisSell response $esmResult")
            when (esmResult) {
                is EsmResult.Success -> {
                    val bundle = esmResult.data
                    bundle.classLoader = LmRegisterResponse::class.java.classLoader
                    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(
                            "key",
                            LmRegisterResponse::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        bundle.getParcelable("key")
                    }

                    _cisSellResult.value =
                        EsmResult.Success("Успех: ${result?.toPrettyString()}")
                }

                is EsmResult.Error -> {
                    _cisSellResult.value = EsmResult.Error(esmResult.code, esmResult.message)
                }

                EsmResult.ServiceUnavailable -> {
                    _cisSellResult.value = EsmResult.ServiceUnavailable
                }
            }
        }
    }

    fun cisReturn(cisList: List<String>) {
        viewModelScope.launch {
            Log.d(TAG, "cisReturn called")

            _cisSellResult.value = null
            _cisReturnResult.value = null
            _cisSoldResult.value = null

            val esmResult = try {
                esmServiceClient.cisReturn(cisList)
            } catch (e: Exception) {
                EsmResult.Error(777, e.message)
            }
            Log.d(TAG, "cisReturn response $esmResult")
            when (esmResult) {
                is EsmResult.Success -> {
                    val bundle = esmResult.data
                    bundle.classLoader = LmRegisterResponse::class.java.classLoader
                    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(
                            "key",
                            LmRegisterResponse::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        bundle.getParcelable("key")
                    }

                    _cisReturnResult.value =
                        EsmResult.Success("Успех: ${result?.toPrettyString()}")
                }

                is EsmResult.Error -> {
                    _cisReturnResult.value = EsmResult.Error(esmResult.code, esmResult.message)
                }

                EsmResult.ServiceUnavailable -> {
                    _cisReturnResult.value = EsmResult.ServiceUnavailable
                }
            }
        }
    }

    fun cisSold(skip: Int, limit: Int) {
        viewModelScope.launch {
            Log.d(TAG, "cisSold called")

            _cisSellResult.value = null
            _cisReturnResult.value = null
            _cisSoldResult.value = null
            
            val esmResult = esmServiceClient.cisSold(skip, limit)
            Log.d(TAG, "cisSold response $esmResult")
            when (esmResult) {
                is EsmResult.Success -> {
                    val bundle = esmResult.data
                    bundle.classLoader = LmSoldCommodities::class.java.classLoader
                    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(
                            "key",
                            LmSoldCommodities::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        bundle.getParcelable("key")
                    }

                    _cisSoldResult.value =
                        EsmResult.Success("Успех: ${result?.toPrettyString()}")
                }

                is EsmResult.Error -> {
                    _cisSoldResult.value = EsmResult.Error(esmResult.code, esmResult.message)
                }

                EsmResult.ServiceUnavailable -> {
                    _cisSoldResult.value = EsmResult.ServiceUnavailable
                }
            }
        }
    }

    companion object {
        private const val TAG = "LmViewModel"
    }
}