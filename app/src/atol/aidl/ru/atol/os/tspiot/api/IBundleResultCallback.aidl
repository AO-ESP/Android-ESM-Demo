package ru.atol.os.tspiot.api;

import android.os.Bundle;

interface IBundleResultCallback {
    void onSuccess(in Bundle bundle);
    void onError(in int code, in String message);
}
