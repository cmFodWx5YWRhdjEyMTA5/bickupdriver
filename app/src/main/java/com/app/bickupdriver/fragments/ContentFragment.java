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


public class ContentFragment extends Fragment {

    public static String TAG=ContentFragment.class.getSimpleName();
    private Activity activity;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_content, container, false);
        mTypefaceRegular= Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);

        TextView txt1=(TextView) view.findViewById(R.id.heading);
        TextView txt2=(TextView) view.findViewById(R.id.txt);
        txt1.setTypeface(mTypefaceRegular);
        txt2.setTypeface(mTypefaceRegular);
         return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;

    }

}
