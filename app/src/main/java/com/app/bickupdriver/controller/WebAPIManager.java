package com.app.bickupdriver.controller;

import com.app.bickupdriver.utility.ConstantValues;

/**
 * Created by fluper-pc on 29/10/17.
 */

public class WebAPIManager {
    private static final WebAPIManager ourInstance = new WebAPIManager();

    public static WebAPIManager getInstance() {
        return ourInstance;
    }

    private WebAPIManager() {
    }


    public String getCreateUserUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver";
        return  url;
    }

    public String getUserLoginUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/login";
        return  url;
    }

    public String getVerifyUserUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/verifyOtp?";
        return  url;
    }

    public String getResenOTPdUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/resendOtp";
        return  url;
    }


    public String getforgotPasswordUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/forgotPassword?";
        return  url;
    }

    public String getresetPasswordUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/resetPassword?";
        return  url;
    }
    public String getchangeNumberUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/changeNumber?phone_number=";
        return  url;
    }

    public String getimageUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver";
        return  url;
    }

    public String getbankUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/bank";
        return  url;
    }

    public String getupdateProfileUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver";
        return  url;
    }

    public String getLicenseFragmentUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/license";
        return  url;

    }

    public String getregistrationUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/certificate";
        return  url;

    }

    public String getInsurenceUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/insurance";
        return  url;

    }

    public String getTradeInsurenceUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/tradeLicensePaper";
        return  url;

    }
    public String getVehicaleUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/vehicle";
        return  url;

    }

    public String getUserDetailsUrl(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver/details";
        return  url;
    }

    public String getUpdateUser(){
        String url= ConstantValues.BASE_URL;
        url=url+"/driver";
        return  url;
    }
}
