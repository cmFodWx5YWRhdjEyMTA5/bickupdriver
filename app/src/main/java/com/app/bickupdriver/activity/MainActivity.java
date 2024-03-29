package com.app.bickupdriver.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.adapter.RideListRecyclerAdapter;
import com.app.bickupdriver.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickupdriver.controller.AppConstants;
import com.app.bickupdriver.controller.AppController;
import com.app.bickupdriver.controller.location.LocationChangeCallback;
import com.app.bickupdriver.model.Revenue;
import com.app.bickupdriver.model.Ride;
import com.app.bickupdriver.model.ServerResult;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.restservices.ApiClientConnection;
import com.app.bickupdriver.restservices.ApiInterface;
import com.app.bickupdriver.restservices.ServerResponseRideDetails;
import com.app.bickupdriver.utility.CMSActivity;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.GpsTracker;
import com.app.bickupdriver.utility.MapUtils;
import com.app.bickupdriver.utility.SharedPrefManager;
import com.app.bickupdriver.utility.Utils;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.bickupdriver.utility.ConstantValues.KEY_CAMERA_POSITION;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        InternetConnectionBroadcast.ConnectivityRecieverListener,
        OnMapReadyCallback, View.OnClickListener, LocationChangeCallback, RoutingListener {

    private static final String TAG = "NAVIGATION";


    private Snackbar snackbar;
    private boolean mIsConnected;
    private Activity mActivityreference;
    private LocationManager mLocationmanager;
    private GoogleMap googleMap;
    private Button btnStartLoading;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    //private DuoDrawerLayout drawerLayout;
    private ImageView ivHamburgerIcon;
    private RecyclerView recyclerViewBookings;

    //private String TAG = getClass().getSimpleName();
    private Context context = this;
    private Socket socket;
    private String driverId;
    public static GpsTracker gpsTracker;
    private final int REQUEST_CODE_LOCATION_PERMISSION = 907;
    private String latitude, longitude;
    private TextView labelJobProvided, labelRevenue, valueJobProvided, valueRevenue,
            txtOnline, valueBookingPovided;
    private Polyline line;
    private LatLng currentLatLng;
    private LatLng dropLatLng;
    private LatLng pickupLatLng;
    private String destinationAddress, pickupAddress;
    private ArrayList<Ride> rideList;
    private PolylineOptions options;
    public static boolean isOngoing = false;

    private String rideId;
    private List<Polyline> polylines;
    private ImageView btnCurrentLocation;
    private ImageView ivNotification;


    /**
     * Emmit from both the apps.
     * If DriverBickup then use 1 .
     * If User then use 0
     */
    private int isDriver = 1;


    // Sliding Root Navigation.
    private SlidingRootNavBuilder slidingRootNavBuilder;
    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Utils(MainActivity.this).loadLocale();
        setContentView(R.layout.activity_main);


        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        String appLanguageCode = sharedPrefManager.getStringData(SharedPrefManager.APP_LANGUAGE);
        Utils.printLogs(TAG, "App Language Code : " + appLanguageCode);
        if (appLanguageCode.equals(ConstantValues.LANGUAGE_ARABIC_CODE)) {
            slidingRootNavBuilder = new SlidingRootNavBuilder(this)
                    .withMenuOpened(false)
                    .withContentClickableWhenMenuOpened(false)
                    .withSavedState(savedInstanceState)
                    .withGravity(SlideGravity.RIGHT)
                    .withMenuLayout(R.layout.drawer_layout);
            slidingRootNav = slidingRootNavBuilder.inject();
        } else {
            slidingRootNavBuilder = new SlidingRootNavBuilder(this)
                    .withMenuOpened(false)
                    .withContentClickableWhenMenuOpened(false)
                    .withSavedState(savedInstanceState)
                    .withGravity(SlideGravity.LEFT)
                    .withMenuLayout(R.layout.drawer_layout);
            slidingRootNav = slidingRootNavBuilder.inject();
        }

        mActivityreference = MainActivity.this;
        polylines = new ArrayList<>();


        intializeViews();
        // setLayoutForSmallTraveller();
        setGoogleMap();
        // CheckdrawerLayout.openDrawer(Gravity.RIGHT);


        String firebaseRegistrationToken = SharedPrefManager.getGcmRegistrationId(this);
        Utils.printLogs(TAG, "FCM Registration Token : " + firebaseRegistrationToken);
        this.checkForLocationPermission();
        this.getRevenueDetails();
        this.getRides();

        SharedPreferences sharedPreferences = getSharedPreferences(
                ConstantValues.USER_PREFERENCES,
                Context.MODE_PRIVATE);

        driverId = sharedPreferences.getString(
                ConstantValues.USER_ID, "");
        MainActivity.this.connectSocketForLocationUpdates();


        String accessToken = sharedPreferences.getString(
                ConstantValues.USER_ACCESS_TOKEN, "");
        Utils.printLogs(TAG, "Access Token : " + accessToken);
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
            gpsTracker = new GpsTracker(context, MainActivity.this);
            this.trackCurrentLocation();
            this.fetchRouteDetails(MainActivity.this.rideList);
            Utils.printLogs(TAG, "Permission Granted");
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
            Utils.printLogs(TAG, "Permission NOT Granted, Requesting for the same");
            return false;
        }
    }

    /**
     * Fetches the route for the ongoing booking/ride
     *
     * @param rideList Holds the Array list of Ride object
     */
    private void fetchRouteDetails(ArrayList<Ride> rideList) {
        Utils.printLogs(TAG, "Fetching route details ...");
        if (rideList != null) {
            for (int i = 0; i < rideList.size(); i++) {
                Utils.printLogs(TAG, "Ride completed Status : " + rideList.get(0).rideCompletedStatus);

                if (rideList.get(i).rideCompletedStatus == 1) {
                    isOngoing = true;
                    dropLatLng = new LatLng(
                            Double.valueOf(rideList.get(i).dropLatitude),
                            Double.valueOf(rideList.get(i).dropLongitude));
                    pickupLatLng = new LatLng(
                            Double.valueOf(rideList.get(i).pickupLatitude),
                            Double.valueOf(rideList.get(i).pickupLongitude));
                    destinationAddress = rideList.get(i).dropLocationAddress;
                    pickupAddress = rideList.get(i).pickupLocationAddress;
                    Utils.printLogs(TAG, "Pickup Address : " + pickupAddress);
                    Utils.printLogs(TAG, "Destination Address : " + destinationAddress);
                    /*String urlTopass = this.makeURL(gpsTracker.getLatitude(),
                            gpsTracker.getLongitude(),
                            destinationLatLng.latitude,
                            destinationLatLng.longitude,
                            Double.valueOf(rideList.get(i).pickupLatitude),
                            Double.valueOf(rideList.get(i).pickupLongitude));
                    new MapRouteAsyncTask(MainActivity.this, urlTopass).execute();*/
                    List<LatLng> latLngList = new ArrayList<>();
                    latLngList.add(pickupLatLng);
                    latLngList.add(dropLatLng);
                    zoomRoute(googleMap, latLngList);
                    drawRouteBetweenTwoPlaces(pickupLatLng, dropLatLng);
                    i = rideList.size();
                } else if (rideList.get(i).rideCompletedStatus == 0) {
                    btnCurrentLocation.setVisibility(View.VISIBLE);
                    Utils.currentLocation(googleMap, this);
                }
            }
        }
    }

    public static void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        googleMap.setPadding(100, 10, 100, 300);   // left, top, right, bottom
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 1;
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
     * Fetches revenue details from server
     */
    private void getRevenueDetails() {
        Utils.printLogs(TAG, "Getting revenue details from server ...");
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

                Call<ServerResult> call = service.getRevenues(accessToken);

                call.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call,
                                           Response<ServerResult> response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                ServerResult apiResponse = response.body();
                                Utils.printLogs(TAG, "onResponse : Success : -- " +
                                        apiResponse.response);
                                Revenue revenue = (new Gson()).fromJson(
                                        apiResponse.response.toString(), Revenue.class);
                                if (revenue != null) {
                                    valueBookingPovided.setText(
                                            String.valueOf(revenue.bookingsToAccept));
                                    valueRevenue.setText(String.valueOf(revenue.revenue));
                                    valueJobProvided.setText(String.valueOf(
                                            revenue.jobsProvidedByBickup));
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
                    public void onFailure(Call<ServerResult> call, Throwable t) {
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


    private void getRides() {
        Utils.printLogs(TAG, "Getting rides from server ...");
        try {
            ApiInterface apiInterface = ApiClientConnection.getInstance().createService();

            SharedPreferences sharedPreferences = getSharedPreferences(ConstantValues.USER_PREFERENCES, Context.MODE_PRIVATE);

            String accessToken = sharedPreferences.getString(
                    ConstantValues.USER_ACCESS_TOKEN, "");

            Utils.printLogs(TAG, "Access Token : " + accessToken);
            Call<ServerResponseRideDetails> call = apiInterface.getRideDetail(accessToken);
            call.enqueue(new Callback<ServerResponseRideDetails>() {
                @Override
                public void onResponse(Call<ServerResponseRideDetails> call, Response<ServerResponseRideDetails> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Response is successful ");

                        ServerResponseRideDetails serverResponseRideDetails = response.body();
                        ArrayList<Ride> rideArrayList = serverResponseRideDetails.rides;

                        if (rideArrayList != null) {
                            setBookingList(rideArrayList);
                            MainActivity.this.rideList = serverResponseRideDetails.rides;
                            MainActivity.this.fetchRouteDetails(rideArrayList);
                        }

                        for (Ride ride : rideList) {
                            rideId = ride.rideId;
                            Log.d(TAG, "Ride Id : " + rideId);
                        }

                    } else {
                        try {
                            Log.d(TAG, "Error while getting ride details : " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseRideDetails> call, Throwable t) {
                    Log.d(TAG, "Exception : " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Sets Google Map
     */
    private void setGoogleMap() {
        SupportMapFragment mAupportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mAupportMapFragment.getMapAsync(this);
    }

    /**
     * Initializes views
     */
    private void intializeViews() {

        ivNotification = findViewById(R.id.iv_notification_home);
        ivNotification.setOnClickListener(this);
        btnCurrentLocation = findViewById(R.id.btn_current_location);
        btnCurrentLocation.setOnClickListener(this);

        mTypefaceRegular = Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_BOLD);
        recyclerViewBookings = (RecyclerView) findViewById(R.id.ry_bookings);
        labelJobProvided = (TextView) findViewById(R.id.label_job_provided);
        valueJobProvided = (TextView) findViewById(R.id.value_job_provided);
        valueBookingPovided = (TextView) findViewById(R.id.value_booking_provided);
        labelRevenue = (TextView) findViewById(R.id.label_revenue);
        valueRevenue = (TextView) findViewById(R.id.value_revenue);
        // txtOnline = (TextView) findViewById(R.id.txt_online);
        labelJobProvided.setTypeface(mTypefaceRegular);
        valueJobProvided.setTypeface(mTypefaceBold);
        labelRevenue.setTypeface(mTypefaceRegular);
        valueRevenue.setTypeface(mTypefaceBold);
        valueBookingPovided.setTypeface(mTypefaceBold);
        //txtOnline.setTypeface(mTypefaceRegular);

        btnStartLoading = (Button) findViewById(R.id.btn_start_loading);
        btnStartLoading.setOnClickListener(this);
        btnStartLoading.setTypeface(mTypefaceRegular);
        ivHamburgerIcon = (ImageView) findViewById(R.id.iv_hamburger_icon);
        ivHamburgerIcon.setOnClickListener(this);


        findViewById(R.id.box_container).setOnClickListener(this);
        findViewById(R.id.bookingtxt_container).setOnClickListener(this);
        findViewById(R.id.menu_delivery).setOnClickListener(this);
        //    findViewById(R.id.menu_scheduled).setOnClickListener(this);
        findViewById(R.id.menu_setting).setOnClickListener(this);
        findViewById(R.id.userimage).setOnClickListener(this);
        findViewById(R.id.revenue_container).setOnClickListener(this);
        findViewById(R.id.contactus_container).setOnClickListener(this);
        findViewById(R.id.aboutus_container).setOnClickListener(this);
        findViewById(R.id.privacy_container).setOnClickListener(this);
        findViewById(R.id.faq_container).setOnClickListener(this);
        findViewById(R.id.invite_and_earn_container).setOnClickListener(this);
        findViewById(R.id.change_password_container).setOnClickListener(this);
        findViewById(R.id.help_container).setOnClickListener(this);
    }

    private void setBookingList(ArrayList<Ride> rides) {

        RideListRecyclerAdapter goodsImagesAdapter = new RideListRecyclerAdapter(mActivityreference, rides);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivityreference, LinearLayoutManager.HORIZONTAL, false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewBookings);

        recyclerViewBookings.setLayoutManager(mLayoutManager);
        recyclerViewBookings.setItemAnimator(new DefaultItemAnimator());
        recyclerViewBookings.setAdapter(goodsImagesAdapter);

        recyclerViewBookings.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(
                        recyclerView.getLayoutManager());
                int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (position == 0) {
                    btnStartLoading.setText(getResources().getString(R.string.txt_start_loading));
                } else {
                    btnStartLoading.setText(getResources().getString(R.string.txt_waiting_to_accept));
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkInternetconnection();
        if (AppController.getInstance() != null) {
            AppController.getInstance().setConnectivityListener(this);
        }

        setDrawerData();

        if (this.checkForLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Request location updates:
                //locationManager.requestLocationUpdates(provider, 400, 1, this);
                gpsTracker = new GpsTracker(context, MainActivity.this);
                if (gpsTracker.canGetLocation()) {
                    // this.updateLocationUI();
                }
            }
        }
    }

    private void setDrawerData() {
        // String fullname = User.getInstance().getFirstName() + " " + User.getInstance().getLastName();
        String fullname = User.getInstance().getFirstName();
        String email = User.getInstance().getEmail();
        ImageView imageView = (ImageView) findViewById(R.id.userimage);
        TextView username = (TextView) findViewById(R.id.user_name);
        TextView userEmail = (TextView) findViewById(R.id.user_email);
        String userImage = User.getInstance().getUserImage();
        if (username != null && userEmail != null) {
            username.setText(fullname);
            userEmail.setText(email);
        }
        if (CommonMethods.getInstance().validateEditFeild(userImage)) {
            Ion.with(imageView)
                    .placeholder(R.drawable.profile_placeholder)
                    .load(ConstantValues.BASE_URL + "/" + userImage);
        }
    }

    @Override
    public void onBackPressed() {

        if (slidingRootNav.isMenuOpened()) {
            slidingRootNav.closeMenu(true);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("")
                    .setMessage(getResources().getString(R.string.txt_close_app))
                    .setPositiveButton(getResources().getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }

                    })
                    .setNegativeButton(getResources().getString(R.string.txt_No), null)
                    .show();
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }

/*        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }

    // Set UI to hide keyoard onTouch outside the edittext
    public void setUIToHideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    CommonMethods.getInstance().hideSoftKeyBoard(MainActivity.this);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUIToHideKeyBoard(innerView);
            }
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

    public void showSnackBar(String mString) {
      /*  snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();*/
    }

    private void showPopUp(int choosetraveller) {
        final Dialog openDialog = new Dialog(mActivityreference);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.pick_up_dialog);
        //   openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView travellerName = (TextView) openDialog.findViewById(R.id.txt_traveller_name_dialog);

        TextView travellerCost = (TextView) openDialog.findViewById(R.id.txt_traveller_cost);

        Button btnAgree = (Button) openDialog.findViewById(R.id.btn_agree);
        travellerName.setTypeface(mTypefaceRegular);
        travellerCost.setTypeface(mTypefaceRegular);
        btnAgree.setTypeface(mTypefaceRegular);
        if (choosetraveller == 1) {
        }

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BookingDetailsAcceptRejectActivity.class);
                startActivity(intent);
                openDialog.dismiss();
            }
        });
        openDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.googleMap = googleMap;

            /*if (this.checkForLocationPermission()) {
                this.trackCurrentLocation();
            }*/
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Location location = null;

        if (googleMap != null) {

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {

                location = mLocationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
                outState.putParcelable(ConstantValues.KEY_LOCATION, location);
                super.onSaveInstanceState(outState);
                return;
            }
        }
    }

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
                        15);
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
                        .title("I am here")
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.iv_notification_home:

                Intent notificationIntent = new Intent(this, NotificationListActivity.class);
                startActivity(notificationIntent);
                break;

            case R.id.btn_current_location:
                MapUtils.currentLocation(googleMap, MainActivity.this);
                break;

            case R.id.iv_hamburger_icon:

                slidingRootNav.openMenu(true);

                break;
            case R.id.menu_delivery:
                Intent intent = new Intent(this, DeliveryActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_setting:
                Intent setting = new Intent(this, SettingsActivity.class);
                startActivity(setting);
                break;
            case R.id.userimage:
                Intent edit = new Intent(this, EditProfileActivity.class);
                startActivity(edit);
                break;
            case R.id.revenue_container:
                Intent revenue = new Intent(this, RevenueActivity.class);
                startActivity(revenue);
                break;
            case R.id.contactus_container:
                Intent cms = new Intent(this, CMSActivity.class);
                cms.putExtra(ConstantValues.CHOOSE_PAGE, 1);
                startActivity(cms);
                break;
            case R.id.aboutus_container:
                Intent cms1 = new Intent(this, CMSActivity.class);
                cms1.putExtra(ConstantValues.CHOOSE_PAGE, 3);
                startActivity(cms1);
                break;
            case R.id.privacy_container:
                Intent cms3 = new Intent(this, CMSActivity.class);
                cms3.putExtra(ConstantValues.CHOOSE_PAGE, 2);
                startActivity(cms3);
                break;
            case R.id.faq_container:
                Intent cms4 = new Intent(this, CMSActivity.class);
                cms4.putExtra(ConstantValues.CHOOSE_PAGE, 4);
                startActivity(cms4);
                break;
            case R.id.invite_and_earn_container:
                Intent cms5 = new Intent(this, CMSActivity.class);
                cms5.putExtra(ConstantValues.CHOOSE_PAGE, 5);
                startActivity(cms5);
                break;
            case R.id.change_password_container:
                Intent cms6 = new Intent(this, CMSActivity.class);
                cms6.putExtra(ConstantValues.CHOOSE_PAGE, 6);
                startActivity(cms6);
                break;
            case R.id.help_container:
                Intent cms7 = new Intent(this, CMSActivity.class);
                cms7.putExtra(ConstantValues.CHOOSE_PAGE, 7);
                startActivity(cms7);
                break;
            case R.id.btn_start_loading:
                break;
            case R.id.box_container:
            case R.id.bookingtxt_container:
                //showPopUp(1);
                break;
        }
    }

    /**
     * Connects socket for updating driver's location
     */
    private void connectSocketForLocationUpdates() {
        Utils.printLogs(TAG, "Connecting to Socket for Location Update ... ");
        try {
            socket = IO.socket(ConstantValues.BASE_URL);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Utils.printLogs(TAG, "Connected to Socket ... ");
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
                    Utils.printLogs(TAG, "Disconnected from socket ... ");
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
                    "\nDriverBickup ID : " + driverId);

            if (socket == null)
                MainActivity.this.connectSocketForLocationUpdates();
            socket.on("trackDriverLocation", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject jsonObject = (JSONObject) args[0];
                    Log.d(TAG, "DriverBickup's location from driver .. trackDriverLocation " + jsonObject);
                }
            });

            Log.d(TAG, "Ride id on Socket : " + rideId);
            if (rideId != null) {
                socket.emit("driverLocation", driverId, Double.valueOf(latitude), Double.valueOf(longitude), rideId, isDriver);
                // socket.emit("driverLocation", rideId, Double.valueOf(latitude), Double.valueOf(longitude));
            } else {
                socket.emit("driverLocation", driverId, Double.valueOf(latitude), Double.valueOf(longitude), null, isDriver);
                Log.d(TAG, "Ride id is NUll ");
            }

            currentLatLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            //destinationLatLng = new LatLng(30.707104, 76.690749);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.printLogs(TAG, "onRequestPermissionsResult : Permission Granted");
                    this.trackCurrentLocation();
                } else {
                    Utils.printLogs(TAG, "onRequestPermissionsResult : Permission NOT Granted");
                    this.trackCurrentLocation();
                }
                break;
        }
    }

    /**
     * Tracks current location of user
     */
    private void trackCurrentLocation() {


        gpsTracker = new GpsTracker(context, this);
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            this.updateLocationUI();
        } else {
            latitude = "";
            longitude = "";
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Utils.printLogs(TAG, "Routing Failure MainActivity : " + e.getMessage());
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
