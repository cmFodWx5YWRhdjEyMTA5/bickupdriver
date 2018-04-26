package com.app.bickupdriver.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>Response</H1>
 *
 * <p>Represents the POJO class or object model for Response</p>
 *
 * @author Divya Thakur
 * @since 5/12/17
 * @version 1.0
 */
public class Response implements Serializable{

    @SerializedName("date")
    public String date;
    @SerializedName("ride")
    public ArrayList<Ride> ride;
}
