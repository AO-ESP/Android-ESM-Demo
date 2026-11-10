package ru.esm.tspiot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.esm.tspiot.ui.navigation.AppRoute
import ru.esm.tspiot.ui.navigation.LocalNavController
import ru.esm.tspiot.ui.navigation.createPreviewNavController

@Preview
@Composable
fun MainScreenPreview() {
    MainScreenContent(
        navController = createPreviewNavController(),
    )
}

@Composable
fun MainScreen(
    navController: NavHostController = LocalNavController.current,
) {
    MainScreenContent(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    navController: NavHostController,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ESM Demo") },
                navigationIcon = {
                    IconButton(
                        onClick = { (context as? android.app.Activity)?.finish() },
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ExitToApp,
                            contentDescription = "Выход",
                            modifier = Modifier.rotate(180f)
                        )
                    }
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
                onClick = { navController.navigate(AppRoute.MainScanScreenRoute.id) }
            ) {
                Text("Работа с марками")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(AppRoute.MainPiotScreenRoute.id) }
            ) {
                Text("Работа с событиями")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(AppRoute.EanScreenRoute.id) }
            ) {
                Text("Проверка EAN через ДККТ")
            }
        }
    }
}
