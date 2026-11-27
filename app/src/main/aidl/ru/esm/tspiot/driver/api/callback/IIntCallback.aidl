package ru.esm.tspiot.driver.api.callback;

interface IIntCallback {
    void onSuccess(int value);
    void onFailure(int code, in String message);
}
