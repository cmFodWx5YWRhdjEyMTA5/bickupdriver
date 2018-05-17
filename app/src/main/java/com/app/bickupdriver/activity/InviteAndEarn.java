package com.app.bickupdriver.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.bickupdriver.R;

public class InviteAndEarn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_invite_and_earn);
    }
}
