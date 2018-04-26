package com.app.bickupdriver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickupdriver.controller.AppController;
import com.app.bickupdriver.fragments.ChangeProfileFragment;
import com.app.bickupdriver.fragments.ForgotPassword;
import com.app.bickupdriver.fragments.InsurenceFragment;
import com.app.bickupdriver.fragments.Licensefragment;
import com.app.bickupdriver.fragments.Otp;
import com.app.bickupdriver.fragments.PersonalDetailsFragment;
import com.app.bickupdriver.fragments.RegistrationCirtificateFragment;
import com.app.bickupdriver.fragments.ResetPassword;
import com.app.bickupdriver.fragments.TradeLicenseFragment;
import com.app.bickupdriver.fragments.VehicalImagesFragment;
import com.app.bickupdriver.fragments.ViewProfileDriverFragment;
import com.app.bickupdriver.interfaces.DoccumentInterface;
import com.app.bickupdriver.interfaces.HandleForgotResetNavigations;
import com.app.bickupdriver.interfaces.UpdateUser;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;


public class ResetAndForgetPasswordActivity extends AppCompatActivity implements HandleForgotResetNavigations, InternetConnectionBroadcast.ConnectivityRecieverListener,View.OnClickListener,DoccumentInterface {


    private TextView tv_header;
    private boolean mIsConnected;
    private Context mActivityreference;
    private Snackbar snackbar;
    private CoordinatorLayout mCoordinatorLayout;
    public  static  boolean isForgotpassword=false;
    public  static  boolean isChangeNumber=false;
    public static ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_reset_and_forget_password);
        initViews();
        getdata();
    }

    private void getdata() {
        int pageToOpen=getIntent().getIntExtra(ConstantValues.CHOOSE_PAGE,0);
        int changeNumber=getIntent().getIntExtra(ConstantValues.CHANGE_NUMBER,0);
        openfragment(pageToOpen,changeNumber);
    }

    private void openfragment(int pageToOpen,int changeNumber) {
        ImageView imageView=(ImageView)findViewById(R.id.img_tick_toolbar);
        imageView.setOnClickListener(this);
        imageView.setVisibility(View.GONE);
        switch (pageToOpen) {
            case ConstantValues.FORGOT_PASSWORD:
                imgBack.setVisibility(View.VISIBLE);
                if(changeNumber==1) {
                    tv_header.setText(getString(R.string.txt_change_number));
                }else {
                    tv_header.setText(getString(R.string.txt_forgot_pasword));
                }
                Fragment forgotPassword = new ForgotPassword();
                Bundle bundle1 = new Bundle();
                bundle1.putInt(ConstantValues.CHANGE_NUMBER, changeNumber);
                forgotPassword.setArguments(bundle1);
                callfragment(forgotPassword, ForgotPassword.TAG);
                break;
            case ConstantValues.RESET_PASSWORD:
                tv_header.setText(getString(R.string.txt_reset_pasword));
                imgBack.setVisibility(View.GONE);
                ResetPassword resetPassword = new ResetPassword();
                callfragment(resetPassword, ResetPassword.TAG);
                // tv_header.setText(getString(R.string.enter_otp));
                break;
            case ConstantValues.OTP:
                tv_header.setText(getString(R.string.otp));
                imgBack.setVisibility(View.GONE);
                Fragment otp = new Otp();
                Bundle bundle = new Bundle();
                bundle.putInt(ConstantValues.CHANGE_NUMBER, changeNumber);
                otp.setArguments(bundle);
                callfragment(otp, Otp.TAG);
                tv_header.setText(getString(R.string.enter_otp));
                break;
            case ConstantValues.LICENSE_NUMBER:
                imgBack.setVisibility(View.GONE);
                tv_header.setText(getResources().getString(R.string.txt_driving_license));
                Licensefragment licensefragment=new Licensefragment();
                getSupportFragmentManager().popBackStackImmediate();
                callfragment(licensefragment,Licensefragment.TAG);
                break;
            case ConstantValues.INSURENCE:
                tv_header.setText(getResources().getString(R.string.txt_insurence_paper));
                InsurenceFragment insurenceFragment=new InsurenceFragment();
                getSupportFragmentManager().popBackStackImmediate();
                callfragment(insurenceFragment,InsurenceFragment.TAG);
                break;
            case ConstantValues.CIRTIFICATE:
                tv_header.setText(getResources().getString(R.string.txt_registration_cirtificate));
                RegistrationCirtificateFragment registrationCirtificateFragment=new RegistrationCirtificateFragment();
                getSupportFragmentManager().popBackStackImmediate();
                callfragment(registrationCirtificateFragment,RegistrationCirtificateFragment.TAG);
                break;
            case ConstantValues.TRADE_LICENSE:
                tv_header.setText(getResources().getString(R.string.txt_trade_license));
                TradeLicenseFragment tradeLicenseFragment=new TradeLicenseFragment();
                getSupportFragmentManager().popBackStackImmediate();
                callfragment(tradeLicenseFragment,TradeLicenseFragment.TAG);
                break;
            case ConstantValues.VEHICAL_IMAGES:
                tv_header.setText(getResources().getString(R.string.txt_vehical_image));
                VehicalImagesFragment vehicalImagesFragment=new VehicalImagesFragment();
                getSupportFragmentManager().popBackStackImmediate();
                callfragment(vehicalImagesFragment,VehicalImagesFragment.TAG);
                break;
            case ConstantValues.PERSONAL_DETAILS:
                tv_header.setText(getResources().getString(R.string.txt_personal_details));
                PersonalDetailsFragment personalDetailsFragment=new PersonalDetailsFragment();
                getSupportFragmentManager().popBackStackImmediate();
                callfragment(personalDetailsFragment,PersonalDetailsFragment.TAG);
                break;
            case ConstantValues.PROFILE_DETAILS:
                imageView.setVisibility(View.VISIBLE);
                tv_header.setText(getResources().getString(R.string.txt_profile));
                ViewProfileDriverFragment viewProfileDriverFragment=new ViewProfileDriverFragment();
                callfragment(viewProfileDriverFragment,ViewProfileDriverFragment.TAG);
                break;
            case ConstantValues.CHANGE_PROFILE:
                imageView.setVisibility(View.VISIBLE);
                tv_header.setText(getResources().getString(R.string.txt_change_profile));
                ChangeProfileFragment changeProfileFragment=new ChangeProfileFragment();
                callfragment(changeProfileFragment,ChangeProfileFragment.TAG);
                break;
        }
    }

    private void callfragment(Fragment fragment,String tag) {
        getSupportFragmentManager().beginTransaction().add(R.id.password_activity_container,fragment,tag).addToBackStack(tag).commit();
    }

    private void initViews() {
        mActivityreference=this;
        mCoordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator_reset_and_forget);

        tv_header=(TextView)findViewById(R.id.txt_activty_header);
        imgBack=(ImageView)findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.GONE);
        imgBack.setOnClickListener(this);

    }

    @Override
    public void callOTPFragment(int changeNumber) {
        tv_header.setText(getString(R.string.otp));
        openfragment(ConstantValues.OTP,changeNumber);
    }

    @Override
    public void callResetFragment() {
        tv_header.setText(getString(R.string.txt_reset_pasword));
        ResetPassword resetPassword=new ResetPassword();
        getSupportFragmentManager().beginTransaction().replace(R.id.password_activity_container,resetPassword).addToBackStack(ResetPassword.TAG).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isChangeNumber=false;
        isForgotpassword=false;
    }

    @Override
    public void callForgotFragment(int changenumber) {
        if(changenumber==1){
            tv_header.setText(getString(R.string.txt_change_number));
        }else{
            tv_header.setText(getString(R.string.txt_forgot_password));
        }
        openfragment(ConstantValues.FORGOT_PASSWORD,changenumber);
    }

    @Override
    public void callFragment(int callFragment) {
        openfragment(callFragment,0);
    }

    @Override
    public void handleResetpasswordResult(Bundle data) {
        this.finish();
    }



    @Override
    protected void onResume() {
        super.onResume();
        checkInternetconnection();
        if (AppController.getInstance() != null) {
            AppController.getInstance().setConnectivityListener(this);
        }
    }



    private void checkInternetconnection() {
        mIsConnected = CommonMethods.getInstance().checkInterNetConnection(mActivityreference);
        if (mIsConnected) {
            if (snackbar != null) {
                snackbar.dismiss();

            }
        } else {
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

    public void showSnackBar(String mString) {
       /* snackbar = Snackbar
                .make(mCoordinatorLayout, mString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setText(mString);
        snackbar.show();*/
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else {
            showSnackBar(getResources().getString(R.string.iconnection_availability));
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>1){
            getSupportFragmentManager().popBackStackImmediate();
            Fragment fragment=getCurrentFragment();
            closeTickImage(View.GONE);

           if(fragment instanceof ForgotPassword){
               tv_header.setText(getResources().getString(R.string.txt_forgot_pasword));
           }
            if(fragment instanceof ResetPassword){
                tv_header.setText(getResources().getString(R.string.txt_reset_pasword));
            }
            if(fragment instanceof Otp){
                tv_header.setText(getResources().getString(R.string.otp));
            }
            if(fragment instanceof PersonalDetailsFragment){
                tv_header.setText(getResources().getString(R.string.txt_personal_details));
            }
            if(fragment instanceof Licensefragment){
                tv_header.setText(getResources().getString(R.string.txt_driving_license));
            }
            if(fragment instanceof RegistrationCirtificateFragment){
                tv_header.setText(getResources().getString(R.string.txt_registration_cirtificate));
            }
            if(fragment instanceof InsurenceFragment){
                tv_header.setText(getResources().getString(R.string.txt_insurence_paper));
            }
            if(fragment instanceof TradeLicenseFragment){
                tv_header.setText(getResources().getString(R.string.txt_trade_license));
            }
            if(fragment instanceof VehicalImagesFragment){
                tv_header.setText(getResources().getString(R.string.txt_vehical_image));
            }
            if(fragment instanceof ChangeProfileFragment){
                closeTickImage(View.VISIBLE);
                tv_header.setText(getResources().getString(R.string.txt_change_profile));
            }
            if(fragment instanceof ViewProfileDriverFragment){
                closeTickImage(View.VISIBLE);
                tv_header.setText(getResources().getString(R.string.txt_profile));
            }
        }else {
            Fragment fragment=getCurrentFragment();
            if(fragment instanceof ForgotPassword){
                finish();
            }else {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("")
                        .setMessage(getResources().getString(R.string.txt_close_app))
                        .setPositiveButton(getResources().getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }

                        })
                        .setNegativeButton(getResources().getString(R.string.txt_No), null)
                        .show();
            }
        }
    }

    private void closeTickImage(int visible) {
        ImageView imageView=(ImageView)findViewById(R.id.img_tick_toolbar);
        imageView.setVisibility(visible);
    }

    private Fragment getCurrentFragment(){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.password_activity_container);
        return f;
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.backImage_header:
                onBackPressed();
                break;
            case R.id.img_tick_toolbar:
                Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.password_activity_container);
                if(fragment instanceof ChangeProfileFragment){
                    ChangeProfileFragment changeProfileFragment=(ChangeProfileFragment) fragment;
                    UpdateUser updateUser=changeProfileFragment;
                    updateUser.updateUser();
                }

                if(fragment instanceof ViewProfileDriverFragment){
                    Intent intent=new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
                break;
        }
    }

    @Override
    public void callFragment(Fragment fragment) {
        if(fragment instanceof ChangeProfileFragment||fragment instanceof ViewProfileDriverFragment){
            ImageView imageView=(ImageView)findViewById(R.id.img_tick_toolbar);
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(this);
        }else {
            ImageView imageView=(ImageView)findViewById(R.id.img_tick_toolbar);
            imageView.setVisibility(View.GONE);

        }

        if(fragment instanceof Licensefragment){
            getSupportFragmentManager().popBackStackImmediate();
            imgBack.setVisibility(View.GONE);
            tv_header.setText(getResources().getString(R.string.txt_driving_license));
        }
        if(fragment instanceof RegistrationCirtificateFragment){
            getSupportFragmentManager().popBackStackImmediate();
            imgBack.setVisibility(View.GONE);
            tv_header.setText(getResources().getString(R.string.txt_registration_cirtificate));
        }
        if(fragment instanceof InsurenceFragment){
            getSupportFragmentManager().popBackStackImmediate();
            imgBack.setVisibility(View.GONE);
            tv_header.setText(getResources().getString(R.string.txt_insurence_paper));
        }
        if(fragment instanceof TradeLicenseFragment){
            getSupportFragmentManager().popBackStackImmediate();
            imgBack.setVisibility(View.GONE);
            tv_header.setText(getResources().getString(R.string.txt_trade_license));
        }
        if(fragment instanceof VehicalImagesFragment){
            getSupportFragmentManager().popBackStackImmediate();
            imgBack.setVisibility(View.GONE);
            tv_header.setText(getResources().getString(R.string.txt_vehical_image));
        }
        if(fragment instanceof ChangeProfileFragment){
            closeTickImage(View.VISIBLE);
            tv_header.setText(getResources().getString(R.string.txt_change_profile));
        }
        if(fragment instanceof ViewProfileDriverFragment){
            getSupportFragmentManager().popBackStackImmediate();
            closeTickImage(View.VISIBLE);
            tv_header.setText(getResources().getString(R.string.txt_profile));
        }
        getSupportFragmentManager().beginTransaction().add(R.id.password_activity_container,fragment).addToBackStack("").commit();
    }
}
