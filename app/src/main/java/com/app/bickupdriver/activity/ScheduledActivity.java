package com.app.bickupdriver.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickupdriver.controller.AppController;
import com.app.bickupdriver.utility.CommonMethods;

public class ScheduledActivity extends AppCompatActivity implements View.OnClickListener, InternetConnectionBroadcast.ConnectivityRecieverListener {


    private ImageView imgBack;
    private CoordinatorLayout mCoordinatorLayout;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_scheduled);
        initializeViews();

    }


    private void initializeViews() {
        mActivityreference=this;
        mCoordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator__activity_scheduled);
        TextView txtHeader=(TextView)findViewById(R.id.txt_activty_header);
        txtHeader.setText(getResources().getString(R.string.txt_schedule_delivery));
        imgBack=(ImageView)findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.backImage_header:
                finish();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initializeViews();
        checkInternetconnection();
        if (AppController.getInstance() != null) {
            AppController.getInstance().setConnectivityListener(this);
        }
    }



    private void checkInternetconnection() {
        mIsConnected = CommonMethods.getInstance().checkInterNetConnection(mActivityreference);
        if (mIsConnected) {
            if (snackbar != null) {
                snackbar.dismiss();

            }
        } else {
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

    public void showSnackBar(String mString) {
        snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else {
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }
}
