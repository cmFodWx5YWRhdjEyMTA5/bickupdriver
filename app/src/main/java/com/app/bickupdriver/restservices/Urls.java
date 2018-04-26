package com.app.bickupdriver.restservices;

import com.app.bickupdriver.utility.ConstantValues;

/**
 * Created by Divya Thakur on 4/12/17.
 *
 */

public class Urls {

    public static final String DRIVER_BASE_URL = "/driver/";
    public static final String FINAL_BASE_URL = ConstantValues.BASE_URL + DRIVER_BASE_URL;

    public static final String GET_COMPLETED_RIDES = "rideCompleted";
    public static final String GET_MISSED_RIDES = "rideMissed";
    public static final String GET_RIDE_DETAILS = "rideDetails";
    public static final String GET_PARTICULAR_RIDE_DETAIL = "rideDetail/{ride_id}";
    public static final String ACCEPT_REJECT_DELIVERY = "acceptedRejected/{ride_id}/{isAccepted}";
    public static final String CHANGE_RIDE_STATUS = "changeStatus";
    public static final String GET_RIDE_STATUS = "status/{ride_id}";
    public static final String GET_REVENUES = "revenue";
}
