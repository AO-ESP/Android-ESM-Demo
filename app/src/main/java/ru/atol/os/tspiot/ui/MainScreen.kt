package ru.atol.os.tspiot.ui

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ru.atol.os.tspiot.ui.scanner.PermissionScreen
import ru.atol.os.tspiot.ui.scanner.ScannerScreen
import ru.esm.tspiot.MainViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "mainScreen"
    ) {
        composable("mainScreen") {
            MainScreen(
                cameraPermissionState = cameraPermissionState,
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("scannerScreen") {
            ScannerScreen(
                viewModel = viewModel
            ) {
                navController.navigate("mainScreen")
            }
        }
        composable("permissionScreen") {
            PermissionScreen(
                permissionState = cameraPermissionState
            ) {
                navController.navigate("mainScreen")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    cameraPermissionState: PermissionState,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scanResult = viewModel.scanResult.collectAsState(null).value
    var showScanner by remember { mutableStateOf(false) }

    LaunchedEffect(showScanner) {
        if (showScanner && !cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ESM Demo") },
                navigationIcon = {
                    IconButton(
                        onClick = { (context as? Activity)?.finish() }
                    ) { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Статус подключения
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        uiState.isConnected -> MaterialTheme.colorScheme.primaryContainer
                        uiState.isConnecting -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Статус подключения",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.connectionStatus,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Кнопки управления
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.connectService(context) },
                    enabled = !uiState.isConnected && !uiState.isConnecting,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Подключить")
                }

                Button(
                    onClick = { viewModel.disconnectService(context) },
                    enabled = uiState.isConnected,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Отключить")
                }
            }

            // Кнопка запуска сканирования
            Button(
                onClick = {
                    showScanner = true
                    if (cameraPermissionState.status.isGranted) {
                        navController.navigate("scannerScreen") {
                            popUpTo("mainScreen") {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate("permissionScreen") {
                            popUpTo("mainScreen") {
                                inclusive = true
                            }
                        }
                    }
                },
                enabled = uiState.isConnected,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сканировать марку")
            }

            // Кнопка тестового запроса
            Button(
                onClick = { viewModel.testRequest(scanResult?.text) },
                enabled = uiState.isConnected && scanResult != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                val scannedMark =
                    if (scanResult != null) scanResult.text else "Марка не отсканирована"
                Text("Выполнить тестовый запрос\n$scannedMark")
            }

            // Результат проверки
            if (uiState.isValid != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (uiState.isValid == true) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Ответ ТС ПИоТ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (uiState.isValid == true) "✓ Success" else "✗ Error",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            // Последнее действие
            if (uiState.lastAction.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Последнее действие",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.lastAction,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Результат
            if (uiState.lastResult.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Результат",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.lastResult,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Ошибка
            if (uiState.lastError.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Ошибка",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.lastError,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Информация о сервисе
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Информация о сервисе",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Service: IMarkingManager")
                    Text("Method: requestCheck")
                    Text("Callback: IBundleResultCallback")
                }
            }
        }
    }
}