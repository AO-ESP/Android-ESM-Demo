package ru.atol.os.tspiot.api;

import ru.atol.os.tspiot.api.IBundleResultCallback;
import ru.atol.os.tspiot.api.model.MarkingVerifyRequest;

interface IMarkingManager {
    void requestCheck(in IBundleResultCallback callback, in MarkingVerifyRequest info);
}
