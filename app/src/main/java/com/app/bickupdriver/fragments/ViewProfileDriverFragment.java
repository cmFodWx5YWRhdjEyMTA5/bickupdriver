package com.app.bickupdriver.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickupdriver.activity.EditProfileActivity;
import com.app.bickupdriver.activity.ImagesActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.activity.ResetAndForgetPasswordActivity;
import com.app.bickupdriver.adapter.ViewPagerAdapter;
import com.app.bickupdriver.controller.NetworkCallBack;
import com.app.bickupdriver.controller.WebAPIManager;
import com.app.bickupdriver.interfaces.HandleDialogClick;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.bickupdriver.R.id.btn_change;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ViewProfileDriverFragment extends Fragment implements View.OnClickListener,NetworkCallBack,HandleDialogClick {

    public static String TAG=ViewProfileDriverFragment.class.getSimpleName();
    private ResetAndForgetPasswordActivity activity;
    private EditProfileActivity mActivityResponse;
    private String message;
    private CircularProgressView circularProgressBar;
    public static RelativeLayout viewpage_layout;
    private View view;
    private ArrayList<String> licenseImagesList;
    private ArrayList<String> cirtificateList;
    private ArrayList<String> insuranceList;
    private ArrayList<String> tradeInsuranceList;
    private ArrayList<String> vehicalImagesList;
    private String license_expiry_date,trade_license_paper_expiry_date,certificate_expiry_date,driver_insurance_expiry_date;
 private  TextView licenseExpiry,cirtificateExpiry,insuranceExpiry,tradeInsuranceExpiry;



    public ViewProfileDriverFragment() {
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
         view =inflater.inflate(R.layout.fragment_view_profile_driver, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        Activity activity=getActivityRefrence();
        TextView labelFullname,txtFullName,labelMobile,txtMobile,lablemEmail,txtEmail,labelbankName,txtBankName,labelAccountNumber,txtAccountNumber,labelConfirmAccount,txtConfirmAccount,txtDrivingLicence,txtlicenceExpiry,txtRegistration,txtRegistrationExpiry,txtInsurence,txtInsurenceExpiry,txtTrade,txtTradeExpiry;
         Button btnChange;
        circularProgressBar=(CircularProgressView)view.findViewById(R.id.progress_view);
        Typeface mTypefaceRegular= Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        Typeface  mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);

        btnChange=view.findViewById(btn_change);
        btnChange.setTypeface(mTypefaceRegular);
        btnChange.setOnClickListener(this);
        txtDrivingLicence=view.findViewById(R.id.txt_driving_license);

        txtDrivingLicence.setTypeface(mTypefaceRegular);


        txtRegistration=view.findViewById(R.id.txt_registration_cirtificate);
        viewpage_layout=view.findViewById(R.id.view_pager_layout);

        txtRegistration.setTypeface(mTypefaceRegular);
        txtInsurence=view.findViewById(R.id.txt_insurence_paper);
        txtInsurence.setTypeface(mTypefaceRegular);
        txtTrade=view.findViewById(R.id.txt_trade_license_paper);
        txtTrade.setTypeface(mTypefaceRegular);


//        labelFullname=view.findViewById(R.id.label_full_name);
        txtFullName=view.findViewById(R.id.edt_Value_full_name);
      //  labelFullname.setTypeface(mTypefaceRegular);
        txtFullName.setTypeface(mTypefaceRegular);
        txtMobile=view.findViewById(R.id.edt_mobile_number_signup);
        txtMobile.setTypeface(mTypefaceRegular);

     //   lablemEmail=view.findViewById(R.id.label_email);
        txtEmail=view.findViewById(R.id.edt_Value_email_id);
   //    lablemEmail.setTypeface(mTypefaceRegular);
        txtEmail.setTypeface(mTypefaceRegular);

    //    labelbankName=view.findViewById(R.id.label_bank_name);
        txtBankName=view.findViewById(R.id.edt_Value_bank_name);
      //  labelbankName.setTypeface(mTypefaceRegular);
        txtBankName.setTypeface(mTypefaceRegular);

   //     labelAccountNumber=view.findViewById(R.id.label_account_number);
        txtAccountNumber=view.findViewById(R.id.edt_Value_account_number);
   //     labelAccountNumber.setTypeface(mTypefaceRegular);
        txtAccountNumber.setTypeface(mTypefaceRegular);



        licenseExpiry=view.findViewById(R.id.txt_driving_license_expiry);
        cirtificateExpiry=view.findViewById(R.id.txt_registration_cirtificate_expiry);
        insuranceExpiry=view.findViewById(R.id.txt_insurence_paper_expiry);
        tradeInsuranceExpiry=view.findViewById(R.id.txt_trade_license_paper_expiry);





    }

    private Activity getActivityRefrence() {
        Activity mActivity;
        if(activity!=null){
            mActivity=activity;
        }else {
            mActivity=mActivityResponse;
        }
        return mActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareDriverDetails(view);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            this.activity=(ResetAndForgetPasswordActivity) activity;
            User.getInstance().setVarified(true, activity, true);
        }catch (Exception e){
            mActivityResponse=(EditProfileActivity)activity;
        }

    }

    @Override
    public void onClick(View view) {

        int id=view.getId();
        switch (id){
            case R.id.btn_change:
                ChangeProfileFragment changeProfileFragment=new ChangeProfileFragment();
                callFragment(changeProfileFragment);
                break;
        }
    }


    private void callFragment(Fragment fragment) {
        if (activity != null) {
            activity.callFragment(fragment);
        }else {
            mActivityResponse.callFragment(fragment);
          }


    }
    public void setUIToHideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Activity activity=getActivityRefrence();
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
    public void onResume() {
        super.onResume();


    }

    public void  prepareDriverDetails(View view) {
        String createUserUrl= WebAPIManager.getInstance().getUserDetailsUrl();
        final JsonObject requestBody=new JsonObject();
        callAPI(requestBody,createUserUrl,this,60*1000,100,view);
    }


    private void callAPI(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode,View view) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load(createUserUrl)
                .setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
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
        switch (requestCode){
            case 100:
                ParseDriverDetails parseuserLoginResponse=new ParseDriverDetails();
                parseuserLoginResponse.execute(String.valueOf(data));
                break;

        }
    }

    @Override
    public void onError(String msg) {
    }



    class ParseDriverDetails extends AsyncTask<String,Void,HashMap<String,String>> {

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email,accessToken,phoneNumber,userId,message,flag="0";
            HashMap<String,String> map=new HashMap<>();
            String response=strings[0];
            try {
                JSONObject jsonObject=new JSONObject(response);
                message= jsonObject.getString("message");
                flag= jsonObject.getString("flag");
                map.put("flag",flag);
                map.put("message",message);
                JSONObject data=jsonObject.getJSONObject("response");
                try {
                    map.put(ConstantValues.LICENSE_NUMBER_VALUE,data.getString("license_number"));
                    map.put(ConstantValues.LICENSE_EXPIRY,data.getString("license_expiry_date"));
                    license_expiry_date=data.getString("license_expiry_date");
                    JSONArray jsonArray=data.getJSONArray("license");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    String licenseFrontImage=imageObject.getString("image_url");
                    String licensebackImage=backImageObject.getString("image_url");
                    map.put(ConstantValues.LICENSE_FRONT,licenseFrontImage);
                    map.put(ConstantValues.LICENSE_BACK,licensebackImage);

                }catch (Exception e){
                }


                try {
                    map.put(ConstantValues.CIRTIFICATE_NUMBER,data.getString("certificate_registration_number"));
                    map.put(ConstantValues.CIRTIFICATE_EXPIRY,data.getString("certificate_expiry_date"));
                    certificate_expiry_date=data.getString("certificate_expiry_date");
                    JSONArray jsonArray=data.getJSONArray("certificate");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    String cirtificateFrontImage=imageObject.getString("image_url");
                    String cirtificatebackImage=backImageObject.getString("image_url");
                    map.put(ConstantValues.CIRTIFICATE_FRONT,cirtificateFrontImage);
                    map.put(ConstantValues.CIRTIFICATE_BACK,cirtificatebackImage);

                }catch (Exception e){
                }


                try {
                    map.put(ConstantValues.INSURENCE_NUMBER,data.getString("insurance_paper_number"));
                    map.put(ConstantValues.INSURENCE_NUMBER_EXPIRY,data.getString("driver_insurance_expiry_date"));
                    driver_insurance_expiry_date=data.getString("driver_insurance_expiry_date");
                    JSONArray jsonArray=data.getJSONArray("insurance");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    String insurenceFrontImage=imageObject.getString("image_url");
                    String insurencebackImage=backImageObject.getString("image_url");
                    map.put(ConstantValues.INSURENCE_FRONT,insurenceFrontImage);
                    map.put(ConstantValues.INSURENCE_BACK,insurencebackImage);
                }catch (Exception e){
                }


                try {
                    map.put(ConstantValues.TRADE_INSURENCE_NUMBER,data.getString("trade_license_paper_number"));
                 //   map.put(ConstantValues.INSURENCE_NUMBER,data.getString("insurance_paper_number"));

                    map.put(ConstantValues.TRADE_INSURENCE_NUMBER_EXPIRY,data.getString("trade_license_paper_expiry_date"));
                    trade_license_paper_expiry_date=data.getString("trade_license_paper_expiry_date");
                    JSONArray jsonArray=data.getJSONArray("trade_license_paper");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    String tradefrontImage=imageObject.getString("image_url");
                    String tradebackImage=backImageObject.getString("image_url");
                    map.put(ConstantValues.TRADE_FRONT,tradefrontImage);
                    map.put(ConstantValues.TRADE_BACK,tradebackImage);
                }catch (Exception e){
                }

                try {
                    JSONArray jsonArray=data.getJSONArray("vehicle");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    JSONObject rightImageObject=jsonArray.getJSONObject(2);
                    JSONObject leftImageObject=jsonArray.getJSONObject(3);
                    String tradefrontImage=imageObject.getString("image_url");
                    String tradebackImage=backImageObject.getString("image_url");
                    String rightImage=rightImageObject.getString("image_url");
                    String leftImage=leftImageObject.getString("image_url");
                    map.put(ConstantValues.VEHICAL_FRONT,tradefrontImage);
                    map.put(ConstantValues.VEHICAL_BACK,tradebackImage);
                    map.put(ConstantValues.VEHICAL_RIGHT,rightImage);
                    map.put(ConstantValues.VEHICAL_LEFT,leftImage);
                }catch (Exception e){
                }


                try {
                    JSONArray jsonArray=data.getJSONArray("profile_image");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    String insurenceFrontImage=imageObject.getString("image_url");
                    map.put(ConstantValues.USER_IMAGE,insurenceFrontImage);

                }catch (Exception e){
                }

                map.put(ConstantValues.BANK_NAME,data.getString("bank_name"));
                map.put(ConstantValues.ACCOUNT_NUMBER,data.getString("account_number"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            String flag=hashMap.get("flag");
            String message=hashMap.get("message");

                setDataToViews(hashMap);


        }
    }

    private void setDataToViews(HashMap<String, String> hashMap) {
        Activity activity=getActivityRefrence();
        final EditText txtFullName,txtMobile,txtEmail,txtBankName,txtAccountNumber;
        licenseImagesList=new ArrayList<>();
        cirtificateList=new ArrayList<>();
        insuranceList=new ArrayList<>();
        tradeInsuranceList=new ArrayList<>();
        vehicalImagesList=new ArrayList<>();
        CircleImageView licenseFront,licenseback,cirtificateFront,cirtificateBack,insuranceFront,insuranceback,tradeFront,TradeBack,vehivalfront,vehicalBack,vehicalRight,vehicalLeft,userImage,frontVehical,backVehical,leftVehical,rightVehical;
        txtFullName=view.findViewById(R.id.edt_Value_full_name);
        txtMobile=view.findViewById(R.id.edt_mobile_number_signup);
        txtEmail=view.findViewById(R.id.edt_Value_email_id);
        txtBankName=view.findViewById(R.id.edt_Value_bank_name);
        txtAccountNumber=view.findViewById(R.id.edt_Value_account_number);

        String countryCode=User.getInstance().getCountryCode();
       // txtFullName.setText(User.getInstance().getFirstName()+" "+User.getInstance().getLastName());
        txtFullName.setText(User.getInstance().getFirstName());
        txtMobile.setText(User.getInstance().getCountryCode()+" "+User.getInstance().getMobileNumber());
        txtEmail.setText(User.getInstance().getEmail());
        txtBankName.setText(hashMap.get(ConstantValues.BANK_NAME));
        txtAccountNumber.setText(hashMap.get(ConstantValues.ACCOUNT_NUMBER));







        txtAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = txtAccountNumber.getText().toString();
                int textlength = txtAccountNumber.getText().length();
                if (text.endsWith("-"))
                    return;
                if (textlength == 5||textlength==10||textlength==15) {
                    txtAccountNumber.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                    txtAccountNumber.setSelection(txtAccountNumber.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        licenseFront=(CircleImageView)view.findViewById(R.id.frontdrivingLicense);
        licenseback=(CircleImageView)view.findViewById(R.id.backdrivinglicense);
        cirtificateFront=(CircleImageView)view.findViewById(R.id.front_registration_Cirtificate);
        cirtificateBack=(CircleImageView)view.findViewById(R.id.back_registration_cirtificate);

        insuranceFront=(CircleImageView)view.findViewById(R.id.frontInsurence);
        insuranceback=(CircleImageView)view.findViewById(R.id.backInsurence);
        tradeFront=(CircleImageView)view.findViewById(R.id.front_trade);
        TradeBack=(CircleImageView)view.findViewById(R.id.back_trade);

        frontVehical=(CircleImageView)view.findViewById(R.id.frontVehical);
        backVehical=(CircleImageView)view.findViewById(R.id.backVehical);
        rightVehical=(CircleImageView)view.findViewById(R.id.rightVehical);
        leftVehical=(CircleImageView)view.findViewById(R.id.leftVehical);

        userImage=(CircleImageView)view.findViewById(R.id.userImage);

        if(hashMap.get(ConstantValues.LICENSE_FRONT)!=null) {
    licenseImagesList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.LICENSE_FRONT));
    Picasso.with(activity)
            .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.LICENSE_FRONT))
            .placeholder(R.drawable.good_img)
            .error(R.drawable.good_img)
            .into(licenseFront);
}
        if(hashMap.get(ConstantValues.LICENSE_BACK)!=null) {
            licenseImagesList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.LICENSE_BACK));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.LICENSE_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(licenseback);
        }

        if(hashMap.get(ConstantValues.CIRTIFICATE_FRONT)!=null) {
            cirtificateList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.CIRTIFICATE_FRONT));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.CIRTIFICATE_FRONT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(cirtificateFront);
        }
        if(hashMap.get(ConstantValues.CIRTIFICATE_BACK)!=null) {
            cirtificateList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.CIRTIFICATE_BACK));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.CIRTIFICATE_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(cirtificateBack);

        }
        if(hashMap.get(ConstantValues.INSURENCE_FRONT)!=null) {
            insuranceList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.INSURENCE_FRONT));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.INSURENCE_FRONT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(insuranceFront);
        }
        if(hashMap.get(ConstantValues.INSURENCE_BACK)!=null) {
            insuranceList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.INSURENCE_BACK));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.INSURENCE_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(insuranceback);
        }
        if(hashMap.get(ConstantValues.TRADE_FRONT)!=null) {
            tradeInsuranceList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.TRADE_FRONT));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.TRADE_FRONT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(tradeFront);

        }
        if(hashMap.get(ConstantValues.TRADE_BACK)!=null) {
            tradeInsuranceList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.TRADE_BACK));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.TRADE_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(TradeBack);
        }

               if(hashMap.get(ConstantValues.USER_IMAGE)!=null) {
                   User.getInstance().setUserImage(hashMap.get(ConstantValues.USER_IMAGE),activity);

                   Picasso.with(activity)
                           .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.USER_IMAGE))
                           .placeholder(R.drawable.profile_placeholder)
                           .error(R.drawable.profile_placeholder)
                           .into(userImage);
               }

        if(hashMap.get(ConstantValues.VEHICAL_FRONT)!=null) {
            vehicalImagesList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_FRONT));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_FRONT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(frontVehical);

        }
        if(hashMap.get(ConstantValues.VEHICAL_BACK)!=null) {
            vehicalImagesList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_BACK));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(backVehical);

        }
        if(hashMap.get(ConstantValues.VEHICAL_RIGHT)!=null) {
            vehicalImagesList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_RIGHT));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_RIGHT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(rightVehical);

        }
        if(hashMap.get(ConstantValues.VEHICAL_LEFT)!=null) {
            vehicalImagesList.add(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_LEFT));
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_LEFT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(leftVehical);

        }

        view.findViewById(R.id.rl_driving_license).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(licenseImagesList);
            }
        });
        view.findViewById(R.id.rl_registration_cirtificate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(cirtificateList);
            }
        });
        view.findViewById(R.id.rl_insurence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(insuranceList);
            }
        });
        view.findViewById(R.id.rl_trade_insurence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(tradeInsuranceList);
            }
        });
        view.findViewById(R.id.rl_vehicle_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(vehicalImagesList);
            }
        });


        licenseExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+CommonMethods.getInstance().getDatafromTimeStamp(hashMap.get(ConstantValues.LICENSE_EXPIRY)));
        cirtificateExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+CommonMethods.getInstance().getDatafromTimeStamp(hashMap.get(ConstantValues.CIRTIFICATE_EXPIRY)));
        insuranceExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+CommonMethods.getInstance().getDatafromTimeStamp(hashMap.get(ConstantValues.INSURENCE_NUMBER_EXPIRY)));
        tradeInsuranceExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+CommonMethods.getInstance().getDatafromTimeStamp(hashMap.get(ConstantValues.TRADE_INSURENCE_NUMBER_EXPIRY)));


      /*  licenseExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+DateFormat.format("dd/MM/yyyy", Long.parseLong(license_expiry_date)).toString());
        cirtificateExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+DateFormat.format("dd/MM/yyyy", Long.parseLong(certificate_expiry_date)).toString());
        insuranceExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+DateFormat.format("dd/MM/yyyy", Long.parseLong(driver_insurance_expiry_date)).toString());
        tradeInsuranceExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+DateFormat.format("dd/MM/yyyy", Long.parseLong(trade_license_paper_expiry_date)).toString());
*/
    }


    public  void  openImages(ArrayList<String> images,ViewPager viewPager){
        ViewPagerAdapter mPageAdapter;
        List<Fragment> fragments = buildFragments(images);
        if(viewPager.getAdapter()!=null){
            ViewPagerAdapter viewPagerAdapter=(ViewPagerAdapter)viewPager.getAdapter();
            viewPagerAdapter.notifyDataSetChanged();
        }
        if(mActivityResponse!=null){
            mPageAdapter = new ViewPagerAdapter(mActivityResponse,mActivityResponse.getSupportFragmentManager(),fragments, images);
        }else {
            mPageAdapter = new ViewPagerAdapter(activity,activity.getSupportFragmentManager(),fragments, images);
        }
        viewPager.setAdapter(mPageAdapter);
    }





    private List<Fragment> buildFragments(ArrayList<String> images) {
        Activity activity=getActivityRefrence();
        List<android.support.v4.app.Fragment> fragments = new ArrayList<Fragment>();
        if(images!=null) {
            for (int i = 0; i < images.size(); i++) {
                Bundle b = new Bundle();
                b.putInt("position", i);
                b.putString(ConstantValues.USER_IMAGE,images.get(i));
                fragments.add(Fragment.instantiate(activity, DoccumentImagesFragment.class.getName(), b));
            }
        }
        return fragments;
    }


    private void showPopUp(ArrayList<String> licenseImagesList) {
        Activity activity=getActivityRefrence();
        Intent intent=new Intent(activity, ImagesActivity.class);
        intent.putStringArrayListExtra("list",licenseImagesList);
        startActivity(intent);

       /* final Dialog openDialog = new Dialog(activity);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        openDialog.setContentView(R.layout.dialog_imges_view_pager);
        ViewPager viewPager=openDialog.findViewById(R.id.view_pager);
        ImageView imageViewCross=openDialog.findViewById(R.id.crossImage);
        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog.dismiss();
            }
        });
        openImages(licenseImagesList,viewPager);
        openDialog.show();*/

    }

    @Override
    public void closeDialog() {
    }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }
}



 /* map.put(ConstantValues.USER_FIRSTNAME,data.getString("first_name"));
                map.put(ConstantValues.USER_LASTNAME,data.getString("last_name"));
                map.put(ConstantValues.USER_EMAILADDRESS,data.getString("email"));
                map.put(ConstantValues.USER_MOBILENUMBER,data.getString("phone_number"));
                map.put(ConstantValues.COUNTRY_CODE,data.getString("country_code"));
                map.put(ConstantValues.USER_ID,data.getString("driver_id"));
                map.put(ConstantValues.USER_ACCESS_TOKEN,data.getString("access_token"));*/