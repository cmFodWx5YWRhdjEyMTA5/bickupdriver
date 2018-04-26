package com.app.bickupdriver.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by karun on 13/12/17.
 */

public class Helper implements Serializable {


    @SerializedName("helper_id")
    public String helperId;
    @SerializedName("price")
    public String price;

}
