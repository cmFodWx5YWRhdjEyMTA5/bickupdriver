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
import com.app.bickupdriver.utility.CMSActivity;
import com.app.bickupdriver.utility.ConstantValues;


public class ContactUsFragment extends Fragment implements View.OnClickListener{


    public static String TAG=ContactUsFragment.class.getSimpleName();
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Activity activity;
    private CMSActivity cmsActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView=inflater.inflate(R.layout.fragment_contact_us, container, false);
        mTypefaceRegular= Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);

        TextView txt1=(TextView) rootView.findViewById(R.id.txt1);
        TextView txt2=(TextView) rootView.findViewById(R.id.text2);
        TextView txt3=(TextView) rootView.findViewById(R.id.txt3);
        TextView txt4=(TextView) rootView.findViewById(R.id.txt4);

        rootView.findViewById(R.id.arrow1).setOnClickListener(this);
        rootView.findViewById(R.id.arrow2).setOnClickListener(this);
        rootView.findViewById(R.id.arrow3).setOnClickListener(this);
        rootView.findViewById(R.id.arrow4).setOnClickListener(this);


        TextView txt9=(TextView) rootView.findViewById(R.id.headind_txt1);
        TextView txt10=(TextView) rootView.findViewById(R.id.header2);

        txt9.setTypeface(mTypefaceRegular);
        txt10.setTypeface(mTypefaceRegular);
        txt1.setTypeface(mTypefaceRegular);
        txt2.setTypeface(mTypefaceRegular);
        txt3.setTypeface(mTypefaceRegular);
        txt4.setTypeface(mTypefaceRegular);
        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
        cmsActivity=(CMSActivity)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id){
            case R.id.arrow1:
            case R.id.arrow2:
            case R.id.arrow3:
            case R.id.arrow4:
                cmsActivity.calltitlefragment();
                break;

        }
    }
}
