package com.app.bickupdriver.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.fragments.ChangeProfileFragment;
import com.app.bickupdriver.fragments.InsurenceFragment;
import com.app.bickupdriver.fragments.Licensefragment;
import com.app.bickupdriver.fragments.RegistrationCirtificateFragment;
import com.app.bickupdriver.fragments.TradeLicenseFragment;
import com.app.bickupdriver.fragments.VehicalImagesFragment;
import com.app.bickupdriver.fragments.ViewProfileDriverFragment;
import com.app.bickupdriver.interfaces.DoccumentInterface;
import com.app.bickupdriver.interfaces.UpdateUser;


public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, DoccumentInterface {


    public static TextView tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initializeViews();
        ImageView imageView = (ImageView) findViewById(R.id.img_tick_toolbar);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(this);
        callFragment(new ViewProfileDriverFragment(),
                ViewProfileDriverFragment.class.getSimpleName());
    }

    private void initializeViews() {
        tv_header = (TextView) findViewById(R.id.txt_activty_header);
        ImageView backImage = (ImageView) findViewById(R.id.backImage_header);
        backImage.setVisibility(View.VISIBLE);
        backImage.setOnClickListener(this);
        tv_header.setText(getResources().getString(R.string.txt_profile));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backImage_header:
                callBackPressed();
                break;
            case R.id.img_tick_toolbar:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.profile_container);
                if (fragment instanceof ChangeProfileFragment) {
                    ChangeProfileFragment changeProfileFragment = (ChangeProfileFragment) fragment;
                    UpdateUser updateUser = changeProfileFragment;
                    updateUser.updateUser();
                }
                if (fragment instanceof ViewProfileDriverFragment) {
                    finish();
                }

                break;
        }
    }

    private void callFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().add(R.id.profile_container, fragment).addToBackStack(tag).commit();


    }

    @Override
    public void onBackPressed() {
        callBackPressed();
    }

    private void callBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.profile_container);
            if (currentFragment instanceof VehicalImagesFragment) {
                getSupportFragmentManager().popBackStackImmediate();
                tv_header.setText(getResources().getString(R.string.txt_profile));
            }
            if (currentFragment instanceof Licensefragment) {
                getSupportFragmentManager().popBackStackImmediate();
                tv_header.setText(getResources().getString(R.string.txt_driving_license));
            }
            if (currentFragment instanceof RegistrationCirtificateFragment) {
                getSupportFragmentManager().popBackStackImmediate();
                tv_header.setText(getResources().getString(R.string.txt_registration_cirtificate));
            }
            if (currentFragment instanceof InsurenceFragment) {
                getSupportFragmentManager().popBackStackImmediate();
                tv_header.setText(getResources().getString(R.string.txt_insurence_paper));
            }
            if (currentFragment instanceof TradeLicenseFragment) {
                getSupportFragmentManager().popBackStackImmediate();
                tv_header.setText(getResources().getString(R.string.txt_trade_license));
            }
            if (currentFragment instanceof VehicalImagesFragment) {
                tv_header.setText(getResources().getString(R.string.txt_vehical_image));

            }
            if (currentFragment instanceof ChangeProfileFragment) {
                tv_header.setText(getResources().getString(R.string.txt_change_profile));
                ImageView imageView2 = (ImageView) findViewById(R.id.img_tick_toolbar);
                imageView2.setVisibility(View.VISIBLE);
                imageView2.setOnClickListener(this);
            }
            if (currentFragment instanceof ViewProfileDriverFragment) {
                tv_header.setText(getResources().getString(R.string.txt_profile));
                ImageView imageView2 = (ImageView) findViewById(R.id.img_tick_toolbar);
                imageView2.setVisibility(View.VISIBLE);
                imageView2.setOnClickListener(this);
            }
        } else {
            finish();
        }
    }

    @Override
    public void callFragment(Fragment fragment) {
        if (fragment instanceof ChangeProfileFragment || fragment instanceof ViewProfileDriverFragment) {
            ImageView imageView = (ImageView) findViewById(R.id.img_tick_toolbar);
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(this);
            if (fragment instanceof ChangeProfileFragment) {
                tv_header.setText(getResources().getString(R.string.txt_change_profile));
            } else {
                tv_header.setText(getResources().getString(R.string.txt_profile));
            }
        } else {
            ImageView imageView = (ImageView) findViewById(R.id.img_tick_toolbar);
            imageView.setVisibility(View.GONE);

        }
        getSupportFragmentManager().beginTransaction().add(R.id.profile_container, fragment).addToBackStack("").commit();

    }


}
