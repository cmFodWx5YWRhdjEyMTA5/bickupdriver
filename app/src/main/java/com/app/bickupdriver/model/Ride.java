package com.app.bickupdriver.model;

import com.app.bickupdriver.utility.ConstantValues;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>Ride</H1>
 * <p>
 * <p>Represents the POJO class or object model for Ride</p>
 *
 * @author Divya Thakur
 * @version 1.0
 * @since 4/12/17
 */
public class Ride implements Serializable {

    @SerializedName("drop_location_address")
    public String dropLocationAddress;
    @SerializedName("pickup_location_address")
    public String pickupLocationAddress;
    @SerializedName("distance")
    public String distance;
    @SerializedName("total_price")
    public float totalPrice;
    @SerializedName("name")
    public String name;
    //@SerializedName(value = "profile_image", alternate = "image")
    @SerializedName("profile_image")
    public GoodsImage goodsImage;

    @SerializedName("image")
    public ArrayList<GoodsImage> goodsImageList;

    @SerializedName("type_of_goods")
    public String[] typesOfGoods;
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("date")
    public String date;
    @SerializedName("ride_completed_status")
    public int rideCompletedStatus;

    @SerializedName("pickup_contact_name")
    public String pickUpContactName;
    @SerializedName("drop_contact_name")
    public String dropContactName;
    @SerializedName("pickup_latitude")
    public String pickupLatitude;
    @SerializedName("pickup_longitude")
    public String pickupLongitude;
    @SerializedName("drop_latitude")
    public String dropLatitude;
    @SerializedName("drop_longitude")
    public String dropLongitude;
    @SerializedName("accepted_by")
    public String acceptedBy;

    @SerializedName("ride_id")
    public String rideId;

    @SerializedName("pickup_contact_number")
    public String pickupContactNumber;
    @SerializedName("drop_contact_number")
    public String dropOffContactNumber;

    @SerializedName("rating")
    public float rating;

    @SerializedName("helper")
    public ArrayList<Helper> helperList;

    @SerializedName("no_of_helpers")
    public int noOfHelpers;
}
