package com.app.bickupdriver.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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


public class Otp extends Fragment implements View.OnClickListener,NetworkCallBack {

    public static String TAG=Otp.class.getSimpleName();
    private ResetAndForgetPasswordActivity mActivityReference;
    private EditText edtOTP;
    private TextView txtHeader;
    private TextView txtMobileNumber;
    private TextView txtResend;
    private Button btnSubmit;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Activity mActivity;
    private int changeNumber=0;
    private CircularProgressView circularProgressBar;
    private final  int  REQUEST_VERIFY_USER_FOR_SIGNUP=100;
    private final int REQUEST_VERIFY_USER_FOR_FORGOT_PASSWORD=101;
    private final int REQUEST_RESEND_OTP=102;
    private String message;

    public Otp() {
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
         View view=inflater.inflate(R.layout.fragment_otp, container, false);
        initializeviews(view);
        return view;
    }

    private void initializeviews(View view) {
        circularProgressBar=(CircularProgressView)view.findViewById(R.id.progress_view);
        if(getArguments()!=null) {
            changeNumber = getArguments().getInt(ConstantValues.CHANGE_NUMBER, 0);
        }
        if(changeNumber==1){
            view.findViewById(R.id.btn_change_number).setVisibility(View.VISIBLE);
            Button button=view.findViewById(R.id.btn_change_number);
            button.setTypeface(mTypefaceRegular);
            view.findViewById(R.id.btn_change_number).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivityReference.callForgotFragment(1);
                    ResetAndForgetPasswordActivity.isChangeNumber=true;
                }
            });
        }
        RelativeLayout container=(RelativeLayout)view.findViewById(R.id.otp_container);
        edtOTP=(EditText)view.findViewById(R.id.edt_mobile_otp);
        edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edtOTP.getText().toString();
                int textlength = edtOTP.getText().length();
                if(text.endsWith("-"))
                    return;
                if(textlength == 4)
                {
                    edtOTP.setText(new StringBuilder(text).insert(text.length()-1, "-").toString());
                    edtOTP.setSelection(edtOTP.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtHeader=(TextView)view.findViewById(R.id.txt_header_otp);
        txtMobileNumber=(TextView)view.findViewById(R.id.txt_mobile_otp);
        txtResend=(TextView)view.findViewById(R.id.txt_resend_Otp);
        btnSubmit=(Button)view.findViewById(R.id.btn_submit_otp);
        txtResend.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);

        txtResend.setTypeface(mTypefaceRegular);
        txtMobileNumber.setTypeface(mTypefaceBold);
        txtHeader.setTypeface(mTypefaceRegular);
        btnSubmit.setTypeface(mTypefaceRegular);
        edtOTP.setTypeface(mTypefaceRegular);
        txtMobileNumber.setText(User.getInstance().getCountryCode()+" "+User.getInstance().getMobileNumber());
        setUIToHideKeyBoard(container);
    }


    @Override
    public void onResume() {
        super.onResume();
        ResetAndForgetPasswordActivity.isChangeNumber=false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference=(ResetAndForgetPasswordActivity) context;
        mActivity=(Activity)context;

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
                    if(mActivity!=null) {
                        CommonMethods.getInstance().hideSoftKeyBoard(mActivity);
                    }
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

    private boolean validateFields() {
        if(!CommonMethods.getInstance().validateEditFeild(edtOTP.getText().toString())){
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_otp), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btn_submit_otp:
                if(validateFields()) {
                    if(ResetAndForgetPasswordActivity.isForgotpassword){
                        prepareVerifiedFORGOTUser(edtOTP.getText().toString().trim());
                    }else {
                        prepareVerifiedUser(edtOTP.getText().toString().trim());
                    }
                }
                break;
            case R.id.txt_resend_Otp:
               prepareResendOtp();
                break;
        }
    }

         // Flow for the User from Signup
    public void  prepareVerifiedUser( String otp) {
        otp=otp.replace("-","");
        String createUserUrl= WebAPIManager.getInstance().getVerifyUserUrl();
        createUserUrl=createUserUrl+"verification_code="+otp;
        final JsonObject requestBody=new JsonObject();
        callAPI(requestBody,createUserUrl,this,60*1000,REQUEST_VERIFY_USER_FOR_SIGNUP);
    }

    public void  prepareVerifiedFORGOTUser( String otp) {
        otp=otp.replace("-","");
        String mobileNumber=User.getInstance().getMobileNumber();
        String createUserUrl= WebAPIManager.getInstance().getVerifyUserUrl();
        createUserUrl=createUserUrl+"verification_code="+otp;
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty("password_otp",1);
        requestBody.addProperty("phone_number",mobileNumber);
        callAPIFORFORGOT(requestBody,createUserUrl,this,60*1000,REQUEST_VERIFY_USER_FOR_FORGOT_PASSWORD);
    }

    public void  prepareResendOtp() {
        String mobileNumber=User.getInstance().getMobileNumber();
        String createUserUrl = WebAPIManager.getInstance().getResenOTPdUrl();
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty(ConstantValues.USER_MOBILENUMBER,mobileNumber);
        callAPI(requestBody, createUserUrl, this, 60 * 1000, REQUEST_RESEND_OTP);
    }

    private void callAPIFORFORGOT(JsonObject requestBody, String createUserUrl, final NetworkCallBack callBack, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load("PUT",createUserUrl)
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
                            case 400:
                            case 500:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 200:
                                callBack.onSuccess(resultObject,requestCode,status);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
            case REQUEST_VERIFY_USER_FOR_SIGNUP:
                ParseVerifyForSignupResponse parseVerifyForSignupResponse=new ParseVerifyForSignupResponse();
                parseVerifyForSignupResponse.execute(data.toString());
                break;
            case REQUEST_VERIFY_USER_FOR_FORGOT_PASSWORD:
                ParseVerifyForSignupResponse parseVerifyForForgotResponse=new ParseVerifyForSignupResponse();
                parseVerifyForForgotResponse.execute(data.toString());
                break;
            case REQUEST_RESEND_OTP:
                ParseResendresponse parseResendresponse=new ParseResendresponse();
                parseResendresponse.execute(data.toString());
                break;
        }

    }

    @Override
    public void onError(String msg) {

    }

    class ParseResendresponse extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String  message;
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                map.put("message", message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            circularProgressBar.setVisibility(View.GONE);
            String message = hashMap.get("message");
            Toast.makeText(mActivity,message,Toast.LENGTH_SHORT).show();
        }
    }



    class ParseVerifyForSignupResponse extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email, accessToken, phoneNumber, userId, message, flag = "0";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("response");
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }



        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            circularProgressBar.setVisibility(View.GONE);
            String message = hashMap.get("message");
            if(ResetAndForgetPasswordActivity.isForgotpassword) {
                ResetAndForgetPasswordActivity.isForgotpassword=false;
                mActivityReference.callFragment(ConstantValues.RESET_PASSWORD);
            }else {
                User.getInstance().setVarified(true, mActivity, false);
                mActivityReference.callFragment(ConstantValues.PERSONAL_DETAILS);

            }
            Toast.makeText(mActivity,message,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ResetAndForgetPasswordActivity.isForgotpassword=false;
    }
}
