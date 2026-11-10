package ru.esm.tspiot.api;

import ru.esm.tspiot.api.IBundleResultCallback;
import ru.esm.tspiot.api.model.MarkingVerifyRequest;

interface IMarkingManager {
    void requestCheck(in IBundleResultCallback callback, in MarkingVerifyRequest info);
}
