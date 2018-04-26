package com.app.bickupdriver;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.app.bickupdriver.broadcastreciever.InternetConnectionBroadcast;
import com.app.bickupdriver.controller.AppController;
import com.app.bickupdriver.fragments.LoginFragment;
import com.app.bickupdriver.fragments.Signupfragment;
import com.app.bickupdriver.interfaces.HandleLoginSignUpNavigation;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements InternetConnectionBroadcast.ConnectivityRecieverListener, HandleLoginSignUpNavigation {


    private CoordinatorLayout mCoordinatorLayout;
    private boolean mIsConnected;
    public static String TAG = LoginActivity.class.getSimpleName();
    private Activity mActivityreference;
    private Snackbar snackbar;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_login);
        mActivityreference = this;
        //googleSignIn();
        initializeViews();
        callLoginfragment();

    }

    private void callLoginfragment() {
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_activity_container, loginFragment).commit();
    }

    private void googleSignIn() {
        GoogleSignInOptions mGoogleSignInOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOption)
                .build();
    }


    private void signInWithGoogle() {
        Intent mIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(mIntent, ConstantValues.GOOGLE_SIGN_IN);
    }


    private void initializeViews() {
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatorlayout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValues.GOOGLE_SIGN_IN) {
            GoogleSignInResult mGoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(mGoogleSignInResult);
        }
    }

    private void handleSignInResult(GoogleSignInResult mGoogleSignInResult) {
        if (mGoogleSignInResult != null) {
            GoogleSignInAccount mGoogleSignInAccount = mGoogleSignInResult.getSignInAccount();

        }
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


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        mIsConnected = isConnected;
        if (isConnected) {
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
    public void performSignIn() {
        callmainActivity();
    }

    private void callmainActivity() {
        Intent intent = new Intent(mActivityreference, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void performSignUp() {
        callmainActivity();
    }

    @Override
    public void callSignupFragment() {
        Signupfragment mSignupFargment = new Signupfragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_activity_container, mSignupFargment).commit();
    }

    @Override
    public void callSigninFragment() {
        callLoginfragment();
    }

    @Override
    public void callForgotAndReset(int checkForgotReset, int changeNumber) {
        Intent intent = new Intent(this, ResetAndForgetPasswordActivity.class);
        intent.putExtra(ConstantValues.CHOOSE_PAGE, checkForgotReset);
        intent.putExtra(ConstantValues.CHANGE_NUMBER, changeNumber);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void callMainActivity() {
        callmainActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("")
                .setMessage(getResources().getString(R.string.txt_close_app))
                .setPositiveButton(getResources().getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.txt_No), null)
                .show();
    }
}

