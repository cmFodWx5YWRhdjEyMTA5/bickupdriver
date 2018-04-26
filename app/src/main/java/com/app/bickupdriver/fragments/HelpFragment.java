package com.app.bickupdriver.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.utility.CMSActivity;
import com.app.bickupdriver.utility.ConstantValues;


public class HelpFragment extends Fragment implements View.OnClickListener {


    private Activity mActivityReference;
    private CMSActivity cmsActivity;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;

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
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    private void initializeViews(View rootView) {

        TextView txt1=(TextView) rootView.findViewById(R.id.txt1);
        TextView txt2=(TextView) rootView.findViewById(R.id.txt2);
        TextView txt3=(TextView) rootView.findViewById(R.id.txt3);
        TextView txt4=(TextView) rootView.findViewById(R.id.txt4);
        TextView txt5=(TextView) rootView.findViewById(R.id.txt5);

        ImageView img1=(ImageView) rootView.findViewById(R.id.img1);
        ImageView img2=(ImageView) rootView.findViewById(R.id.img2);
        ImageView img3=(ImageView) rootView.findViewById(R.id.img3);
        ImageView img4=(ImageView) rootView.findViewById(R.id.img4);
        ImageView img5=(ImageView) rootView.findViewById(R.id.img5);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);






        txt1.setTypeface(mTypefaceRegular);
        txt2.setTypeface(mTypefaceRegular);
        txt3.setTypeface(mTypefaceRegular);
        txt4.setTypeface(mTypefaceRegular);
        txt5.setTypeface(mTypefaceRegular);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference=(Activity) context;
        cmsActivity=(CMSActivity)context;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id){
            case R.id.img1:
            case R.id.img2:
            case R.id.img3:
            case R.id.img4:
            case R.id.img5:
                cmsActivity.calltitlefragment();
                break;

        }

    }
}
