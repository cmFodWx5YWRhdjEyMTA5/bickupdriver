package com.app.bickupdriver.controller.location;

/**
 * Created by Divya Thakur on 5/12/17.
 */

public interface LocationChangeCallback {

    /**
     * Callback method for getting the location changes
     * @param latitude Holds the latitude
     * @param longitude Holds the longitude
     */
    void onLocationChange(String latitude, String longitude);
}
