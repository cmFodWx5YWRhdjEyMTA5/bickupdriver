package com.app.bickupdriver.controller;

import com.google.gson.JsonObject;

/**
 * Created by fluper on 5/9/17.
 */

public interface NetworkCallBack<E> {

    public void onSuccess(JsonObject data, int requestCode, int statusCode);

    public void onError(String msg);
}