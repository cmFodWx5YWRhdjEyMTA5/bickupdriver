package com.app.bickupdriver.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by karun on 7/12/17.
 */

public class Temp implements Serializable {

        @SerializedName("status")
        public ArrayList<Status> statusList;

        @SerializedName("user_phone_number")
        public String userPhoneNumber;

        @SerializedName("country_code")
        public String countryCode;
}
