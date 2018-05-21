package com.app.bickupdriver.activity;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.adapter.DeliveriesTabAdapter;
import com.app.bickupdriver.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickupdriver.controller.AppConstants;
import com.app.bickupdriver.controller.AppController;
import com.app.bickupdriver.fragments.CompletedRidesFragment;
import com.app.bickupdriver.fragments.MissedRidesFragment;
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


    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private DeliveriesTabAdapter deliveriesTabAdapter;
    private ImageView imgBack;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;
    private CoordinatorLayout mCoordinatorLayout;
    private String TAG = "HISTORY";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery2);
        initializeViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void initializeViews() {
        mActivityreference = this;
        TextView txtHeader = (TextView) findViewById(R.id.txt_activty_header);
        txtHeader.setText(getResources().getString(R.string.txt_deliveries));
        imgBack = (ImageView) findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        mViewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        deliveriesTabAdapter = new DeliveriesTabAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(deliveriesTabAdapter);
        tabLayout.setupWithViewPager(mViewPager);
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
