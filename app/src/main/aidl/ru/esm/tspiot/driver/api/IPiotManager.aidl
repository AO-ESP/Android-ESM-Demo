package ru.esm.tspiot.driver.api;

import ru.esm.tspiot.driver.api.callback.IBoolCallback;
import ru.esm.tspiot.driver.api.callback.IResultCallback;
import ru.esm.tspiot.driver.api.model.KktInfo;
import ru.esm.tspiot.driver.api.model.CashierInfo;
import ru.esm.tspiot.driver.api.model.ErrorInfo;
import ru.esm.tspiot.driver.api.model.ErrorRequest;
import ru.esm.tspiot.driver.api.model.IsmNoticeInfo;
import ru.esm.tspiot.driver.api.model.ReceiptInfo;
import ru.esm.tspiot.driver.api.model.CashierInfo;

interface IPiotManager {
    // Состояние смены
    void setShiftState(in IBoolCallback callback, in boolean isClosed, in KktInfo kktInfo);
    // Информация от ДККТ о вызовах методов работы с КМ
    void setImcData(in IResultCallback callback, in String imcData, in KktInfo kktInfo, in boolean isOnline);
    // Ошибка
    void setError(in IResultCallback callback, in ErrorRequest request, in KktInfo kktInfo);
    // Данные об уведомлениях ОФД/ИСМ
    void setIsmNotice(in IResultCallback callback, in IsmNoticeInfo info, in KktInfo kktInfo);
    // Ивент
    void setRawEvent(in IResultCallback callback, in String event);
    // Данные о марках (кодах идентификации) в чеке
    void setReceiptInfo(in IResultCallback callback, in ReceiptInfo info, in KktInfo kktInfo);
    // Информация о ККТ и ДККТ
    void setKktInfo(in IResultCallback callback, in KktInfo kktInfo);
    // Информация о кассире
    void setCashier(in IResultCallback callback, in CashierInfo cashierInfo, in KktInfo kktInfo);
}
