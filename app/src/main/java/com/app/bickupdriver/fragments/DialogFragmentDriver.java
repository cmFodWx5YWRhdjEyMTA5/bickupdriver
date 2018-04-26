package com.app.bickupdriver.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.app.bickupdriver.R;

/**
 * Created by fluper-pc on 9/10/17.
 */

public class DialogFragmentDriver extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rate_driver_dialog, container,
                false);
        getDialog().getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return rootView;

    }
}
