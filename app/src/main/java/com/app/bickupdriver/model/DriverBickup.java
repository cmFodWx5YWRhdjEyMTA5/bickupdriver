package com.app.bickupdriver.model;

import com.app.bickupdriver.utility.Image;

/**
 * Created by manish on 17/4/18.
 */

public class DriverBickup {
    private String first_name;

    private String last_name;

    private String email;
    private String driver_id;
    private String phone_number;
    private String access_token;
    private String account_number;
    private String bank_name;
    private String country_code;
    private String profile_status;
    private String certificate_registration_number;

    public DriverBickup() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getProfile_status() {
        return profile_status;
    }

    public void setProfile_status(String profile_status) {
        this.profile_status = profile_status;
    }

    public String getCertificate_registration_number() {
        return certificate_registration_number;
    }

    public void setCertificate_registration_number(String certificate_registration_number) {
        this.certificate_registration_number = certificate_registration_number;
    }
}
