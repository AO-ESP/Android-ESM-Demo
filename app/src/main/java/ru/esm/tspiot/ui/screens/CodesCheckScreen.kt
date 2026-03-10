package ru.esm.tspiot.ui.screens

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ru.atol.os.tspiot.presentation.ui.navigation.AppRoute
import ru.esm.tspiot.domain.ScanResult
import ru.esm.tspiot.ui.items.MethodSection
import ru.esm.tspiot.ui.items.NumberInputField
import ru.esm.tspiot.ui.items.ScannedMarks
import ru.esm.tspiot.ui.viewmodels.CodesCheckViewModel
import ru.esp.esm.api.model.Cis

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CodesCheckScreen(
    navController: NavHostController,
    viewModel: CodesCheckViewModel = hiltViewModel()
) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    var scanResult by rememberSaveable { mutableStateOf<Set<ScanResult>?>(null) }

    var showScanner by rememberSaveable { mutableStateOf(false) }

    var tz by rememberSaveable { mutableStateOf<Int?>(2) }
    var pg by rememberSaveable { mutableStateOf<Int?>(16) }

    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    LaunchedEffect(showScanner) {
        if (showScanner && !cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    // Наблюдение за изменениями в savedStateHandle
    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getStateFlow<Set<ScanResult>?>("scanResult", null)?.collect { result ->
            scanResult = result
        }
    }

    val checkResult by viewModel.checkResult.collectAsStateWithLifecycle()
    Log.d("CodesCheckScreen", scanResult.toString())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Проверка") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(AppRoute.id) }
                    ) { Icon(Icons.AutoMirrored.Default.ArrowBack, null) }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Кнопка запуска сканирования
            item {
                Button(
                    onClick = {
                        showScanner = true
                        if (cameraPermissionState.status.isGranted) {
                            savedStateHandle?.remove<Set<ScanResult>?>("scanResult")
                            scanResult = null
                            viewModel.clearCheckResult()
                            navController.navigate(AppRoute.ScannerScreenRoute.id) {
                                popUpTo(AppRoute.CodesCheckScreenRoute.id) {
                                    inclusive = false
                                }
                            }
                        } else {
                            navController.navigate(AppRoute.PermissionScreenRoute.id) {
                                popUpTo(AppRoute.CodesCheckScreenRoute.id) {
                                    inclusive = false
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Сканировать марку")
                }
            }
            item {
                ScannedMarks(scanResult)
            }

            scanResult?.let { results ->
                item {
                    NumberInputField(
                        value = tz,
                        onValueChange = { tz = it },
                        label = "tz"
                    )
                }
                item {
                    NumberInputField(
                        value = pg,
                        onValueChange = { pg = it },
                        label = "pg"
                    )
                }
                item {
                    MethodSection(
                        title = "Метод codesCheck (AIDL version 2)",
                        onClick = {
                            viewModel.codesCheck(
                                codes = results.map { Cis(it.text, pg) },
                                tz = tz
                            )
                        },
                        result = checkResult
                    )
                }
            }
        }
    }
}