package com.app.bickupdriver.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
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

import com.app.bickupdriver.activity.LoginActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.activity.ResetAndForgetPasswordActivity;
import com.app.bickupdriver.controller.NetworkCallBack;
import com.app.bickupdriver.controller.WebAPIManager;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ResetPassword extends Fragment implements View.OnClickListener,NetworkCallBack {
    public static String TAG=ResetPassword.class.getSimpleName();

    private ResetAndForgetPasswordActivity mActivityReference;
    private EditText edtNewPassword;
    private EditText edtConfirmPassword;
    private Button btnSubmit;
    private TextView txtHeading;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Activity mActivity;
    private final int REQUEST_RESET_PASSWORD=100;
    private CircularProgressView circularProgressBar;
    private String message;

    public ResetPassword() {
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
        View view=inflater.inflate(R.layout.fragment_reset_password, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        circularProgressBar=(CircularProgressView)view.findViewById(R.id.progress_view);
        RelativeLayout container=(RelativeLayout)view.findViewById(R.id.reset_container);
        edtNewPassword=(EditText)view.findViewById(R.id.edt_new_password_reset);
        edtConfirmPassword=(EditText)view.findViewById(R.id.edt_confirm_password_reset);
        btnSubmit=(Button)view.findViewById(R.id.btn_submit__reset);
        txtHeading=(TextView)view.findViewById(R.id.tv_heading_reset);

        btnSubmit.setOnClickListener(this);

        txtHeading.setTypeface(mTypefaceRegular);
        edtConfirmPassword.setTypeface(mTypefaceRegular);
        edtNewPassword.setTypeface(mTypefaceRegular);
        btnSubmit.setTypeface(mTypefaceRegular);
        setUIToHideKeyBoard(container);
    }

    private boolean validateFields() {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference=(ResetAndForgetPasswordActivity) context;
        mActivity=(Activity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

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
       // HandleForgotResetNavigations handleForgotResetNavigations=mActivityReference;
        int id=view.getId();
        switch (id){
            case R.id.btn_submit__reset:
                if(validateFields()) {
                    prepareresetPasswordUser(User.getInstance().getMobileNumber().trim(),edtConfirmPassword.getText().toString().trim());
                }
                break;
        }
    }


    public void  prepareresetPasswordUser( String oldPassword,String newPassword) {
        String createUserUrl= WebAPIManager.getInstance().getresetPasswordUrl();
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty(ConstantValues.USER_MOBILENUMBER,oldPassword);
        requestBody.addProperty(ConstantValues.USER_PASSWORD,newPassword);
        callAPI(requestBody,createUserUrl,this,60*1000,REQUEST_RESET_PASSWORD);
    }



    private void callAPI(JsonObject requestBody, String createUserUrl, final NetworkCallBack callBack, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        String accessToken= User.getInstance().getAccesstoken();
        Ion.with(this)
                .load("PUT",createUserUrl)
                .setHeader(ConstantValues.USER_ACCESS_TOKEN,accessToken)
                .setJsonObjectBody(requestBody)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        circularProgressBar.setVisibility(View.GONE);
                        if(e!=null){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                        String value=String.valueOf(resultObject);
                        try {
                            JSONObject jsonObject=new JSONObject(value);
                            message=jsonObject.getString("message");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        switch (status){
                            case 403:
                            case 400:
                            case 500:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 200:
                                callBack.onSuccess(resultObject,requestCode,status);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
    }

    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        switch (requestCode){
            case REQUEST_RESET_PASSWORD:
                ParseResetresponse parseResetresponse=new ParseResetresponse();
                parseResetresponse.execute(data.toString());
                break;
        }
    }

    @Override
    public void onError(String msg) {

    }


    class ParseResetresponse extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String  message="",flag="",mobilenumber="";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
                JSONObject jsonObject1=jsonObject.getJSONObject("response");
                String country_code=jsonObject1.getString("country_code");
                String phoneNumber=jsonObject1.getString("phone_number");
                map.put("country_code",country_code);
                map.put("phone_number",phoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            String message = hashMap.get("message");
            Toast.makeText(mActivityReference,message,Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(mActivityReference, LoginActivity.class);
            mActivityReference.startActivity(intent);
            mActivityReference.finishAffinity();
        }
    }
}
