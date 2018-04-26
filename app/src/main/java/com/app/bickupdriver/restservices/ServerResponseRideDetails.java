package com.app.bickupdriver.restservices;

import com.app.bickupdriver.model.Ride;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by manish on 7/4/18.
 */

public class ServerResponseRideDetails {

    @SerializedName("response")
    public ArrayList<Ride> rides;


    @SerializedName("message")
    public String message;


    @SerializedName("flag")
    public int status;


}
