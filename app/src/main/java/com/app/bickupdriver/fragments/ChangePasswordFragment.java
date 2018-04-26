package com.app.bickupdriver.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickupdriver.R;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;


public class ChangePasswordFragment extends Fragment  implements View.OnClickListener {


    public static String TAG=ChangePasswordFragment.class.getSimpleName();

    private EditText edtNewPassword;
    private EditText edtConfirmPassword;
    private Button btnSubmit;
    private TextView txtHeading;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Activity mActivity;
    private EditText edtOldPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTypefaceRegular= Typeface.createFromAsset(mActivity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(mActivity.getAssets(), ConstantValues.TYPEFACE_BOLD);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_change_password, container, false);
        initializeViews(view);
        return view;
    }
    private void initializeViews(View view) {
        RelativeLayout container=(RelativeLayout)view.findViewById(R.id.reset_container);
        edtNewPassword=(EditText)view.findViewById(R.id.edt_new_password_reset);
        edtOldPassword=(EditText)view.findViewById(R.id.edt_old_password_reset);
        edtConfirmPassword=(EditText)view.findViewById(R.id.edt_confirm_password_reset);
        btnSubmit=(Button)view.findViewById(R.id.btn_submit__reset);
        txtHeading=(TextView)view.findViewById(R.id.tv_heading_reset);

        btnSubmit.setOnClickListener(this);

        txtHeading.setTypeface(mTypefaceRegular);
        edtConfirmPassword.setTypeface(mTypefaceRegular);
        edtNewPassword.setTypeface(mTypefaceRegular);
        edtOldPassword.setTypeface(mTypefaceRegular);
        btnSubmit.setTypeface(mTypefaceRegular);
        setUIToHideKeyBoard(container);
    }

    private boolean validateFields() {
        if(!CommonMethods.getInstance().validateEditFeild(edtOldPassword.getText().toString())){
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_old_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateEditFeild(edtNewPassword.getText().toString())){
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_password), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(edtNewPassword.getText().toString().length()<8){
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_password_strength), Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        if(!CommonMethods.getInstance().validateEditFeild(edtConfirmPassword.getText().toString())){
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_confirm_password), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(!edtNewPassword.getText().toString().equalsIgnoreCase(edtConfirmPassword.getText().toString())){
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_confirm_password_and_password), Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }





    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(Activity) activity;
    }

    //hide keyboard onTouch outSide the EditText
    public void setUIToHideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    CommonMethods.getInstance().hideSoftKeyBoard(mActivity);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUIToHideKeyBoard(innerView);
            }
        }

    }

    @Override
    public void onClick(View view) {

        int id=view.getId();
        switch (id){
            case R.id.btn_submit__reset:
                if(validateFields()) {
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
