package com.app.bickupdriver.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.bickupdriver.DeliveryActivity;
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


public class MissedRidesFragment extends Fragment {
    private String TAG = "HISTORY";
    Activity activity;
    ListView listView;
    private String[] array = {"Single Bed", "Refrigerator", "Single Bed"};
    //private ArrayList<Response> responseList;
    //private ArrayList<Response> completedResponseList;
    //private ArrayList<Response> missedResponseList;

    public MissedRidesFragment() {
        //this.responseList = responseList;
        // Required empty public constructor
    }

    /*@SuppressLint("ValidFragment")
    public MissedRidesFragment(ArrayList<Response> responseList) {
        //this.responseList = responseList;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        listView = view.findViewById(R.id.delivery_history);
        /*listView.setAdapter(new BookingsAdapter(activity.getApplicationContext(),
                activity,array,1, responseList));*/
        getMissedDeliveries();
        return view;
    }


    /**
     * Gets the list of missed deliveries
     */
    private void getMissedDeliveries() {
        Utils.printLogs(TAG, "Getting missed Deliveries ... ");
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

                Call<BaseArrayResponse> call = service.getListOfMissedRides(accessToken);

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
                                //missedResponseList = apiResponse.response;
                                DeliveriesAdapter deliveriesAdapter = new DeliveriesAdapter(activity.getApplicationContext(),
                                        1, apiResponse.response);
                                listView.setAdapter(deliveriesAdapter);
                                Utils.printLogs(TAG, "Missed Deliveries retrieved successfully ... ");
                            } else {
                                Utils.printLogs(TAG, "onResponse : Failure : -- " +
                                        response.body());
                            }
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
