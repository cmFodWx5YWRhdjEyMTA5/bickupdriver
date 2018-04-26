package com.app.bickupdriver.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.utility.ConstantValues;


public class InviteAndEarnScreen extends Fragment {




    public static String TAG=InviteAndEarnScreen.class.getSimpleName();
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Context mActivityReference;


    public InviteAndEarnScreen() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTypefaceRegular= Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_BOLD);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_invite_and_earn_screen, container, false);
        initializeViews(view);
        return view;
    }


    private void initializeViews(View view) {
        TextView txtEarn,txtYourBank,txtRefferel,txtCode;
        txtEarn=(TextView)view.findViewById(R.id.earn_txt_1);
        txtYourBank=(TextView)view.findViewById(R.id.txt_your_bank);
        txtRefferel=(TextView)view.findViewById(R.id.txt_reffral_code);
        txtCode=(TextView)view.findViewById(R.id.txt_code);



    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference=(Activity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
