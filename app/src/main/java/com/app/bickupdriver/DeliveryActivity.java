package com.app.bickupdriver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickupdriver.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickupdriver.controller.AppConstants;
import com.app.bickupdriver.controller.AppController;
import com.app.bickupdriver.fragments.HistoryFragment;
import com.app.bickupdriver.fragments.OnGoingFragment;
import com.app.bickupdriver.interfaces.OpenratingInterface;
import com.app.bickupdriver.model.BaseArrayResponse;
import com.app.bickupdriver.restservices.ApiClientConnection;
import com.app.bickupdriver.restservices.ApiInterface;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Utils;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryActivity extends AppCompatActivity implements View.OnClickListener,
        InternetConnectionBroadcast.ConnectivityRecieverListener, OpenratingInterface {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private ImageView imgBack;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;
    private CoordinatorLayout mCoordinatorLayout;
    private String TAG = getClass().getSimpleName();
    private Context context = this;
    private ArrayList<com.app.bickupdriver.model.Response> completedResponseList;
    private ArrayList<com.app.bickupdriver.model.Response> missedResponseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_delivery2);
        // setupWindowAnimations();
        initializeViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getCompletedDeliveries();
        this.getMissedDeliveries();
    }

    /**
     * Gets the list of completed deliveries
     */
    private void getCompletedDeliveries() {

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

                Call<BaseArrayResponse> call = service.getListOfCompletedRides(accessToken);

                call.enqueue(new Callback<BaseArrayResponse>() {
                    @Override
                    public void onResponse(Call<BaseArrayResponse> call,
                                           Response<BaseArrayResponse> response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                BaseArrayResponse apiResponse = response.body();
                                Utils.printLogs(TAG, "onResponse : Success : -- ");

                                completedResponseList = apiResponse.response;
                            } else {
                                Utils.printLogs(TAG, "onResponse : Failure : -- " +
                                        response.body());
                            }

                           // DeliveryActivity.this.setupPagerAdapter();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseArrayResponse> call, Throwable t) {
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

    /**
     * Sets the pager adapter
     */
    private void setupPagerAdapter() {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Gets the list of missed deliveries
     */
    private void getMissedDeliveries() {

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

                Call<BaseArrayResponse> call = service.getListOfMissedRides(accessToken);

                call.enqueue(new Callback<BaseArrayResponse>() {
                    @Override
                    public void onResponse(Call<BaseArrayResponse> call,
                                           Response<BaseArrayResponse> response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                BaseArrayResponse apiResponse = response.body();
                                Utils.printLogs(TAG, "onResponse : Success : -- ");

                                missedResponseList = apiResponse.response;
                            } else {
                                Utils.printLogs(TAG, "onResponse : Failure : -- " +
                                        response.body());
                            }
                            DeliveryActivity.this.setupPagerAdapter();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseArrayResponse> call, Throwable t) {
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

    private void setupWindowAnimations() {
        Slide fade = (Slide) TransitionInflater.from(this)
                .inflateTransition(R.transition.slide_transition);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    private void initializeViews() {
        mActivityreference = this;
        //mCoordinatorLayout=(CoordinatorLayout)findViewById(R.id.main_content);
        TextView txtHeader = (TextView) findViewById(R.id.txt_activty_header);
        txtHeader.setText(getResources().getString(R.string.txt_deliveries));
        imgBack = (ImageView) findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delivery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backImage_header:
                finish();
                break;
        }
    }

    @Override
    public void openDialog() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.booking_container);
        EasyFlipView easyFlipView = (EasyFlipView) findViewById(R.id.flip_view);
        relativeLayout.setVisibility(View.VISIBLE);
        easyFlipView.flipTheView();
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_delivery, container,
                    false);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new OnGoingFragment(completedResponseList);
                    break;
                case 1:
                    fragment = new HistoryFragment(missedResponseList);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.txt_completed);
                case 1:
                    return getResources().getString(R.string.txt_missed);
            }
            return null;
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
            //showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

   /* public void showSnackBar(String mString) {
        snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();
    }*/

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else {
            //showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }
}
