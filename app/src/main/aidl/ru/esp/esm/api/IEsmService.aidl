package ru.esp.esm.api;

import ru.esp.esm.api.IBundleResultCallback;
import ru.esp.esm.api.model.CodesCheckRequest;

interface IEsmService {
    int getAidlVersion();
    void codesCheck(in IBundleResultCallback callback, in CodesCheckRequest codes);
    void cisSell(in IBundleResultCallback callback, in List<String> cisList);
    void cisReturn(in IBundleResultCallback callback, in List<String> cisList);
    void cisSold(in IBundleResultCallback callback, int skip, int limit);
    void getInfo(in IBundleResultCallback callback);
}