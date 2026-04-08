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
                        "imcData", // информация о кодах маркировки в чеке
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
                                    0, // код ошибки
                                    "message", // описание ошибки
                                    "module", // источник ошибки (ДККТ/ККТ/ФН)
                                    "type" // вид ошибки
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
                        // Событие формирования и отправки уведомления в ОФД/ГИС МТ
                        IsmNoticeInfoModel(
                            "issueDate", // дата и время формирования уведомления в формате YYYY-MM-DD hh:mm:ss
                            "sendDate", // дата и время отправки уведомления в ОФД/ГИС МТ в формате YYYY-MM-DD hh:mm:ss
                            123, // номер уведомления
                            "receiptId" // номер чека
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
                    onSetRawEventAction("event") // Событие эвента
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
                            "receiptId", // номер чека
                            listOf(
                                ReceiptImcDataModel(
                                    "ki", // номер ФД в ФН
                                    "ofdStatus" // статус ОФД
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
                            "Мариваннна", // ФИО пользователя ТС ПИоТ
                            "9826267492" // ИНН пользователя ТС ПИоТ
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
        "9999078902019459", // Серийный номер ФН (Фискальный накопитель)
        "9717169631", // ИНН Владельца
        "00109428623100", // Серийный номер ККТ (Контрольно кассовая техника)
        "5.10.50", // Версия прошивки ККТ
        "n 1.2 mgm-p 11", // Версия ФН
    )
}