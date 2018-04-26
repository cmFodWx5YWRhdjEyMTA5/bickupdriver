package com.app.bickupdriver.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>GoodsImage</H1>
 *
 * <p>Represents the POJO class or object model for Goods Image</p>
 *
 * @author Divya Thakur
 * @since 11/12/17
 * @version 1.0
 */
public class GoodsImage implements Serializable {

    @SerializedName("image_url")
    public String imageUrl;

}
