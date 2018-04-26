package com.app.bickupdriver.model;

import com.app.bickupdriver.utility.ConstantValues;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>RideArrayResponse</H1>
 *
 * <p>Represents the POJO class or object model for Server Result</p>
 *
 * @author Divya Thakur
 * @since 5/11/17
 * @version 1.0
 */
public class RideArrayResponse implements Serializable {

    @SerializedName(ConstantValues.MESSAGE)
    public String message;

    @SerializedName(ConstantValues.FLAG)
    public int flag;

    @SerializedName(ConstantValues.RESPONSE)
    public ArrayList<Ride> response;
}
