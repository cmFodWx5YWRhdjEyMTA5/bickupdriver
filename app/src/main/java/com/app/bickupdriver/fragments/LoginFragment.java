package com.app.bickupdriver.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickupdriver.activity.LoginActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.controller.NetworkCallBack;
import com.app.bickupdriver.controller.WebAPIManager;
import com.app.bickupdriver.interfaces.GetSocialLoginResultInterface;
import com.app.bickupdriver.interfaces.HandleLoginSignUpNavigation;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.SharedPreferencesManager;
import com.app.bickupdriver.utility.Utils;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;


public class LoginFragment extends Fragment implements GetSocialLoginResultInterface, View.OnClickListener, NetworkCallBack {


    private LoginActivity mActivityReference;
    private Activity activity;
    private EditText edtMobileNumber;
    private EditText edtPassword;
    private Button btnLogin;
    private ImageButton btnfacebook;
    private ImageButton btnGoogle;
    private TextView tvForgotPassword;
    private TextView tvSignUP;
    private LinearLayout container;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private String message;

    public static final int REQUEST_CREATEUSER = 101;
    public static final int REQUEST_SOCIAL_LOGIN = 102;
    public static final int REQUEST_SOCIAL_SIGNUP = 104;
    public static final int REQUEST_LOGIN = 103;
    private CircularProgressView circularProgressBar;
    private TextView countryCode;
    private String countryCodevalue = "+91";
    private String TAG = "REFERRAL";


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        activity = getActivity();
        circularProgressBar = (CircularProgressView) view.findViewById(R.id.progress_view);
        mTypefaceRegular = Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        container = (LinearLayout) view.findViewById(R.id.container_login);

        countryCode = view.findViewById(R.id.edt_country_code);
        countryCode.setOnClickListener(this);

        edtMobileNumber = (EditText) view.findViewById(R.id.edt_mobile_login);
        edtPassword = (EditText) view.findViewById(R.id.edt_password_login);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnfacebook = (ImageButton) view.findViewById(R.id.btn_facebook_login);
        btnGoogle = (ImageButton) view.findViewById(R.id.btn_google_login);
        tvForgotPassword = (TextView) view.findViewById(R.id.txt_forgot_password_login);
        tvSignUP = (TextView) view.findViewById(R.id.txt_signup_login);

        tvSignUP.setTypeface(mTypefaceRegular);
        tvForgotPassword.setTypeface(mTypefaceRegular);
        btnLogin.setTypeface(mTypefaceRegular);
        edtMobileNumber.setTypeface(mTypefaceRegular);
        edtPassword.setTypeface(mTypefaceRegular);
        setUIToHideKeyBoard(container);
        btnLogin.setOnClickListener(this);
        tvSignUP.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        tvSignUP.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }


    private boolean validateFields() {
        if (!CommonMethods.getInstance().validateMobileNumber(edtMobileNumber.getText().toString(), 6)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!CommonMethods.getInstance().validateEditFeild(edtPassword.getText().toString())) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_password), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (edtPassword.getText().toString().length() < 8) {
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_password_strength), Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }

    @Override
    public void onResume() {
        if (edtMobileNumber != null && edtPassword != null) {
            edtMobileNumber.setText("");
            edtPassword.setText("");
        }
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference = (LoginActivity) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void getLoginresult(User mUser) {
    }

    //hide keyboard onTouch outSide the EditText
    public void setUIToHideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    CommonMethods.getInstance().hideSoftKeyBoard(activity);
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
        HandleLoginSignUpNavigation mHandleLoginSignUpNavigation = mActivityReference;
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                if (validateFields()) {
                    String mobileNumber = edtMobileNumber.getText().toString();
                    String password = edtPassword.getText().toString();
                    prepareuserLogin(mobileNumber.trim(), password.trim());
                }
                break;
            case R.id.txt_signup_login:
                mHandleLoginSignUpNavigation.callSignupFragment();
                break;
            case R.id.txt_forgot_password_login:
                mHandleLoginSignUpNavigation.callForgotAndReset(ConstantValues.FORGOT_PASSWORD, 0);
                break;
            case R.id.edt_country_code:
                openCountryCodeDialog();
                break;

        }
    }

    public void openCountryCodeDialog() {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                picker.dismiss();
                countryCodevalue = dialCode;
                countryCode.setText(dialCode);
            }
        });
        picker.show(mActivityReference.getSupportFragmentManager(), "COUNTRY_PICKER");

    }
    // User Login process

    public void prepareuserLogin(final String mobileNumber, final String password) {
        String createUserUrl = WebAPIManager.getInstance().getUserLoginUrl();
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty(ConstantValues.USER_MOBILENUMBER, mobileNumber);
        requestBody.addProperty(ConstantValues.USER_PASSWORD, password);
        requestBody.addProperty(ConstantValues.COUNTRY_CODE, countryCodevalue);
        requestBody.addProperty(ConstantValues.DEVICE_TOKEN,
                FirebaseInstanceId.getInstance().getToken());
        //CommonMethods.getUniqueDeviceId(mActivityReference));
        userLogin(requestBody, createUserUrl, this, 60 * 1000, REQUEST_LOGIN);
    }


    private void userLogin(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Log.e(createUserUrl, requestBody.toString());
        Ion.with(this)
                .load("POST", createUserUrl)
                .setJsonObjectBody(requestBody)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        circularProgressBar.setVisibility(View.GONE);
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.e("LoginFragment", result.getResult().toString());
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                        String value = String.valueOf(resultObject);
                        try {
                            JSONObject jsonObject = new JSONObject(value);
                            message = jsonObject.getString("message");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        switch (status) {
                            case 422:
                            case 400:
                            case 500:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 200:
                            case 202:
                                loginActivity.onSuccess(resultObject, requestCode, status);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        switch (requestCode) {
            case REQUEST_LOGIN:
                ParseuserLoginResponse parseuserLoginResponse = new ParseuserLoginResponse();
                parseuserLoginResponse.execute(data.toString());
                break;
        }
    }

    @Override
    public void onError(String msg) {
    }


    class ParseuserLoginResponse extends AsyncTask<String, Void, HashMap<String, String>> {

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
                if (data.has("profile_status")) {
                    map.put("profile_status", data.getString("profile_status"));
                }
                map.put(ConstantValues.USER_EMAILADDRESS, data.getString("email"));
                map.put(ConstantValues.USER_MOBILENUMBER, data.getString("phone_number"));
                map.put(ConstantValues.USER_ID, data.getString("driver_id"));
                map.put(ConstantValues.USER_ACCESS_TOKEN, data.getString("access_token"));
                map.put(ConstantValues.USER_FIRSTNAME, data.getString("first_name"));
                map.put(ConstantValues.USER_LASTNAME, data.getString("last_name"));
                map.put(ConstantValues.COUNTRY_CODE, data.getString("country_code"));

                /**
                 *
                 */

                /**
                 * Save is_shopkeeper key into SharedPreferences.
                 */
                SharedPreferencesManager sharedPrefManager = new SharedPreferencesManager(getContext());
                // TODO: 16/5/18  Change this Fixed Field
                String referalCodeFromServer = data.getString("referal_code");
                Utils.printLogs(TAG, "Referal Code ---- : -- " + referalCodeFromServer);
                sharedPrefManager.saveStringData(SharedPreferencesManager.REFERRAL_CODE, referalCodeFromServer);


                try {
                    JSONObject jsonArray = data.getJSONObject("profile_image");
                    String insurenceFrontImage = jsonArray.getString("image_url");
                    map.put(ConstantValues.USER_IMAGE, insurenceFrontImage);
                } catch (Exception e) {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            String flag = hashMap.get("flag");
            if (flag != null) {
                String message = hashMap.get("message");
                User.getInstance().createUser(activity,
                        hashMap.get(ConstantValues.USER_ACCESS_TOKEN),
                        hashMap.get(ConstantValues.USER_EMAILADDRESS),
                        hashMap.get(ConstantValues.USER_ID),
                        hashMap.get(ConstantValues.USER_MOBILENUMBER),
                        hashMap.get(ConstantValues.USER_FIRSTNAME),
                        hashMap.get(ConstantValues.USER_LASTNAME),
                        hashMap.get(ConstantValues.USER_PASSWORD),
                        false, false,
                        hashMap.get(ConstantValues.COUNTRY_CODE),
                        hashMap.get(ConstantValues.USER_IMAGE));
                if (flag.equalsIgnoreCase("0")) {
                    mActivityReference.callForgotAndReset(ConstantValues.OTP, 1);
                } else {
                    String profilestatus = hashMap.get("profile_status");
                    if (profilestatus != null) {
                        if (profilestatus.equalsIgnoreCase("1")) {
                            mActivityReference.callForgotAndReset(ConstantValues.PERSONAL_DETAILS, 0);
                        }
                        if (profilestatus.equalsIgnoreCase("2")) {
                            mActivityReference.callForgotAndReset(ConstantValues.LICENSE_NUMBER, 0);
                        }
                        if (profilestatus.equalsIgnoreCase("3")) {
                            mActivityReference.callForgotAndReset(ConstantValues.CIRTIFICATE, 0);
                        }
                        if (profilestatus.equalsIgnoreCase("4")) {
                            mActivityReference.callForgotAndReset(ConstantValues.INSURENCE, 0);
                        }
                        if (profilestatus.equalsIgnoreCase("5")) {
                            mActivityReference.callForgotAndReset(ConstantValues.TRADE_LICENSE, 0);
                        }
                        if (profilestatus.equalsIgnoreCase("6")) {
                            mActivityReference.callForgotAndReset(ConstantValues.VEHICAL_IMAGES, 0);
                        }
                        if (profilestatus.equalsIgnoreCase("7")) {
                            User.getInstance().setVarified(true, mActivityReference, true);
                            mActivityReference.callMainActivity();
                        }
                    }
                }
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            } else {
                mActivityReference.callForgotAndReset(ConstantValues.OTP, 1);
            }
        }
    }

}
