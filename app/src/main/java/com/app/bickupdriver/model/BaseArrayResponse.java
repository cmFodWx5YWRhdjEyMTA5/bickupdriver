package com.app.bickupdriver.model;

import com.app.bickupdriver.utility.ConstantValues;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Divya Thakur on 11/12/17.
 */

public class BaseArrayResponse implements Serializable {

    @SerializedName(ConstantValues.MESSAGE)
    public String message;

    @SerializedName(ConstantValues.FLAG)
    public int flag;

    @SerializedName(ConstantValues.RESPONSE)
    public ArrayList<Response> response;
}
