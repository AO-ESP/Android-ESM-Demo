package ru.atol.os.tspiot.driver.api.callback;

interface IBoolCallback {
    void onSuccess(boolean status);
    void onFailure(int code, in String message);
}
