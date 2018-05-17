package com.app.bickupdriver.restservices;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by manish on 15/5/18.
 */

public class ServerResponseNotificationList {
    @SerializedName("response")
    public ArrayList<NotificationListModel> response;

    @SerializedName("message")
    public String message;

    @SerializedName("flag")
    public int flag;
}
