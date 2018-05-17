package com.app.bickupdriver.fragments;

import android.app.Activity;
import android.content.Context;
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
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.app.bickupdriver.R.id.btn_submit_on_changeNumber;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ForgotPassword extends Fragment implements View.OnClickListener,NetworkCallBack {

    private EditText edtMobileNumber;
    private ResetAndForgetPasswordActivity mActivityReference;
    private TextView txtheading;
    private Button btnSubmit;
    private Activity mActivity;



    public static String TAG=ForgotPassword.class.getSimpleName();
    private RelativeLayout container;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private int changeNumber=0;
    private final  int REQUEST_FORGOT_PASSWORD=100;
    private final  int REQUEST_CHANGE_NUMBER=101;
    private  String message;
    private CircularProgressView circularProgressBar;
    private TextView txtCountryCode;
    private String countryCode="+91";

    public ForgotPassword() {
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
        View view=inflater.inflate(R.layout.fragment_forgot_password, container, false);
        intitializeViews(view);
        return view;
    }

    private void intitializeViews(View view) {
        if(getArguments()!=null){
          changeNumber=  getArguments().getInt(ConstantValues.CHANGE_NUMBER,0);
        }
        mActivity=getActivity();
        circularProgressBar=(CircularProgressView)view.findViewById(R.id.progress_view);
        txtheading=(TextView)view.findViewById(R.id.txt_forgot_heading) ;
        container=(RelativeLayout)view.findViewById(R.id.forgot_container);

        setUIToHideKeyBoard(container);
        RelativeLayout rl_changeNumber=(RelativeLayout)view.findViewById(R.id.rl_change_number);
        RelativeLayout rl_forgot=(RelativeLayout)view.findViewById(R.id.rl_forgot_password);
        if(ResetAndForgetPasswordActivity.isChangeNumber){
            rl_changeNumber.setVisibility(View.VISIBLE);
            rl_forgot.setVisibility(View.GONE);
            edtMobileNumber=(EditText)view.findViewById(R.id.edt_mobile_number_signup);
            view.findViewById(btn_submit_on_changeNumber).setOnClickListener(this);
            txtCountryCode=(TextView)view.findViewById(R.id.txt_country_code) ;
            txtCountryCode.setOnClickListener(this);
            txtCountryCode.setTypeface(mTypefaceRegular);
        }else {
            rl_changeNumber.setVisibility(View.GONE);
            rl_forgot.setVisibility(View.VISIBLE);
            edtMobileNumber=(EditText)view.findViewById(R.id.edt_mobile_forgot);
            btnSubmit=(Button)view.findViewById(R.id.btn_submit_on_forgot);
            btnSubmit.setOnClickListener(this);
            btnSubmit.setTypeface(mTypefaceRegular);
            txtCountryCode=(TextView)view.findViewById(R.id.txt_country_code_forgot) ;
            txtCountryCode.setOnClickListener(this);

        }
        edtMobileNumber.setTypeface(mTypefaceRegular);
        txtheading.setTypeface(mTypefaceRegular);
    }

    public  void  openCountryCodeDialog(){
        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                picker.dismiss();
                countryCode=dialCode;
                txtCountryCode.setText(dialCode);
            }
        });
        picker.show(mActivityReference.getSupportFragmentManager(), "COUNTRY_PICKER");

    }

    private boolean validateFields() {
        if(!CommonMethods.getInstance().validateEditFeild(edtMobileNumber.getText().toString())){
            Toast.makeText(mActivityReference, mActivity.getResources().getString(R.string.txt_vaidate_mobile), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateMobileNumber(edtMobileNumber.getText().toString(),6)){
            Toast.makeText(mActivityReference, mActivity.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference= (ResetAndForgetPasswordActivity) context;

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
                    CommonMethods.getInstance().hideSoftKeyBoard(mActivityReference);
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
            case R.id.btn_submit_on_forgot:
                if(validateFields()) {
                   prepareforgotPasswordUser(edtMobileNumber.getText().toString().trim());
                }
                break;
            case R.id.txt_country_code:
                openCountryCodeDialog();
                break;
            case R.id.btn_submit_on_changeNumber:
                if(validateFields()) {
                    prepareforchangeNumber(edtMobileNumber.getText().toString().trim(),countryCode);
                }
                break;
            case R.id.txt_country_code_forgot:
                openCountryCodeDialog();
                break;

        }

    }
    public void  prepareforchangeNumber(String mobilenumber,String countryCode) {
        mobilenumber=mobilenumber.trim();
        String createUserUrl= WebAPIManager.getInstance().getchangeNumberUrl();
        createUserUrl=createUserUrl+mobilenumber;
        createUserUrl=createUserUrl+"&"+ConstantValues.COUNTRY_CODE+"="+countryCode;
        final JsonObject requestBody=new JsonObject();
        callAPI(requestBody,createUserUrl,this,60*1000,REQUEST_CHANGE_NUMBER);
    }
    public void  prepareforgotPasswordUser( String number) {
        number=number.trim();
        String createUserUrl= WebAPIManager.getInstance().getforgotPasswordUrl();
        createUserUrl=createUserUrl+"phone_number="+number;
        final JsonObject requestBody=new JsonObject();
        callAPI(requestBody,createUserUrl,this,60*1000,REQUEST_FORGOT_PASSWORD);
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
            case REQUEST_FORGOT_PASSWORD:
                ParseForgotresponse parseForgotresponse=new ParseForgotresponse();
                parseForgotresponse.execute(data.toString());
                break;
            case REQUEST_CHANGE_NUMBER:
                ParseChangeNumber parseChangeNumber=new ParseChangeNumber();
                parseChangeNumber.execute(data.toString());
                break;
        }

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ResetAndForgetPasswordActivity.isForgotpassword=false;
    }

    class ParseForgotresponse extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String  message="",flag="";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObject1=jsonObject.getJSONObject("response");
                String mobileNumber=jsonObject1.getString("phone_number");
                String country_code=jsonObject1.getString("country_code");
                message = jsonObject.getString("message");
                map.put("message", message);
                map.put(ConstantValues.USER_MOBILENUMBER, mobileNumber);
                map.put(ConstantValues.COUNTRY_CODE,country_code);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            ResetAndForgetPasswordActivity.isForgotpassword=true;
            String message = hashMap.get("message");
            User.getInstance().setMobileNumber(hashMap.get(ConstantValues.USER_MOBILENUMBER));
            User.getInstance().setCountryCode(hashMap.get(ConstantValues.COUNTRY_CODE));
            mActivityReference.callOTPFragment(0);
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }




    class ParseChangeNumber extends AsyncTask<String,Void,HashMap<String,String>> {
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
            User.getInstance().setMobileNumber(hashMap.get("phone_number"));
            User.getInstance().setCountryCode(hashMap.get("country_code"));
            mActivityReference.callOTPFragment(1);
            Toast.makeText(mActivityReference,message,Toast.LENGTH_SHORT).show();
        }
    }
}
