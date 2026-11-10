package ru.esm.tspiot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ru.esm.tspiot.domain.PiotResult
import ru.esm.tspiot.driver.api.model.ean.EanCheckItem
import ru.esm.tspiot.driver.api.model.ean.EanCheckRequest
import ru.esm.tspiot.ui.items.ResultDisplay
import ru.esm.tspiot.ui.navigation.AppRoute
import ru.esm.tspiot.ui.navigation.LocalNavController
import ru.esm.tspiot.ui.navigation.createPreviewNavController
import ru.esm.tspiot.ui.viewmodels.EanViewModel

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
    val drafts = remember { mutableStateListOf<EanDraft>().apply { addAll(defaultEanDrafts()) } }
    var nextKey by remember { mutableIntStateOf(defaultEanDrafts().size + 1) }
    val canCheck = drafts.any { it.gtin.isNotBlank() }

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
            if (!isConnected) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onConnect
                ) {
                    Text("Подключиться")
                }
            } else {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onDisconnect
                ) {
                    Text("Отключиться")
                }
                drafts.forEach { draft ->
                    key(draft.key) {
                        EanDraftRow(
                            itemId = draft.itemId,
                            gtin = draft.gtin,
                            onItemIdChange = { value ->
                                updateDraft(drafts, draft.key) { it.copy(itemId = value) }
                            },
                            onGtinChange = { value ->
                                updateDraft(drafts, draft.key) { it.copy(gtin = value) }
                            },
                            onRemove = { drafts.removeAll { it.key == draft.key } },
                        )
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        drafts.add(EanDraft(key = nextKey, itemId = "", gtin = ""))
                        nextKey += 1
                    }
                ) {
                    Text("Добавить")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = canCheck,
                    onClick = {
                        val items = drafts
                            .filter { it.gtin.isNotBlank() }
                            .map { EanCheckItem(it.itemId.trim(), it.gtin.trim()) }
                        onEanCheck(EanCheckRequest(items))
                    }
                ) {
                    Text("EAN Check")
                }
                ResultDisplay(eanCheckResult)
            }
        }
    }
}

@Composable
private fun EanDraftRow(
    itemId: String,
    gtin: String,
    onItemIdChange: (String) -> Unit,
    onGtinChange: (String) -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = itemId,
                onValueChange = onItemIdChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("itemId") },
                singleLine = true,
            )
            OutlinedTextField(
                value = gtin,
                onValueChange = onGtinChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("EAN") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
        TextButton(onClick = onRemove) {
            Text("Удалить")
        }
    }
}

private data class EanDraft(
    val key: Int,
    val itemId: String,
    val gtin: String,
)

private fun defaultEanDrafts(): List<EanDraft> = listOf(
    EanDraft(key = 1, itemId = "1", gtin = "4690228020056"),
    EanDraft(key = 2, itemId = "3", gtin = "4600682003847"),
)

private fun updateDraft(
    drafts: MutableList<EanDraft>,
    key: Int,
    transform: (EanDraft) -> EanDraft,
) {
    val index = drafts.indexOfFirst { it.key == key }
    if (index >= 0) {
        drafts[index] = transform(drafts[index])
    }
}
