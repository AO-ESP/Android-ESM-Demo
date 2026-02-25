package ru.esm.tspiot.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import ru.esm.tspiot.ui.items.MethodSection
import ru.esm.tspiot.ui.scanner.ScannerViewModel
import ru.esm.tspiot.ui.viewmodels.LmViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LmScreen(
    navController: NavHostController,
    scanner: ScannerViewModel = hiltViewModel(),
    viewModel: LmViewModel = hiltViewModel()
) {
    var showScanner by remember { mutableStateOf(false) }
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )
    val scanResult = scanner.scanResult.collectAsState().value

    LaunchedEffect(showScanner) {
        if (showScanner && !cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    val cisSellResult by viewModel.cisSellResult.collectAsStateWithLifecycle()
    val cisReturnResult by viewModel.cisReturnResult.collectAsStateWithLifecycle()
    val cisSoldResult by viewModel.cisSoldResult.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ЛМ ЧЗ") },
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
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Марка", style = MaterialTheme.typography.titleMedium)
                    scanResult?.text?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
            scanResult?.text?.let {
                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        MethodSection(
                            title = "CIS Sell",
                            onClick = { viewModel.cisSell(listOf(it)) },
                            result = cisSellResult
                        )
                    }
                }
            }
            scanResult?.text?.let {
                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        MethodSection(
                            title = "CIS Return",
                            onClick = { viewModel.cisReturn(listOf(it)) },
                            result = cisReturnResult
                        )
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    MethodSection(
                        title = "CIS Sold (skip=0, limit=10)",
                        onClick = { viewModel.cisSold(0, 10) },
                        result = cisSoldResult
                    )
                }
            }
        }
    }
}