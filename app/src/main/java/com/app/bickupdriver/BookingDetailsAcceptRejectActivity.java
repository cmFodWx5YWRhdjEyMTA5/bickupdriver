package com.app.bickupdriver;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickupdriver.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickupdriver.controller.AppConstants;
import com.app.bickupdriver.controller.AppController;
import com.app.bickupdriver.fragments.BookingDetailsAcceptRejectFragment;
import com.app.bickupdriver.fragments.GoodsDetailsFragments;
import com.app.bickupdriver.interfaces.HandlerGoodsNavigations;
import com.app.bickupdriver.model.Ride;
import com.app.bickupdriver.model.RideObjectResponse;
import com.app.bickupdriver.restservices.ApiClientConnection;
import com.app.bickupdriver.restservices.ApiInterface;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingDetailsAcceptRejectActivity extends AppCompatActivity implements HandlerGoodsNavigations,
        View.OnClickListener, InternetConnectionBroadcast.ConnectivityRecieverListener {

    private TextView txtHeader;
    private RelativeLayout rlContainer;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;
    private CoordinatorLayout mCoordinatorLayout;
    private String TAG = getClass().getSimpleName();
    private Context context = BookingDetailsAcceptRejectActivity.this;
    public Ride rideNew;
    private BookingDetailsAcceptRejectFragment bookingDetailsAcceptRejectFragment;
    public String rideId;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_goods);

        Utils.printLogs(TAG, "Opening Goods Activity ... ");
        initializeViews();


        /**
         * Implement Broadcast Receiver
         */
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Utils.GOODS_ACTIVITY_NOTIFICATION_BROADCAST_ACTION)) {
                    rideId = intent.getStringExtra(ConstantValues.RIDE_ID);
                    fetchRideDetails();
                } else {
                    Utils.printLogs(TAG, "Action don't matches Broadcast Receiver .. BookingDetailsAcceptRejectActivity");
                }
            }
        };


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.GOODS_ACTIVITY_NOTIFICATION_BROADCAST_ACTION);

        registerReceiver(broadcastReceiver, intentFilter);

        this.getIntentValues();
        this.fetchRideDetails();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    /**
     * Fetches values from Intent
     */
    private void getIntentValues() {
        //ride = (Ride) getIntent().getSerializableExtra(ConstantValues.RIDE);

        Intent intent = getIntent();
        if (intent != null) {
            rideId = intent.getStringExtra(ConstantValues.RIDE_ID);
            Utils.printLogs(TAG, "Ride Id on BookingDetailsAcceptRejectActivity : " + rideId);
        }

    }

    private void addBookingDetailsFragment() {
        Utils.printLogs(TAG, "Adding Booking Details Fragment ...");
        txtHeader.setText("Booking Number - " + rideId);
        bookingDetailsAcceptRejectFragment = new BookingDetailsAcceptRejectFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.goods_activity_container,
                bookingDetailsAcceptRejectFragment).addToBackStack(BookingDetailsAcceptRejectFragment.Tag).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
       /* snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();*/
    }

    private void initializeViews() {

        mActivityreference = this;
        ImageView imgBack = (ImageView) findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener((View.OnClickListener) this);

        mTypefaceRegular = Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_BOLD);
        txtHeader = (TextView) findViewById(R.id.txt_activty_header);
        txtHeader.setTypeface(mTypefaceRegular);
        rlContainer = (RelativeLayout) findViewById(R.id.goods_activity_container);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_activity_goods);
    }

    /**
     * Fetches current ride details
     */
    private void fetchRideDetails() {
        Utils.printLogs(TAG, "Fetching Ride Details on Goods Details Activity");
        ApiInterface service;
        final ProgressDialog progressDialog = Utils.showProgressDialog(context,
                AppConstants.DIALOG_PLEASE_WAIT);
        try {
            if (Utils.isNetworkAvailable(context)) {
                service = ApiClientConnection.getInstance().createService();
                SharedPreferences sharedPreferences = getSharedPreferences(
                        ConstantValues.USER_PREFERENCES,
                        Context.MODE_PRIVATE);

                String accessToken = sharedPreferences.getString(
                        ConstantValues.USER_ACCESS_TOKEN, "");

                ////
                Utils.printLogs(TAG, "Ride id ... " + rideId);
                Call<RideObjectResponse> call = service.getParticularRideDetails(accessToken,
                        rideId);

                call.enqueue(new Callback<RideObjectResponse>() {
                    @Override
                    public void onResponse(Call<RideObjectResponse> call,
                                           Response<RideObjectResponse> response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                RideObjectResponse apiResponse = response.body();
                                Utils.printLogs(TAG, "onResponse : Success : -- " +
                                        apiResponse.ride);
                                if (apiResponse.ride != null) {
                                    rideNew = apiResponse.ride;
                                    addBookingDetailsFragment();
                                } else {
                                    Utils.showToast(apiResponse.message, context);
                                }
                            } else {
                                Utils.printLogs(TAG, "onResponse : Failure : -- " +
                                        response.body());
                            }
                        } catch (Exception e) {
                            Utils.printLogs(TAG, "Exception 222 " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<RideObjectResponse> call, Throwable t) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        Utils.printLogs(TAG, "onFailure : -- " + t.getCause());
                    }
                });
            } else {
            }
        } catch (Exception e) {
            Utils.printLogs(TAG, "Exception while fetching Ride details : " + e.getMessage());
            e.printStackTrace();
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

        }
    }

    @Override
    public void callBookingDetailsFragment() {
        BookingDetailsAcceptRejectFragment bookingDetailsAcceptRejectFragments = new BookingDetailsAcceptRejectFragment();
        txtHeader.setText(getResources().getString(R.string.txt_view_details));
        getSupportFragmentManager().beginTransaction().replace(R.id.goods_activity_container,
                bookingDetailsAcceptRejectFragments).addToBackStack(BookingDetailsAcceptRejectFragment.Tag).commit();
    }

    @Override
    public void callGoodsFragment() {
        GoodsDetailsFragments goodsDetailsFragments = new GoodsDetailsFragments();
        txtHeader.setText(getResources().getString(R.string.txt_goods_details));
        getSupportFragmentManager().beginTransaction().replace(R.id.goods_activity_container,
                goodsDetailsFragments).addToBackStack(GoodsDetailsFragments.TAG).commit();
    }

    @Override
    public void callTrackDriverActivity() {
        Intent intent = new Intent(this, TrackDriverActivity.class);
        intent.putExtra("ride", rideNew);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backImage_header:
                onBackPressed();
                break;
        }
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

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public void moveToMainScreen() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    public void drawPath(String result) {
        bookingDetailsAcceptRejectFragment.drawPath(result);
    }
}