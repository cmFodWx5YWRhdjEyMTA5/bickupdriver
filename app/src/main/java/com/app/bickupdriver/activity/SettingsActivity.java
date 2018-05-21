package com.app.bickupdriver.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.SharedPrefManager;
import com.app.bickupdriver.utility.Utils;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;
    private RelativeLayout layoutEditProfile;
    private RelativeLayout layoutChangeLanguage;
    private String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_setting);
        initializeViews();
    }

    private void initializeViews() {
        TextView txtHeader = (TextView) findViewById(R.id.txt_activty_header);
        txtHeader.setText(getResources().getString(R.string.txt_setting));

        findViewById(R.id.btn_logout).setOnClickListener(this);
        imgBack = (ImageView) findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);


        layoutEditProfile = findViewById(R.id.layout_edit_profile);
        layoutEditProfile.setOnClickListener(this);

        layoutChangeLanguage = findViewById(R.id.layout_change_language);
        layoutChangeLanguage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backImage_header:
                finish();
                break;
            case R.id.btn_logout:
                CommonMethods.getInstance().clearSharePreferences(this, ConstantValues.USER_PREFERENCES);
                Intent setting = new Intent(this, LoginActivity.class);
                startActivity(setting);
                finishAffinity();
                break;

            case R.id.layout_edit_profile:
                Intent s = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(s);
                break;


            case R.id.layout_change_language:
                languageListDialog();
                break;
        }
    }

    private void languageListDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_language_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RadioGroup rgSelectLanguage = dialog.findViewById(R.id.rg_select_language);
        rgSelectLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rb_language_english) {
                    selectedLanguage = ConstantValues.LANGUAGE_ENGLISH;
                } else if (checkedId == R.id.rb_language_arabic) {
                    selectedLanguage = ConstantValues.LANGUAGE_ARABIC;
                }
            }
        });

        Button btnOk = dialog.findViewById(R.id.btn_ok_change_language);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(selectedLanguage)) {
                    Utils.showToast(getString(R.string.text_please_select_a_language), SettingsActivity.this);
                } else {
                    dialog.dismiss();
                    displayConfirmationDialog(selectedLanguage);
                    //changeLocale(selectedLanguage);
                }
            }
        });

        Button btnCancel = dialog.findViewById(R.id.btn_cancel_select_language);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void displayConfirmationDialog(final String selectedLanguage) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_confirmation_change_language);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvTitle = dialog.findViewById(R.id.tv_alert_title);
        TextView tvMessage = dialog.findViewById(R.id.tv_alert_message);

        tvTitle.setText(getString(R.string.text_alert));
        tvMessage.setText(getString(R.string.text_change_language_message) + " " + selectedLanguage);

        Button btnOk = dialog.findViewById(R.id.btn_ok_change_language);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_change_language);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (selectedLanguage.equals(ConstantValues.LANGUAGE_ENGLISH)) {
                    setLocale(ConstantValues.LANGUAGE_ENGLISH_CODE);
                    recreate();
                } else if (selectedLanguage.equals(ConstantValues.LANGUAGE_ARABIC)) {
                    Utils.printLogs("SETTINGS", "Arabic Language --- : ");
                    setLocale(ConstantValues.LANGUAGE_ARABIC_CODE);
                    recreate();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;


        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.saveStringData(SharedPrefManager.APP_LANGUAGE, languageCode);

        // Restart the Application
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        //getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        /**
         * Save Selected Language to Shared Preferences
         */
    }
}
