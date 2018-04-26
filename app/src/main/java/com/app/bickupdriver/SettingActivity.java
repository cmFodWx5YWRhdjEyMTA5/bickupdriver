package com.app.bickupdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBack;
    private RelativeLayout edit_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_setting);
        initializeViews();
    }
    private void initializeViews() {
        TextView txtHeader=(TextView)findViewById(R.id.txt_activty_header);
        txtHeader.setText(getResources().getString(R.string.txt_setting));
        findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s=new Intent(SettingActivity.this,EditProfileActivity.class);
                startActivity(s);
            }
        });




        findViewById(R.id.btn_logout).setOnClickListener(this);
        imgBack=(ImageView)findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);





    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.backImage_header:
                finish();
                break;
            case R.id.btn_logout:
                CommonMethods.getInstance().clearSharePreferences(this, ConstantValues.USER_PREFERENCES);
                Intent setting=new Intent(this,LoginActivity.class);
                startActivity(setting);
                finishAffinity();
                break;

        }
    }
}
