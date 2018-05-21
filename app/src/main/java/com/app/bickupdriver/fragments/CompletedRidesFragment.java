package com.app.bickupdriver.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.adapter.BookingsAdapter;
import com.app.bickupdriver.adapter.DeliveriesAdapter;
import com.app.bickupdriver.controller.AppConstants;
import com.app.bickupdriver.model.BaseArrayResponse;
import com.app.bickupdriver.model.Response;
import com.app.bickupdriver.restservices.ApiClientConnection;
import com.app.bickupdriver.restservices.ApiInterface;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class CompletedRidesFragment extends Fragment {
    private static final String TAG = "HISTORY";
    private ListView listView;
    private Activity activity;
    //private String[] array = {"Single Bed", "Refrigerator", "Single Bed"};
    //TextView textView;

    public CompletedRidesFragment() {
        // Required empty public constructor
    }

    /*@SuppressLint("ValidFragment")
    public CompletedRidesFragment(ArrayList<Response> responseList) {
        //this.responseList = responseList;
        Utils.printLogs("HISTORY", "Opening CompletedRidesFragment with ResponseList  : " + responseList);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ongoing_deliveries, container, false);
        listView = (ListView) view.findViewById(R.id.delivery_completed);
        getCompletedDeliveries();
        return view;
    }

    /**
     * Gets the list of completed deliveries
     */
    private void getCompletedDeliveries() {
        Utils.printLogs(TAG, "Getting completed Deliveries ... ");
        ApiInterface service;
        try {
            if (Utils.isNetworkAvailable(getContext())) {

                final ProgressDialog progressDialog = Utils.showProgressDialog(getContext(),
                        AppConstants.DIALOG_PLEASE_WAIT);

                service = ApiClientConnection.getInstance().createService();

                SharedPreferences sharedPreferences = getContext().getSharedPreferences(
                        ConstantValues.USER_PREFERENCES,
                        Context.MODE_PRIVATE);

                String accessToken = sharedPreferences.getString(
                        ConstantValues.USER_ACCESS_TOKEN, "");

                Call<BaseArrayResponse> call = service.getListOfCompletedRides(accessToken);

                call.enqueue(new Callback<BaseArrayResponse>() {
                    @Override
                    public void onResponse(Call<BaseArrayResponse> call,
                                           retrofit2.Response<BaseArrayResponse> response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                BaseArrayResponse apiResponse = response.body();
                                Utils.printLogs(TAG, "onResponse : Success : -- ");

                                DeliveriesAdapter deliveriesAdapter = new DeliveriesAdapter(activity.getApplicationContext(),
                                        0, apiResponse.response);
                                listView.setAdapter(deliveriesAdapter);
                                Utils.printLogs(TAG, "Completed deliveries response retrieved successfully .. ");
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
