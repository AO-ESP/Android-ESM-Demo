package ru.atol.os.tspiot.driver.api.callback;

interface IResultCallback {
    void onSuccess();
    void onFailure(int code, in String message);
}
