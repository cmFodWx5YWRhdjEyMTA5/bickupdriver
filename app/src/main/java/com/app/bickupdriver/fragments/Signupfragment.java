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
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Signupfragment extends Fragment implements GetSocialLoginResultInterface, View.OnClickListener, NetworkCallBack {

    private static final String TAG = "DRIVER";
    private LoginActivity mActivityReference;
    private Activity activity;
    private EditText edtFirstname;
    private EditText edtLastname;
    private EditText edtMobileNumber;
    private EditText edtChoosePassword;
    private EditText edtConfirmPassword;
    private EditText edtEmailID;
    private Button btnSignUP;
    private ImageButton btnFacebook;
    private ImageButton btnGoogle;
    private TextView txtSignIn;
    private LinearLayout container;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private ImageView imgTermAndCondition;
    private CircularProgressView circularProgressBar;
    private String message;
    private TextView edtCoutryCode;
    private String countryCode = "+91";


    public Signupfragment() {
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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        InitializeViews(view);
        return view;
    }

    private void InitializeViews(View view) {
        activity = getActivity();
        circularProgressBar = (CircularProgressView) view.findViewById(R.id.progress_view);
        mTypefaceRegular = Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        container = (LinearLayout) view.findViewById(R.id.container_signup);
        edtFirstname = (EditText) view.findViewById(R.id.edt_first_name_signup);
        edtLastname = (EditText) view.findViewById(R.id.edt_last_name_signup);
        edtMobileNumber = (EditText) view.findViewById(R.id.edt_mobile_number_signup);
        edtChoosePassword = (EditText) view.findViewById(R.id.edt_choose_password_signup);
        edtConfirmPassword = (EditText) view.findViewById(R.id.edt_confirm_password_signup);
        edtEmailID = (EditText) view.findViewById(R.id.edt_email_signup);

        edtCoutryCode = (TextView) view.findViewById(R.id.edt_country_code);

        view.findViewById(R.id.edt_country_code).setOnClickListener(this);


        btnSignUP = (Button) view.findViewById(R.id.btn_submit_signup);
        btnFacebook = (ImageButton) view.findViewById(R.id.btn_facebook_signup);
        btnGoogle = (ImageButton) view.findViewById(R.id.btn_google_signup);
        txtSignIn = (TextView) view.findViewById(R.id.txt_signin_signup);
        imgTermAndCondition = (ImageView) view.findViewById(R.id.tick_image_signup);
        imgTermAndCondition.setTag(R.drawable.de_checkbox);

        txtSignIn.setOnClickListener(this);


        btnSignUP.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
        imgTermAndCondition.setOnClickListener(this);


        edtFirstname.setTypeface(mTypefaceRegular);
        edtLastname.setTypeface(mTypefaceRegular);
        edtMobileNumber.setTypeface(mTypefaceRegular);
        edtChoosePassword.setTypeface(mTypefaceRegular);
        edtEmailID.setTypeface(mTypefaceRegular);
        edtConfirmPassword.setTypeface(mTypefaceRegular);
        btnSignUP.setTypeface(mTypefaceRegular);
        txtSignIn.setTypeface(mTypefaceRegular);
        setUIToHideKeyBoard(container);
    }


    private boolean validateFields() {
        int tag = (int) imgTermAndCondition.getTag();
        if (!CommonMethods.getInstance().validateEditFeild(edtFirstname.getText().toString())) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_first_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CommonMethods.getInstance().validateEditFeild(edtLastname.getText().toString())) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_last_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CommonMethods.getInstance().validateMobileNumber(edtMobileNumber.getText().toString(), 6)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CommonMethods.getInstance().validateEmailAddress(edtEmailID.getText().toString())) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_emailID), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CommonMethods.getInstance().validateEditFeild(edtChoosePassword.getText().toString())) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_password), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (edtChoosePassword.getText().toString().length() < 8) {
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_password_strength), Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        if (!CommonMethods.getInstance().validateEditFeild(edtConfirmPassword.getText().toString())) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_confirm_password), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!edtChoosePassword.getText().toString().equalsIgnoreCase(edtConfirmPassword.getText().toString())) {
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_confirm_password_and_password), Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        if (tag == R.drawable.de_checkbox) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_vaidate_term_and_conditions), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
        HandleLoginSignUpNavigation HandleLoginSignUpNavigation = mActivityReference;
        int id = view.getId();
        switch (id) {
            case R.id.btn_submit_signup:
                if (validateFields()) {
                    String firstname = edtFirstname.getText().toString();
                    String lastname = edtLastname.getText().toString();
                    String mobilenumber = edtMobileNumber.getText().toString();
                    String password = edtConfirmPassword.getText().toString();
                    String email = edtEmailID.getText().toString();
                    prepareUnVerifiedUser(firstname.trim(), lastname.trim(), mobilenumber.trim(), email.trim(), password.trim(), countryCode.trim());
                }
                break;
            case R.id.txt_signin_signup:
                HandleLoginSignUpNavigation.callSigninFragment();
                break;
            case R.id.tick_image_signup:
                int tag = (int) imgTermAndCondition.getTag();
                if (tag == R.drawable.de_checkbox) {
                    setImageAndTag(R.drawable.ac_checkbox);
                } else {
                    if (tag == R.drawable.ac_checkbox) {
                        setImageAndTag(R.drawable.de_checkbox);
                    }
                }
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
                countryCode = dialCode;
                edtCoutryCode.setText(dialCode);
            }
        });
        picker.show(mActivityReference.getSupportFragmentManager(), "COUNTRY_PICKER");

    }

    private void setImageAndTag(int id) {
        imgTermAndCondition.setImageResource(id);
        imgTermAndCondition.setTag(id);
    }


    public void prepareUnVerifiedUser(final String firstname, final String lastname, final String mobileNumber, final String email, final String password, String countryCode) {
        String createUserUrl = WebAPIManager.getInstance().getCreateUserUrl();
        Utils.printLogs(TAG, "Create user URL : " + createUserUrl);
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty(ConstantValues.USER_FIRSTNAME, firstname);
        requestBody.addProperty(ConstantValues.USER_LASTNAME, lastname);
        requestBody.addProperty(ConstantValues.USER_EMAILADDRESS, email);
        requestBody.addProperty(ConstantValues.USER_MOBILENUMBER, mobileNumber);
        requestBody.addProperty(ConstantValues.USER_PASSWORD, password);
        requestBody.addProperty(ConstantValues.COUNTRY_CODE, countryCode);
        requestBody.addProperty(ConstantValues.DEVICE_TOKEN, FirebaseInstanceId.getInstance().getToken());
        //CommonMethods.getUniqueDeviceId(mActivityReference));
        createUnVerifieduser(requestBody, createUserUrl, this, 60 * 1000, 101);
    }

    private void createUnVerifieduser(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load(createUserUrl)
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
                        int status = result.getHeaders().code();

                        JsonObject resultObject = result.getResult();
                        //Utils.printLogs(TAG, "Result Object : " + resultObject);
                        String value = String.valueOf(resultObject);
                        //Utils.printLogs(TAG, "Result Value : " + value);
                        try {
                            JSONObject jsonObject = new JSONObject(value);
                            Utils.printLogs(TAG, "Result Object " + resultObject);
                            message = jsonObject.getString("message");

                            JSONObject userObject = jsonObject.getJSONObject("response");
                            if (userObject != null) {
                                String driverId = userObject.getString("driver_id");
                                Utils.printLogs(TAG, "Driver Id : " + driverId);
                                String accessToken = userObject.getString("access_token");

                                String email = userObject.getString("email");
                                String phoneNumber = userObject.getString("phone_number");

                                Utils.printLogs(TAG, "Access Token : " + accessToken);
                                Utils.printLogs(TAG, "Email : " + email);
                                Utils.printLogs(TAG, "Phone Number : " + phoneNumber);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        switch (status) {
                            case 422:
                            case 400:
                            case 500:
                            case 200:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 201:
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
            case LoginFragment.REQUEST_CREATEUSER:
                ParseCreateUserresponse parseCreateUserresponse = new ParseCreateUserresponse();
                parseCreateUserresponse.execute(data.toString());
                break;
        }
    }

    @Override
    public void onError(String msg) {

    }


    class ParseCreateUserresponse extends AsyncTask<String, Void, HashMap<String, String>> {

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
                map.put("message", message);
                map.put("flag", flag);

                map.put(ConstantValues.USER_EMAILADDRESS, data.getString("email"));
                map.put(ConstantValues.USER_MOBILENUMBER, data.getString("phone_number"));
                map.put(ConstantValues.USER_ID, data.getString("driver_id"));
                map.put(ConstantValues.USER_ACCESS_TOKEN, data.getString("access_token"));
                map.put(ConstantValues.USER_FIRSTNAME, data.getString("first_name"));
                map.put(ConstantValues.USER_LASTNAME, data.getString("last_name"));
                map.put(ConstantValues.COUNTRY_CODE, data.getString("country_code"));


                /**
                 * Save is_shopkeeper key into SharedPreferences.
                 */
                SharedPreferencesManager sharedPrefManager = new SharedPreferencesManager(getContext());
                // TODO: 16/5/18  Change this Fixed Field
                String referalCodeFromServer = data.getString("referal_code");
                Utils.printLogs(TAG, "Referal Code ---- : -- " + referalCodeFromServer);
                sharedPrefManager.saveStringData(SharedPreferencesManager.REFERRAL_CODE, referalCodeFromServer);


                try {
                    JSONArray jsonArray = data.getJSONArray("profile_image");
                    JSONObject imageObject = jsonArray.getJSONObject(0);
                    String insurenceFrontImage = imageObject.getString("image_url");
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
            circularProgressBar.setVisibility(View.GONE);
            String flag = hashMap.get("flag");
            String message = hashMap.get("message");
            User.getInstance().createUser(activity, hashMap.get(ConstantValues.USER_ACCESS_TOKEN), hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_ID), hashMap.get(ConstantValues.USER_MOBILENUMBER), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), hashMap.get(ConstantValues.USER_PASSWORD), false, false, hashMap.get(ConstantValues.COUNTRY_CODE), hashMap.get(ConstantValues.USER_IMAGE));
            mActivityReference.callForgotAndReset(ConstantValues.OTP, 1);
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }


}
