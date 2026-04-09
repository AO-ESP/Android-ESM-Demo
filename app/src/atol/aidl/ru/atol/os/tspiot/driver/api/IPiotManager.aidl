package ru.atol.os.tspiot.driver.api;

import ru.atol.os.tspiot.driver.api.callback.IResultCallback;
import ru.atol.os.tspiot.driver.api.callback.IBoolCallback;
import ru.atol.os.tspiot.driver.api.model.IsmNoticeInfo;
import ru.atol.os.tspiot.driver.api.model.ErrorRequest;
import ru.atol.os.tspiot.driver.api.model.CashierInfo;
import ru.atol.os.tspiot.driver.api.model.KktInfo;
import ru.atol.os.tspiot.driver.api.model.ReceiptImcData;
import ru.atol.os.tspiot.driver.api.model.ReceiptInfo;

interface IPiotManager {
    void setShiftState(in IBoolCallback callback, in boolean isClosed, in KktInfo kktInfo);
    void setImcData(in IResultCallback callback, in String imcData, in KktInfo kktInfo, in boolean isOnline);
    void setError(in IResultCallback callback, in ErrorRequest request, in KktInfo kktInfo);
    void setIsmNotice(in IResultCallback callback, in IsmNoticeInfo info, in KktInfo kktInfo);
    void setRawEvent(in IResultCallback callback, in String event);
    void setReceiptInfo(in IResultCallback callback, in ReceiptInfo info, in KktInfo kktInfo);
    void setKktInfo(in IResultCallback callback, in KktInfo kktInfo);
    void setCashier(in IResultCallback callback, in CashierInfo cashierInfo, in KktInfo kktInfo);
}
