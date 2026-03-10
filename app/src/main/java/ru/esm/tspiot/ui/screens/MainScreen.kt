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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ru.atol.os.tspiot.presentation.ui.navigation.AppRoute
import ru.esm.tspiot.ui.items.ServiceConnectCard
import ru.esm.tspiot.ui.items.ServiceInfoCard
import ru.esm.tspiot.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ESM Demo") },
                navigationIcon = {
                    IconButton(
                        onClick = { (context as? android.app.Activity)?.finish() }
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
            // Информация о сервисе
            ServiceInfoCard(viewModel.getServicePackage())
            ServiceConnectCard(viewModel)

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = { navController.navigate(AppRoute.CodesCheckScreenRoute.id) }
            ) {
                Text("Проверка марок")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = { navController.navigate(AppRoute.LmScreenRoute.id) }
            ) {
                Text("Работа с ЛМ ЧЗ")
            }
        }
    }
}