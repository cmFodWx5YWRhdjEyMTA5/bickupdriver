package com.app.bickupdriver.restservices;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by manish on 15/5/18.
 */

public class NotificationListModel {

    @SerializedName("_id")
    public String id;


    /*@SerializedName("driver_id")
    public ArrayList<String> driverId;*/

    @SerializedName("user_id")
    public String userId;

    @SerializedName("timestamp")
    public long timeStamp;

    @SerializedName("notification_type")
    public String notificationType;

    @SerializedName("payload")
    public NotificationListModel payload;

    @SerializedName("notify")
    public NotificationListModel notify;

    @SerializedName("ride_id")
    public String rideId;

    @SerializedName("title'")
    public String title;

    @SerializedName("message")
    public String notificationText;

    @SerializedName("click_action")
    public String clickAction;

    @SerializedName("color")
    public String color;
}
