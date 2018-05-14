package com.app.bickupdriver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickupdriver.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickupdriver.controller.AppConstants;
import com.app.bickupdriver.controller.AppController;
import com.app.bickupdriver.controller.location.LocationChangeCallback;
import com.app.bickupdriver.model.Ride;
import com.app.bickupdriver.model.RideStatusResponse;
import com.app.bickupdriver.model.Status;
import com.app.bickupdriver.restservices.ApiClientConnection;
import com.app.bickupdriver.restservices.ApiInterface;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.DateHelper;
import com.app.bickupdriver.utility.GpsTracker;
import com.app.bickupdriver.utility.MapUtils;
import com.app.bickupdriver.utility.Utils;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackDriverActivityDriver extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, InternetConnectionBroadcast.ConnectivityRecieverListener,
        LocationChangeCallback, RoutingListener {

    public static String OPENTYPESGOODS = "opentypesgoods";
    private TextView txtTrackStatus, txtHeaderText, txtOntheWay, txtTime;
    private TextView txtOntheWayBottomSheet, txtTimeBottomSheet;
    //private ImageView imgDriverImage, imgDriverImageBottomSheet, callDriver, callDriverBottomSheet;
    private Button btnAssignAnother;
    private Button btnAssignAnotherBottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ImageView imgBackButton;
    private RelativeLayout rlBottomSheet;

    //private GoogleMap map;
    private Typeface mTypefaceRegular;
    //private Typeface mTypefaceBold;
    private Activity activity;
    // private CoordinatorLayout mCoordinatorLayout;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;

    private String TAG = getClass().getSimpleName();
    private Context context = this;
    private Socket socket;
    private String driverId;
    public GpsTracker gpsTracker;
    private final int REQUEST_CODE_LOCATION_PERMISSION = 908;
    private String latitude, longitude;
    /*private TextView labelJobProvided, labelRevenue, valueJobProvided, valueRevenue,
            txtOnline, valueBookingPovided;*/
    private Polyline line;
    private LatLng currentLatLng;
    /* private LatLng destinationLatLng = new LatLng(28.6096270,
             77.3870740);*/
    private GoogleMap googleMap;
    private Ride ride;
    // private Ride rideNew;
    private TextView txtTrackStatusBottomSheet, txtBookingStatusTime, txtBookingStatus,
            txtOnTheWayTime;
    private TextView txtOnTheWay, txtArrivedTime, txtArrived, txtLoadingTime, txtLoading;
    private TextView txtEnrouteTime, txtEnroute, txtReachedDropOff;
    private ImageView checkbox_on_the_way, checkbox_arrived, checkbox_enroute,
            checkbox_reached_drop_off;
    private ImageView checkbox_unload, checkbox_delivered, loading_checkbox, cbBookingStatus;
    private TextView txt_reached_drop_off_time, txt_unload_time, txt_delivered_time;
    private PolylineOptions options;
    private ArrayList<LatLng> points;
    private LocationRequest mLocationRequest;
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    private static final float SMALLEST_DISPLACEMENT = 0.25F; //quarter of a meter


    private ImageView ivNavigation;
    private ImageView ivNavigationNormal;
    private RelativeLayout layoutInNormalState;
    private LatLng pickupLatLng;
    private LatLng dropLatLng;
    //private String rideId;
    private List<Polyline> polylines;
    private ImageView ivCallUser;
    private ImageView ivCallUserBottomSheet;
    private String userPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_track_driver);
        polylines = new ArrayList<>();

        setGoogleMap();
        initiTializeViews();


        this.getIntentValues();
        this.checkForLocationPermission();

        SharedPreferences sharedPreferences = getSharedPreferences(
                ConstantValues.USER_PREFERENCES,
                Context.MODE_PRIVATE);
        gpsTracker = new GpsTracker(context, this);
        driverId = sharedPreferences.getString(
                ConstantValues.USER_ID, "");
        this.getStatusOfRide();

        TrackDriverActivityDriver.this.connectSocketForLocationUpdates();

        /*String urlTopass = this.makeURL(gpsTracker.getLatitude(),
                gpsTracker.getLongitude(),
                Double.valueOf(ride.dropLatitude),
                Double.valueOf(ride.dropLongitude),
                Double.valueOf(ride.pickupLatitude),
                Double.valueOf(ride.pickupLongitude));

        new MapRouteAsyncTask(TrackDriverActivityDriver.this, urlTopass).execute();*/

        pickupLatLng = new LatLng(Double.parseDouble(ride.pickupLatitude), Double.parseDouble(ride.pickupLongitude));
        dropLatLng = new LatLng(Double.parseDouble(ride.dropLatitude), Double.parseDouble(ride.dropLongitude));

    }


    public static void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        googleMap.setPadding(100, 250, 100, 800);   // left, top, right, bottom
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 10;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    private void drawRouteBetweenTwoPlaces(LatLng pickupLatLng, LatLng dropLatLng) {
        if (pickupLatLng != null) {
            if (dropLatLng != null) {
                MapUtils.addMarker(googleMap, pickupLatLng, "", R.drawable.pin_location_pin);
                MapUtils.addMarker(googleMap, dropLatLng, "", R.drawable.drop_location_pin);

                Routing routing = new Routing.Builder()
                        .travelMode(Routing.TravelMode.DRIVING)
                        .withListener(this)
                        .waypoints(pickupLatLng, dropLatLng)
                        .key(getString(R.string.google_maps_key))
                        .build();
                routing.execute();

            } else {
                Utils.printLogs(TAG, "Drop LatLng is null");
            }
        } else {
            Utils.printLogs(TAG, "Pickup LatLng is null");
        }
    }


    /**
     * Fetches values from Intent
     */
    private void getIntentValues() {
        ride = (Ride) getIntent().getSerializableExtra(ConstantValues.RIDE);
        txtHeaderText.setText("Booking Number - " + ride.rideId);
    }

    /**
     * Fetches current Ride Status
     */
    private void getStatusOfRide() {
        Utils.printLogs(TAG, "Getting status of Ride ... ");
        ApiInterface service;
        try {
            if (Utils.isNetworkAvailable(context)) {

                final ProgressDialog progressDialog = Utils.showProgressDialog(context,
                        AppConstants.DIALOG_PLEASE_WAIT);

                service = ApiClientConnection.getInstance().createService();

                SharedPreferences sharedPreferences = getSharedPreferences(
                        ConstantValues.USER_PREFERENCES,
                        Context.MODE_PRIVATE);

                String accessToken = sharedPreferences.getString(
                        ConstantValues.USER_ACCESS_TOKEN, "");

                Call<RideStatusResponse> call = service.getStatusOfRide(accessToken, ride.rideId);

                call.enqueue(new Callback<RideStatusResponse>() {
                    @Override
                    public void onResponse(Call<RideStatusResponse> call,
                                           Response<RideStatusResponse> response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                RideStatusResponse apiResponse = response.body();
                                Utils.printLogs(TAG, "onResponse : Success : -- " +
                                        apiResponse.temp.statusList);
                                if (apiResponse.temp.statusList != null) {
                                    String countryCode = apiResponse.temp.countryCode;
                                    String phoneNumber = apiResponse.temp.userPhoneNumber;

                                    Utils.printLogs(TAG, "Country Code : " + countryCode);
                                    Utils.printLogs(TAG, "Phone Number : " + phoneNumber);

                                    userPhoneNumber = countryCode + phoneNumber;

                                    TrackDriverActivityDriver.this.setStatusOfRide(
                                            apiResponse.temp.statusList);


                                    Utils.printLogs(TAG, "Get Status of Ride : " + apiResponse.temp.statusList);
                                } else {
                                    Utils.showToast(apiResponse.message, context);
                                }
                            } else {
                                Utils.printLogs(TAG, "onResponse : Failure : -- " +
                                        response.body());
                            }
                        } catch (Exception e) {
                            Utils.printLogs(TAG, "Exception : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<RideStatusResponse> call, Throwable t) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        Utils.printLogs(TAG, "onFailure : -- " + t.getCause());
                    }
                });
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStatusOfRide(ArrayList<Status> statusList) {
        Utils.printLogs(TAG, "Setting status of Ride ... ");
        int i = 0;
        String[] statusArray = getResources().getStringArray(R.array.ride_status);
        if (statusList.get(i).status == 0) {
            cbBookingStatus.setImageDrawable(
                    getResources().getDrawable(R.drawable.ac_checkbox));
            String time = DateHelper.convertTimestampToTime(Long.valueOf(
                    statusList.get(i).timeStamp));
            txtBookingStatusTime.setText(time);
            btnAssignAnother.setText(statusArray[i]);
            btnAssignAnotherBottomSheet.setText(statusArray[i]);
            i++;
        }
        if (statusList.get(i).status == 1) {
            checkbox_on_the_way.setImageDrawable(
                    getResources().getDrawable(R.drawable.ac_checkbox));
            String time = DateHelper.convertTimestampToTime(Long.valueOf(
                    statusList.get(i).timeStamp));
            txtOnTheWayTime.setText(time);
            btnAssignAnother.setText(statusArray[i]);
            btnAssignAnotherBottomSheet.setText(statusArray[i]);
            i++;
        }
        if (statusList.get(i).status == 2) {
            checkbox_arrived.setImageDrawable(
                    getResources().getDrawable(R.drawable.ac_checkbox));
            String time = DateHelper.convertTimestampToTime(Long.valueOf(
                    statusList.get(i).timeStamp));
            txtArrivedTime.setText(time);
            btnAssignAnother.setText(statusArray[i]);
            btnAssignAnotherBottomSheet.setText(statusArray[i]);
            i++;
        }
        if (statusList.get(i).status == 3) {
            loading_checkbox.setImageDrawable(
                    getResources().getDrawable(R.drawable.ac_checkbox));
            String time = DateHelper.convertTimestampToTime(Long.valueOf(
                    statusList.get(i).timeStamp));
            txtLoadingTime.setText(time);
            btnAssignAnother.setText(statusArray[i]);
            btnAssignAnotherBottomSheet.setText(statusArray[i]);
            i++;
        }
        if (statusList.get(i).status == 4) {
            checkbox_enroute.setImageDrawable(
                    getResources().getDrawable(R.drawable.ac_checkbox));
            String time = DateHelper.convertTimestampToTime(Long.valueOf(
                    statusList.get(i).timeStamp));
            txtEnrouteTime.setText(time);
            btnAssignAnother.setText(statusArray[i]);
            btnAssignAnotherBottomSheet.setText(statusArray[i]);
            i++;
        }
        if (statusList.get(i).status == 5) {
            checkbox_reached_drop_off.setImageDrawable(
                    getResources().getDrawable(R.drawable.ac_checkbox));
            String time = DateHelper.convertTimestampToTime(Long.valueOf(
                    statusList.get(i).timeStamp));
            txt_reached_drop_off_time.setText(time);
            btnAssignAnother.setText(statusArray[i]);
            btnAssignAnotherBottomSheet.setText(statusArray[i]);
            i++;
        }
        if (statusList.get(i).status == 6) {
            checkbox_unload.setImageDrawable(
                    getResources().getDrawable(R.drawable.ac_checkbox));
            String time = DateHelper.convertTimestampToTime(Long.valueOf(
                    statusList.get(i).timeStamp));
            txt_unload_time.setText(time);
            btnAssignAnother.setText(statusArray[i]);
            btnAssignAnotherBottomSheet.setText(statusArray[i]);
            i++;
        }
        if (statusList.get(i).status == 7) {
            checkbox_delivered.setImageDrawable(
                    getResources().getDrawable(R.drawable.ac_checkbox));
            String time = DateHelper.convertTimestampToTime(Long.valueOf(
                    statusList.get(i).timeStamp));
            txt_delivered_time.setText(time);


            //this.onBackPressed();
            // Open Payment Screen
            openPaymentScreen();

        }
    }

    private void openPaymentScreen() {
        Intent paymentScreenIntent = new Intent(this, PaymentActivity.class);
        startActivity(paymentScreenIntent);
        finish();
    }


    /**
     * Changes current Ride Status
     */

    // driver/changeStatus when button is clicked.
    private void changeCurrentStatusOfRide() {
        Utils.printLogs("STATUS", "Change current status of Ride ... ");
        ApiInterface service;
        try {
            if (Utils.isNetworkAvailable(context)) {

                final ProgressDialog progressDialog = Utils.showProgressDialog(context,
                        AppConstants.DIALOG_PLEASE_WAIT);

                service = ApiClientConnection.getInstance().createService();

                SharedPreferences sharedPreferences = getSharedPreferences(
                        ConstantValues.USER_PREFERENCES,
                        Context.MODE_PRIVATE);

                String accessToken = sharedPreferences.getString(
                        ConstantValues.USER_ACCESS_TOKEN, "");

                Call<RideStatusResponse> call = service.changeRideStatus(accessToken, ride.rideId);

                call.enqueue(new Callback<RideStatusResponse>() {
                    @Override
                    public void onResponse(Call<RideStatusResponse> call,
                                           Response<RideStatusResponse> response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                RideStatusResponse apiResponse = response.body();
                                Utils.printLogs(TAG, "onResponse : Success : -- " +
                                        apiResponse.temp.statusList);
                                if (apiResponse.temp.statusList != null) {
                                    Utils.printLogs(TAG, "Change Status of Ride : " + apiResponse.temp.statusList);
                                    TrackDriverActivityDriver.this.setStatusOfRide(
                                            apiResponse.temp.statusList);
                                } else {
                                    Utils.showToast(apiResponse.message, context);
                                }
                            } else {
                                Utils.printLogs(TAG, "onResponse : Failure : -- " +
                                        response.body());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<RideStatusResponse> call, Throwable t) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        Utils.printLogs(TAG, "onFailure : -- " + t.getCause());
                    }
                });
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setGoogleMap() {
        SupportMapFragment mAupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_track_driver);
        mAupportMapFragment.getMapAsync(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initiTializeViews() {
        //mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_track_driver);
        // points = new ArrayList<>();

        ivCallUser = findViewById(R.id.iv_call_user);
        ivCallUser.setOnClickListener(this);
        ivCallUserBottomSheet = findViewById(R.id.iv_call_user_bottomsheet);
        ivCallUserBottomSheet.setOnClickListener(this);

        activity = this;
        mActivityreference = this;
        mTypefaceRegular = Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        //mTypefaceBold = Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        txtHeaderText = (TextView) findViewById(R.id.txt_activty_header);

        txtTrackStatus = (TextView) findViewById(R.id.txt_track_status);

        txtOntheWay = (TextView) findViewById(R.id.txt_on_the_way_time);

        txtOntheWayBottomSheet = (TextView) findViewById(R.id.txt_on_the_way_card_bottom_sheet);
        txtTime = (TextView) findViewById(R.id.txt_time);
        txtTimeBottomSheet = (TextView) findViewById(R.id.txt_time_bottom_sheet);
        btnAssignAnother = (Button) findViewById(R.id.btn_asign);
        btnAssignAnotherBottomSheet = (Button) findViewById(R.id.btn_asign_bottomsheet);
        /*imgDriverImageBottomSheet = (ImageView) findViewById(R.id.img_driver_bottomshet);
        imgDriverImage = (ImageView) findViewById(R.id.img_driver);
        callDriverBottomSheet = (ImageView) findViewById(R.id.img_call_bottomsheet);
        callDriver = (ImageView) findViewById(R.id.call_driver);*/
        ivNavigation = findViewById(R.id.iv_navigation);
        ivNavigation.setOnClickListener(this);

        ivNavigationNormal = findViewById(R.id.iv_navigation_normall);
        ivNavigationNormal.setOnClickListener(this);

        ImageView imgBack = (ImageView) findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        btnAssignAnotherBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mBottomSheetBehavior.setPeekHeight(0);
            }
        });

        btnAssignAnother.setOnClickListener(this);

        rlBottomSheet = (RelativeLayout) findViewById(R.id.rl_bottomSheet);
        rlBottomSheet.setOnClickListener(this);


        txtHeaderText.setTypeface(mTypefaceRegular);
        txtTrackStatus.setTypeface(mTypefaceRegular);
        txtOntheWay.setTypeface(mTypefaceRegular);
        txtTime.setTypeface(mTypefaceRegular);
        txtOntheWayBottomSheet.setTypeface(mTypefaceRegular);
        txtTimeBottomSheet.setTypeface(mTypefaceRegular);
        //btnAssignAnother.setTypeface(mTypefaceRegular);
        btnAssignAnotherBottomSheet.setTypeface(mTypefaceRegular);

        layoutInNormalState = findViewById(R.id.card_container);

        setTypeFaceToViews();

        View bottomSheet = findViewById(R.id.design_bottom_sheet);


        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Utils.printLogs(TAG, "onStateChanged");
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    //toolbarLyout.setVisibility(View.VISIBLE);
                    Utils.printLogs(TAG, "STATE Collapsed");
                    layoutInNormalState.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Utils.printLogs(TAG, "onSlide");
                if (bottomSheet.getScrollY() == BottomSheetBehavior.STATE_DRAGGING)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Utils.printLogs(TAG, "STATE Expanded");
            }
        });

        txtTrackStatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Utils.printLogs(TAG, "STATE Collapsed2");
                        break;
                    case MotionEvent.ACTION_UP:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        layoutInNormalState.setVisibility(View.GONE);
                        Utils.printLogs(TAG, "STATE Expanded2");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        txtTrackStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   toolbarLyout.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void fixZoom() {
        List<LatLng> points = options.getPoints(); // route is instance of PolylineOptions

        LatLngBounds.Builder bc = new LatLngBounds.Builder();

        for (LatLng item : points) {
            bc.include(item);
        }

        googleMap.setPadding(10, 130, 10, 500);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
    }

    /*private void hidebottom() {
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void showPopUp() {
        final Dialog openDialog = new Dialog(this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.assign_driver_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnAgree = (Button) openDialog.findViewById(R.id.btn_agree);
        Button btnDisAgree = (Button) openDialog.findViewById(R.id.btn_disagree);

        btnAgree.setTypeface(mTypefaceRegular);
        btnDisAgree.setTypeface(mTypefaceRegular);
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();

                Intent intent = new Intent(TrackDriverActivityDriver.this, TypesGoods.class);
                intent.putExtra(OPENTYPESGOODS, 1);
                startActivity(intent);
            }
        });
        btnDisAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });
        openDialog.show();
    }*/

    /**
     * Fetches current ride details
     *//*
    private void fetchRideDetails() {

        ApiInterface service;
        try {
            if (Utils.isNetworkAvailable(context)) {

                final ProgressDialog progressDialog = Utils.showProgressDialog(context,
                        AppConstants.DIALOG_PLEASE_WAIT);

                service = ApiClientConnection.getInstance().createService();

                SharedPreferences sharedPreferences = getSharedPreferences(
                        ConstantValues.USER_PREFERENCES,
                        Context.MODE_PRIVATE);

                String accessToken = sharedPreferences.getString(
                        ConstantValues.USER_ACCESS_TOKEN, "");

                Call<RideObjectResponse> call = service.getParticularRideDetails(accessToken,
                        ride.rideId);

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
                                rideNew = apiResponse.ride;
                            } else {
                                Utils.printLogs(TAG, "onResponse : Failure : -- " +
                                        response.body());
                            }
                        } catch (Exception e) {
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
            e.printStackTrace();
        }
    }*/
    private void setTypeFaceToViews() {
        txtTrackStatusBottomSheet = findViewById(R.id.track_status_bottomSheet);

        txtBookingStatusTime = findViewById(R.id.txt_booking_status_time);
        txtBookingStatus = findViewById(R.id.txt_booking_status);
        txtOnTheWayTime = findViewById(R.id.txt_on_the_way_time);
        txtOnTheWay = findViewById(R.id.txt_on_the_way);
        txtArrivedTime = findViewById(R.id.txt_arrived_time);
        txtArrived = findViewById(R.id.txt_arrived);
        txtLoadingTime = findViewById(R.id.txt_loading_time);
        txtLoading = findViewById(R.id.txt_loading);
        txtEnrouteTime = findViewById(R.id.txt_enroute_time);
        txtEnroute = findViewById(R.id.txt_enroute);
        txtReachedDropOff = findViewById(R.id.txt_reached_drop_off);
        txt_reached_drop_off_time = findViewById(R.id.txt_reached_drop_off_time);
        txt_unload_time = findViewById(R.id.txt_unload_time);
        txt_delivered_time = findViewById(R.id.txt_delivered_time);


        cbBookingStatus = findViewById(R.id.cbBookingStatus);
        checkbox_on_the_way = findViewById(R.id.checkbox_on_the_way);
        checkbox_arrived = findViewById(R.id.checkbox_arrived);
        loading_checkbox = findViewById(R.id.loading_checkbox);
        checkbox_enroute = findViewById(R.id.checkbox_enroute);
        checkbox_reached_drop_off = findViewById(R.id.checkbox_reached_drop_off);
        checkbox_unload = findViewById(R.id.checkbox_unload);
        checkbox_delivered = findViewById(R.id.checkbox_delivered);

        txtTrackStatusBottomSheet.setTypeface(mTypefaceRegular);
        txtBookingStatusTime.setTypeface(mTypefaceRegular);
        txtBookingStatus.setTypeface(mTypefaceRegular);
        txtOnTheWayTime.setTypeface(mTypefaceRegular);
        txtOnTheWay.setTypeface(mTypefaceRegular);
        txtArrivedTime.setTypeface(mTypefaceRegular);
        txtArrived.setTypeface(mTypefaceRegular);
        txtLoading.setTypeface(mTypefaceRegular);
        txtLoadingTime.setTypeface(mTypefaceRegular);
        txtEnrouteTime.setTypeface(mTypefaceRegular);
        txtEnroute.setTypeface(mTypefaceRegular);
        txtReachedDropOff.setTypeface(mTypefaceRegular);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (googleMap != null) {
            this.googleMap = googleMap;
            /*MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style);
            this.googleMap.setMapStyle(style);*/

            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    //Your code where exception occurs goes here...
                    /**
                     * Zoom the Map
                     */
                    List<LatLng> latLngList = new ArrayList<>();
                    latLngList.add(pickupLatLng);
                    latLngList.add(dropLatLng);
                    zoomRoute(googleMap, latLngList);
                    drawRouteBetweenTwoPlaces(pickupLatLng, dropLatLng);

                }
            });

            /*if (this.checkForLocationPermission()) {
                this.trackCurrentLocation();
            }*/
        }
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
        Toast.makeText(activity, mString, Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.iv_call_user:
                callUser();
                break;
            case R.id.iv_call_user_bottomsheet:
                callUser();
                break;

            case R.id.rl_bottomSheet:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mBottomSheetBehavior.setPeekHeight(0);
                // toolbarLyout.setVisibility(View.VISIBLE);
                break;
            case R.id.backImage_header:
                this.onBackPressed();
                break;
            case R.id.btn_asign:
                this.changeCurrentStatusOfRide();
                //showPopUp();
                break;

            case R.id.iv_navigation:
                openGoogleNavigation();
                break;

            case R.id.iv_navigation_normall:
                openGoogleNavigation();
                break;
        }
    }

    private void callUser() {
        // TODO: 14/5/18   - Change it to user_contact_number
        Utils.printLogs(TAG, "Calling User having contact Number : " + userPhoneNumber);

        if (userPhoneNumber != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", userPhoneNumber, null));
            startActivity(intent);
        }
    }

    private void openGoogleNavigation() {
        Utils.printLogs(TAG, "Opening Google Navigation ... ");
        String sourceLatitude = ride.pickupLatitude;
        String sourceLongitude = ride.pickupLongitude;

        String destinationLatitude = ride.dropLatitude;
        String destinationLongitude = ride.dropLongitude;
        String uri = "http://maps.google.com/maps?saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + destinationLatitude + "," + destinationLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
        //super.onBackPressed();
    }

    /**
     * Checks for the Permission of Location
     */
    private boolean checkForLocationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            this.trackCurrentLocation();
            Utils.printLogs(TAG, "Permission Granted");
            return true;
        } else {

            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
            Utils.printLogs(TAG, "Permission NOT Granted, Requesting for the same");
            return false;
        }
    }

    /**
     * Tracks current location of user
     */
    private void trackCurrentLocation() {

        gpsTracker = new GpsTracker(context, TrackDriverActivityDriver.this);
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            this.updateLocationUI();
        } else {
            latitude = "";
            longitude = "";
        }
    }

    public void drawPath(String result) {

        if (line != null) {
            googleMap.clear();
        }

        // Drop off location marker
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(ride.dropLatitude),
                        Double.valueOf(ride.dropLongitude)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.drop_location_pin))
                .title(ride.dropLocationAddress));

        // Pickup location marker
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(ride.pickupLatitude),
                        Double.valueOf(ride.pickupLongitude)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.drop_location_pin))
                .title(ride.pickupLocationAddress));

        // Current location marker
        googleMap.addMarker(new MarkerOptions()
                .flat(true)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pin_location_pin))
                .anchor(0.5f, 0.5f)
                .position(new LatLng(gpsTracker.getLatitude(),
                        gpsTracker.getLongitude())));


        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);

            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");

            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            options = new PolylineOptions().width(Utils.THICKNESS_OF_POLYLINE)
                    .color(getResources().getColor(R.color.appcolor)).geodesic(true);

            for (int z = 0; z < list.size(); z++) {
                LatLng point = list.get(z);
                options.add(point);
            }

            line = googleMap.addPolyline(options);

            this.fixZoom();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Decodes the polyline
     *
     * @param encoded
     * @return
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    /**
     * Creates the URL for drawing the polyline on google map
     *
     * @param sourcelat
     * @param sourcelog
     * @param destlat
     * @param destlog
     * @return
     */
    private String makeURL(double sourcelat, double sourcelog, double destlat,
                           double destlog, double wayPointLat, double wayPointLng) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&waypoints=");
        urlString.append(Double.toString(wayPointLat));
        urlString.append(",");
        urlString.append(Double.toString(wayPointLng));
        return urlString.toString();
    }

    /**
     * Updates the location on map
     */
    private void updateLocationUI() {

        if (googleMap != null) {

            if (gpsTracker != null) {

                LatLngBounds currentBounds = new LatLngBounds(
                        new LatLng(gpsTracker.getLatitude(),
                                gpsTracker.getLongitude()),
                        new LatLng(gpsTracker.getLatitude(),
                                gpsTracker.getLongitude()));

                LatLng currentLatLng = new LatLng(gpsTracker.getLatitude(),
                        gpsTracker.getLongitude());

                CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(
                        currentBounds.getCenter(),
                        10);
                googleMap.animateCamera(myLocation);
                UiSettings mUiSetting = googleMap.getUiSettings();
                mUiSetting.setTiltGesturesEnabled(true);
                mUiSetting.setRotateGesturesEnabled(true);

               /* CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(gpsTracker.getLatitude(),
                                gpsTracker.getLongitude()))
                        .radius(30)
                        .fillColor(Color.appcolor)
                        .strokeColor(Color.appcolor)
                        .strokeWidth(3); // In meters*/

                Marker mPositionMarker = googleMap.addMarker(new MarkerOptions()
                        .flat(true)
                        .position(currentLatLng)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.pin_location_pin))
                        .anchor(0.5f, 0.5f)
                        .position(
                                new LatLng(gpsTracker.getLatitude(), gpsTracker
                                        .getLongitude())));

                //googleMap.addCircle(circleOptions);
                //animateMarker(mPositionMarker);
            }
        }
    }

    /**
     * Connects socket for updating driver's location
     */
    private void connectSocketForLocationUpdates() {

        try {
            socket = IO.socket(ConstantValues.BASE_URL);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    // socket.emit("foo", "hi");
                    // socket.disconnect();
                }

            }).on("event", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }
            });
            socket.connect();
            Utils.printLogs(TAG, "Socket ID : " + socket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChange(String latitude, String longitude) {
        try {
            Utils.printLogs(TAG, "Latitude : " + latitude +
                    "\nLongitude : " + longitude +
                    "\nSocket ID : " + socket.id() +
                    "\nDriverBickup ID : " + driverId +
                    "\nRide ID : " + ride.rideId);

            socket.on("driverLocation", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                }

            });
            socket.emit("driverLocation", driverId, Double.valueOf(latitude),
                    Double.valueOf(longitude), ride.rideId);

            currentLatLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            //destinationLatLng = new LatLng(30.707104, 76.690749);

            double lat = Double.valueOf(latitude);
            double lng = Double.valueOf(longitude);
            LatLng latLng = new LatLng(lat, lng);

            //points.add(latLng); //added
            //reDrawPolyLine(); //added

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Redraws the path on map as the current latlng gets changed
     */
    private void reDrawPolyLine() {

        // googleMap.clear();  //clears all Markers and Polylines


        PolylineOptions options = new PolylineOptions().width(20).color(
                getResources().getColor(R.color.appcolor)).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        //  addMarker(); //add Marker in current position
        line = googleMap.addPolyline(options); //add Polyline
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); //added
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Utils.printLogs(TAG, "Routing Failure : ... " + e.getMessage());
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i2) {
        Utils.printLogs(TAG, "Routing Success .. ");
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.blue));
            polyOptions.width(Utils.THICKNESS_OF_POLYLINE);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = googleMap.addPolyline(polyOptions);
            polylines.add(polyline);
        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}
