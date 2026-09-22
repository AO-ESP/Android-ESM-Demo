package ru.esm.tspiot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ru.esm.tspiot.domain.PiotResult
import ru.esm.tspiot.ui.items.ResultDisplay
import ru.esm.tspiot.ui.navigation.AppRoute
import ru.esm.tspiot.ui.navigation.LocalNavController
import ru.esm.tspiot.ui.navigation.createPreviewNavController
import ru.esm.tspiot.ui.viewmodels.EanViewModel
import ru.esm.tspiot.driver.api.model.ean.EanCheckItem
import ru.esm.tspiot.driver.api.model.ean.EanCheckRequest

@Preview
@Composable
fun EanScreenPreview() {
    EanScreenContent(
        navController = createPreviewNavController(),
        isConnected = true,
        eanCheckResult = null,
        onConnect = {},
        onDisconnect = {},
        onEanCheck = {},
    )
}

@Composable
fun EanScreen(
    navController: NavHostController = LocalNavController.current,
    viewModel: EanViewModel = hiltViewModel(),
) {
    val isConnected by viewModel.isPiotManagerConnected.collectAsStateWithLifecycle()
    val eanCheckResult by viewModel.eanCheckResult.collectAsStateWithLifecycle()

    EanScreenContent(
        navController = navController,
        isConnected = isConnected,
        eanCheckResult = eanCheckResult,
        onConnect = { viewModel.connectToPiotManager() },
        onDisconnect = { viewModel.disconnectFromPiotManager() },
        onEanCheck = { viewModel.eanCheck(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EanScreenContent(
    navController: NavHostController,
    isConnected: Boolean,
    eanCheckResult: PiotResult<String>?,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onEanCheck: (EanCheckRequest) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("EAN") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(AppRoute.id) }
                    ) { Icon(Icons.AutoMirrored.Default.ArrowBack, null) }
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
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onConnect
            ) {
                Text("Подключиться")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDisconnect
            ) {
                Text("Отключиться")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = {
                    onEanCheck(
                        EanCheckRequest(
                            listOf(
                                EanCheckItem("1", "4690228020056"),
                                EanCheckItem("3", "4600682003847"),
                            )
                        )
                    )
                }
            ) {
                Text("EAN Check")
            }
            ResultDisplay(eanCheckResult)
        }
    }
}
