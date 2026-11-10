package ru.esm.tspiot.driver.api.callback;

interface IBoolCallback {
    void onSuccess(boolean status);
    void onFailure(int code, in String message);
}
