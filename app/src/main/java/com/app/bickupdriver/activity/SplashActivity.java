package com.app.bickupdriver.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.app.bickupdriver.R;
import com.app.bickupdriver.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickupdriver.controller.AppController;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;


public class SplashActivity extends AppCompatActivity implements InternetConnectionBroadcast.ConnectivityRecieverListener {
    private boolean mIsConnected;
    private Activity mActivityreference;
    private CoordinatorLayout mCoordinatorLayout;
    private Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActivityreference=SplashActivity.this;
        initializeViews();
        checkUserIsLogin();
    }

    private void checkUserIsLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantValues.USER_PREFERENCES, Context.MODE_PRIVATE);
        String isLogin= sharedPreferences.getString(ConstantValues.ISLogin,"false");
        if(isLogin.equalsIgnoreCase("true")){
            String isVerified= sharedPreferences.getString(ConstantValues.ISverified,"false");
            String userFirstName=sharedPreferences.getString(ConstantValues.USER_FIRSTNAME,"");
            String lastname=sharedPreferences.getString(ConstantValues.USER_LASTNAME,"");
            String accessToken=sharedPreferences.getString(ConstantValues.USER_ACCESS_TOKEN,"");
            String mobileNumber=sharedPreferences.getString(ConstantValues.USER_MOBILENUMBER,"");
            String emailAddress=sharedPreferences.getString(ConstantValues.USER_EMAILADDRESS,"");
            String password=sharedPreferences.getString(ConstantValues.USER_PASSWORD,"");
            String countryCode=sharedPreferences.getString(ConstantValues.COUNTRY_CODE,"");
            String userID=sharedPreferences.getString(ConstantValues.USER_ID,"");
            String userImage=sharedPreferences.getString(ConstantValues.USER_IMAGE,"");
            if(isVerified.equalsIgnoreCase("true")){
                User.getInstance().createUser(SplashActivity.this,accessToken,emailAddress,userID,mobileNumber,userFirstName,lastname,password,true,false,countryCode,userImage);
                timerMethod(true);
            }
        }else {
            timerMethod(false);
        }
    }

    private void timerMethod(final boolean isLogin) {
        final Intent[] intent = new Intent[1];
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLogin){
                    intent[0] =new Intent(mActivityreference,MainActivity.class)

                    ;
                }else {
                    intent[0] =new Intent(mActivityreference,LoginActivity.class);
                }
                intent[0].setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent[0]);
                finish();
            }
        },3000);
    }

    private void initializeViews() {
        mCoordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator_layout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetconnection();
        if(AppController.getInstance()!=null) {
            AppController.getInstance().setConnectivityListener(this);
        }
    }

    private void checkInternetconnection() {
        mIsConnected= CommonMethods.getInstance().checkInterNetConnection(mActivityreference);
        if(mIsConnected){
            if(snackbar!=null) {
                snackbar.dismiss();

            }
        }else{
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            if(snackbar!=null) {
                snackbar.dismiss();
            }
        }else{
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }
    public void showSnackBar(String mString){
        snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();
    }
}
