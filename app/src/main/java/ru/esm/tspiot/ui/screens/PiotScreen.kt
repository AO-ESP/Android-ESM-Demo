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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ru.esm.tspiot.data.models.CashierInfoModel
import ru.esm.tspiot.data.models.ErrorInfoModel
import ru.esm.tspiot.data.models.ErrorRequestModel
import ru.esm.tspiot.data.models.IsmNoticeInfoModel
import ru.esm.tspiot.data.models.KktInfoModel
import ru.esm.tspiot.data.models.ReceiptImcDataModel
import ru.esm.tspiot.data.models.ReceiptInfoModel
import ru.esm.tspiot.ui.navigation.AppRoute
import ru.esm.tspiot.ui.navigation.LocalNavController
import ru.esm.tspiot.ui.navigation.createPreviewNavController
import ru.esm.tspiot.ui.viewmodels.MainViewModel

@Preview
@Composable
fun PiotScreenPreview() {
    PiotScreenContent(
        navController = createPreviewNavController(),
        isConnected = true,
        {},
        {},
        "ServicePackage",
        { _, _ -> },
        { _, _, _ -> },
        { _, _ -> },
        { _, _ -> },
        { _ -> },
        { _, _ -> },
        { _ -> },
        { _, _ -> },
    )
}

@Composable
fun PiotScreen(
    navController: NavHostController = LocalNavController.current,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val isConnected by viewModel.isPiotManagerConnected.collectAsStateWithLifecycle()
    val servicePackage = viewModel.getServicePackage()

    PiotScreenContent(
        navController,
        isConnected,
        onConnect = { viewModel.connectToPiotManager() },
        onDisconnect = { viewModel.disconnectFromPiotManager() },
        servicePackage,
        onSetShiftStateAction = { isClosed, kktInfo ->
            viewModel.setShiftState(isClosed, kktInfo)
        },
        onSetImcDataAction = { imcData, kktInfo, isOnline ->
            viewModel.setImcData(imcData, kktInfo, isOnline)
        },
        onSetErrorAction = { request, kktInfo ->
            viewModel.setError(request, kktInfo)
        },
        onSetIsmNoticeAction = { info, kktInfo ->
            viewModel.setIsmNotice(info, kktInfo)
        },
        onSetRawEventAction = { event ->
            viewModel.setRawEvent(event)
        },
        onSetReceiptInfoModelAction = { info, kktInfo ->
            viewModel.setReceiptInfo(info, kktInfo)
        },
        onSetKktInfoModelAction = { kktInfo ->
            viewModel.setKktInfo(kktInfo)
        },
        onSetCashierAction = { cashierInfo, kktInfo ->
            viewModel.setCashier(cashierInfo, kktInfo)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PiotScreenContent(
    navController: NavHostController,
    isConnected: Boolean,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    servicePackage: String,
    onSetShiftStateAction: (
        isClosed: Boolean,
        KktInfoModel: KktInfoModel,
    ) -> Unit,
    onSetImcDataAction: (
        imcData: String,
        KktInfoModel: KktInfoModel,
        isOnline: Boolean
    ) -> Unit,
    onSetErrorAction: (
        request: ErrorRequestModel,
        KktInfoModel: KktInfoModel
    ) -> Unit,
    onSetIsmNoticeAction: (
        info: IsmNoticeInfoModel,
        KktInfoModel: KktInfoModel
    ) -> Unit,
    onSetRawEventAction: (
        event: String
    ) -> Unit,
    onSetReceiptInfoModelAction: (
        info: ReceiptInfoModel,
        KktInfoModel: KktInfoModel
    ) -> Unit,
    onSetKktInfoModelAction: (
        KktInfoModel: KktInfoModel
    ) -> Unit,
    onSetCashierAction: (
        CashierInfoModel: CashierInfoModel,
        KktInfoModel: KktInfoModel
    ) -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ПИоТ менеджер") },
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
                    onSetShiftStateAction(
                        true,
                        getDefaultKktInfoModel()
                    )
                }
            ) {
                Text("Set Shift State")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = {
                    onSetImcDataAction(
                        "imcData",
                        getDefaultKktInfoModel(),
                        true
                    )
                }
            ) {
                Text("Set Imc Data")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = {
                    onSetErrorAction(
                        ErrorRequestModel(
                            listOf(
                                ErrorInfoModel(
                                    0,
                                    "message",
                                    "module",
                                    "type"
                                )
                            )
                        ),
                        getDefaultKktInfoModel()
                    )
                }
            ) {
                Text("Set Error")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = {
                    onSetIsmNoticeAction(
                        IsmNoticeInfoModel(
                            "issueDate",
                            "sendDate",
                            123,
                            "receiptId"
                        ),
                        getDefaultKktInfoModel()
                    )
                }
            ) {
                Text("Set Ism Notice")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = {
                    onSetRawEventAction("event")
                }
            ) {
                Text("Set Raw Event")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = {
                    onSetReceiptInfoModelAction(
                        ReceiptInfoModel(
                            "receiptId",
                            listOf(
                                ReceiptImcDataModel(
                                    "ki",
                                    "ofdStatus"
                                )
                            )
                        ),
                        getDefaultKktInfoModel()
                    )
                }
            ) {
                Text("Set Receipt Info")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = {
                    onSetKktInfoModelAction(
                        getDefaultKktInfoModel()
                    )
                }
            ) {
                Text("Set Kkt Info")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected,
                onClick = {
                    onSetCashierAction(
                        CashierInfoModel(
                            "info",
                            "inn"
                        ),
                        getDefaultKktInfoModel()
                    )
                }
            ) {
                Text("Set Cashier")
            }
        }
    }
}

private fun getDefaultKktInfoModel(): KktInfoModel {
    return KktInfoModel(
        "9999078902019459",
        "9717169631",
        "177044026786",
        "firmwareVersion",
        "1.2",
    )
}