package com.app.bickupdriver.model;

import com.app.bickupdriver.utility.ConstantValues;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by karun on 7/12/17.
 */

public class RideStatusResponse implements Serializable {

    @SerializedName(ConstantValues.MESSAGE)
    public String message;

    @SerializedName(ConstantValues.FLAG)
    public int flag;

    @SerializedName(ConstantValues.RESPONSE)
    public Temp temp;

}
