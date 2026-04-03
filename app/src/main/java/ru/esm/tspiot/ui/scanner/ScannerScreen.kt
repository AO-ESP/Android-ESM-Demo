package ru.esm.tspiot.ui.scanner

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ru.esm.tspiot.domain.ScanResult
import ru.esm.tspiot.ui.navigation.LocalNavController
import ru.esm.tspiot.ui.navigation.createPreviewNavController

@Preview
@Composable
fun ScannerScreenPreview(){
    ScannerScreenContent(
        navController = createPreviewNavController(),
        onBarcodeScannedAction = { _, _ -> },
        onBarcodeScannedErrorAction = { _ -> },
        scanResult = setOf(ScanResult("text", "hh:mm")),
        isScanning = true,
        onScanAction = { },
        onClearResultAction = { },
        errorMessage = null,
        onMessageErrorClearAction = { }
    )
}

@Composable
fun ScannerScreen(
    navController: NavHostController = LocalNavController.current,
    viewModel: ScannerViewModel = hiltViewModel()
) {

    val scanResult by viewModel.scanResult.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()

    ScannerScreenContent(
        navController = navController,
        onBarcodeScannedAction = { text, format ->
            viewModel.onScanResult(
                ScanResult(
                    if (text.startsWith("\\u")) text.substring(2) else text,
                    format
                )
            )
        },
        onBarcodeScannedErrorAction = { error ->
            viewModel.onScanError(error)
        },
        scanResult = scanResult,
        isScanning = isScanning,
        onScanAction = { viewModel.scan() },
        onClearResultAction = { viewModel.clearResult() },
        errorMessage = viewModel.errorMessage.value,
        onMessageErrorClearAction = { viewModel.errorMessage.value = null }
    )
}

@Composable
fun ScannerScreenContent(
    navController: NavHostController = LocalNavController.current,
    onBarcodeScannedAction: (String, String) -> Unit,
    onBarcodeScannedErrorAction: (String) -> Unit,
    scanResult: Set<ScanResult>?,
    isScanning: Boolean,
    onScanAction: () -> Unit,
    onClearResultAction: () -> Unit,
    errorMessage: String?,
    onMessageErrorClearAction: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            onBarcodeScanned = { text, format ->
                onBarcodeScannedAction(text, format)
            },
            onError = { error ->
                onBarcodeScannedErrorAction(error)
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(250.dp)
            ) {
                drawRect(
                    color = Color.Transparent,
                    size = size
                )
                drawRect(
                    color = Color.Green.copy(alpha = 0.3f),
                    size = size,
                    style = Stroke(width = 4f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Назад")
            }

            if (isScanning) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }
        }

        if (!isScanning) {
            scanResult?.let { marks ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {
                                Text(
                                    text = "Отсканировано:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            marks.forEachIndexed { index, item ->
                                item {
                                    Text(
                                        text = "${index + 1}. ${item.text}",
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(4.dp)
                                    )
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            item {
                                Row(
                                    Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(
                                        modifier = Modifier
                                            .padding(end = 2.dp)
                                            .weight(1f),
                                        onClick = onScanAction
                                    ) {
                                        Text("Добавить")
                                    }

                                    Button(
                                        modifier = Modifier
                                            .padding(start = 2.dp)
                                            .weight(1f),
                                        onClick = {
                                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                                "scanResult",
                                                scanResult
                                            )
                                            navController.popBackStack()
                                        },
                                    ) {
                                        Text("Готово")
                                    }
                                }
                            }
                            item {
                                Button(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth(),
                                    onClick = onClearResultAction
                                ) {
                                    Text("Очистить")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

        // Error overlay
    errorMessage?.let { error ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Scan Error",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = error,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onMessageErrorClearAction
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }
        }
    }