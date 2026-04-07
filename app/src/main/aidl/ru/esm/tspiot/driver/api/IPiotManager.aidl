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
    int getAidlVersion();
    void setShiftState(in IBoolCallback callback, in boolean isClosed, in KktInfo kktInfo);
    void setImcData(in IResultCallback callback, in String imcData, in KktInfo kktInfo, in boolean isOnline);
    void setError(in IResultCallback callback, in ErrorRequest request, in KktInfo kktInfo);
    void setIsmNotice(in IResultCallback callback, in IsmNoticeInfo info, in KktInfo kktInfo);
    void setRawEvent(in IResultCallback callback, in String event);
    void setReceiptInfo(in IResultCallback callback, in ReceiptInfo info, in KktInfo kktInfo);
    void setKktInfo(in IResultCallback callback, in KktInfo kktInfo);
    void setCashier(in IResultCallback callback, in CashierInfo cashierInfo, in KktInfo kktInfo);
}
