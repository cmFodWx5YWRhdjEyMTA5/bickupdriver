package com.app.bickupdriver.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by karun on 7/12/17.
 */

public class Status implements Serializable {

    @SerializedName("timestamp")
    public String timeStamp;
    @SerializedName("status")
    public int status;
}
