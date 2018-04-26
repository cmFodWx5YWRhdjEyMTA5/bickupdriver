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
import com.app.bickupdriver.utility.ConstantValues;

import at.blogc.android.views.ExpandableTextView;


public class FaqFragment extends Fragment implements View.OnClickListener {

    public static String TAG=FaqFragment.class.getSimpleName();
    private Activity mActivityReference;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private ExpandableTextView txt9;
    private ExpandableTextView txt10;
    private ExpandableTextView txt11;
    private ExpandableTextView txt12;
    private ExpandableTextView txt13;

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
        View view=inflater.inflate(R.layout.fragment_faq, container, false);
        initializeViews(view);
        return view;
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



         txt9=(ExpandableTextView) rootView.findViewById(R.id.expended_text_1);
         txt10=(ExpandableTextView) rootView.findViewById(R.id.expended_text_2);
         txt11=(ExpandableTextView) rootView.findViewById(R.id.expended_text_3);
         txt12=(ExpandableTextView) rootView.findViewById(R.id.expended_text_4);
         txt13=(ExpandableTextView) rootView.findViewById(R.id.expended_text_5);

        txt9.setTypeface(mTypefaceRegular);
        txt10.setTypeface(mTypefaceRegular);
        txt11.setTypeface(mTypefaceRegular);
        txt12.setTypeface(mTypefaceRegular);
        txt13.setTypeface(mTypefaceRegular);


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
               expandTextView(txt9,(ImageView)view);
                break;
            case R.id.img2:
                expandTextView(txt10,(ImageView)view);
                break;
            case R.id.img3:
                expandTextView(txt11,(ImageView)view);
                break;
            case R.id.img4:
                expandTextView(txt12,(ImageView)view);
                break;
            case R.id.img5:
                expandTextView(txt13,(ImageView)view);
                break;

        }

    }

    private void expandTextView(ExpandableTextView txt, ImageView view) {
        if(txt.isExpanded()){
            view.setImageResource(R.drawable.down_arrow);
            txt.collapse();
        }else {
            view.setImageResource(R.drawable.up_arrow);
            txt.expand();
        }

    }
}
