package com.app.bickupdriver.model;

import com.app.bickupdriver.utility.ConstantValues;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Divya Thakur on 6/12/17.
 */

public class Revenue implements Serializable {

    @SerializedName(ConstantValues.BOOKINGS_TO_ACCEPT)
    public float bookingsToAccept;
    @SerializedName(ConstantValues.REVENUE)
    public float revenue;
    @SerializedName(ConstantValues.JOBS_PROVIDED_BY_BICKUP)
    public float jobsProvidedByBickup;
}
