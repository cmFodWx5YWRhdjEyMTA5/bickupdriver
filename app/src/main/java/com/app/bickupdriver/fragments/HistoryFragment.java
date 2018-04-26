package com.app.bickupdriver.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.adapter.BookingsAdapter;
import com.app.bickupdriver.adapter.DeliveriesAdapter;
import com.app.bickupdriver.model.Response;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    Activity activity;
    ListView listView;
    private String[] array={"Single Bed","Refrigerator","Single Bed"};
    private ArrayList<Response> responseList;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public HistoryFragment(ArrayList<Response> responseList) {
        this.responseList = responseList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history, container, false);
        listView = view.findViewById(R.id.delivery_history);
        DeliveriesAdapter deliveriesAdapter = new DeliveriesAdapter(activity.getApplicationContext(),
                1, responseList);
        listView.setAdapter(deliveriesAdapter);
        /*listView.setAdapter(new BookingsAdapter(activity.getApplicationContext(),
                activity,array,1, responseList));*/
        return view;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
