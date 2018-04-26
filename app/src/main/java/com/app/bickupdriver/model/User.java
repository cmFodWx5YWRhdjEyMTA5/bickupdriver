package com.app.bickupdriver.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.bickupdriver.utility.ConstantValues;

import java.io.Serializable;

/**
 * Created by fluper-pc on 29/10/17.
 */

public class User implements Serializable{

    private String accesstoken=null;
    private String firstName="";

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    private String lastName="";
    private String email=null;
    private String mobileNumber=null;
    private String userId=null;
    private String password=null;
    private boolean isVarified=false;
    private boolean isLogin =false;



    public String getCountryCode() {
        return countryCode;
    }

    public void setUserImage(String userImage,Activity activity) {
        this.userImage = userImage;
        setUserDataToPreferences(activity);
    }

    private String userImage;
    private  String countryCode;

    public String getUserImage() {
        return userImage;
    }



    private SharedPreferences sharedPreferences;
    private static final User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }

    public void createUser(Activity activity,String accesstoken, String email, String userId, String mobileNumber, String firstName, String lastName, String password,boolean isVarified,boolean saveToPreferences,String countryCode,String userImage){
        this.accesstoken=accesstoken;
        this.email=email;
        this.userId=userId;
        this.mobileNumber=mobileNumber;
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
        this.isVarified=isVarified;
        this.countryCode=countryCode;
        this.userImage=userImage;
        if(saveToPreferences) {
            setUserDataToPreferences(activity);
        }
    }

    public void updateProfile(Activity activity, String email, String firstName, String lastName){
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;

        setUserDataToPreferences(activity);

    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public static User getOurInstance() {
        return ourInstance;
    }

    public void setUserDataToPreferences(Activity activity){
        sharedPreferences=activity.getSharedPreferences(ConstantValues.USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantValues.USER_ACCESS_TOKEN,getAccesstoken());
        editor.putString(ConstantValues.USER_EMAILADDRESS,getEmail());
        editor.putString(ConstantValues.USER_ID,getUserId());
        editor.putString(ConstantValues.USER_FIRSTNAME,getFirstName());
        editor.putString(ConstantValues.USER_LASTNAME,getLastName());
        editor.putString(ConstantValues.USER_PASSWORD,getPassword());
        editor.putString(ConstantValues.USER_MOBILENUMBER,getMobileNumber());
        editor.putString(ConstantValues.ISverified,String.valueOf(isVarified()));
        editor.putString(ConstantValues.ISLogin,String.valueOf(isLogin()));
        editor.putString(ConstantValues.USER_IMAGE,getUserImage());
        editor.putString(ConstantValues.COUNTRY_CODE,getCountryCode());
        editor.commit();
    }

    public void setVarified(boolean varified,Activity activity,boolean isLogin) {
        isVarified = varified;
        this.isLogin=isLogin;
        setUserDataToPreferences(activity);
    }

    public boolean isLogin() {
        return isLogin;
    }



    public boolean isVarified() {
        return isVarified;
    }
}
