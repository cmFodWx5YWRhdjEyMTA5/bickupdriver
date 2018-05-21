package com.app.bickupdriver.utility;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.fragments.ChangePasswordFragment;
import com.app.bickupdriver.fragments.ContactUsFragment;
import com.app.bickupdriver.fragments.ContentFragment;
import com.app.bickupdriver.fragments.FaqFragment;
import com.app.bickupdriver.fragments.ForgotPassword;
import com.app.bickupdriver.fragments.HelpFragment;
import com.app.bickupdriver.fragments.InviteAndEarnFragment;
import com.app.bickupdriver.interfaces.CallTitle;

public class CMSActivity extends AppCompatActivity implements CallTitle{

    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private TextView tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cms);
         tv_header = (TextView) findViewById(R.id.txt_activty_header);

        mTypefaceRegular= Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_BOLD);
        tv_header.setTypeface(mTypefaceRegular);
        ImageView imageView2=(ImageView) findViewById(R.id.backImage_header);
        imageView2.setVisibility(View.VISIBLE);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getData();
    }

    private void getData() {
       int data= getIntent().getIntExtra(ConstantValues.CHOOSE_PAGE,1);
        switch (data) {
            case 1:
                tv_header.setText(getResources().getString(R.string.txt_contactus));
                ContactUsFragment forgotPassword = new ContactUsFragment();
                callfragment(forgotPassword, ContactUsFragment.TAG);
                break;
            case 2:
                tv_header.setText(getResources().getString(R.string.txt_privacy_policy));
                ContentFragment forgotPassword1 = new ContentFragment();
                callfragment(forgotPassword1, ContentFragment.TAG);
                break;
            case 3:
                tv_header.setText(getResources().getString(R.string.txt_about_us));
                ContentFragment forgotPassword2 = new ContentFragment();
                callfragment(forgotPassword2, ContentFragment.TAG);
                break;
            case 4:
                tv_header.setText(getResources().getString(R.string.txt_faq));
                FaqFragment forgotPassword4 = new FaqFragment();
                callfragment(forgotPassword4, FaqFragment.TAG);
                break;
            case 5:
                tv_header.setText(getResources().getString(R.string.txt_invite_earn));
                Fragment inviteAndEarn = new InviteAndEarnFragment();
                callfragment(inviteAndEarn, InviteAndEarnFragment.TAG);
                break;
            case 6:
                tv_header.setText(getResources().getString(R.string.txt_change_password));
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                callfragment(changePasswordFragment, ChangePasswordFragment.TAG);
                break;
            case 7:
                tv_header.setText(getResources().getString(R.string.txt_help));
                HelpFragment helpFragment = new HelpFragment();
                callfragment(helpFragment, ChangePasswordFragment.TAG);
                break;

        }

    }

    private void callfragment(Fragment fragment,String tag) {
        getSupportFragmentManager().beginTransaction().add(R.id.content_container,fragment,tag).addToBackStack(null).commit();
    }

    @Override
    public void calltitlefragment() {
        tv_header.setText(getResources().getString(R.string.txt_title));
        ContentFragment forgotPassword = new ContentFragment();
        callfragment(forgotPassword, ForgotPassword.TAG);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>1){
            getSupportFragmentManager().popBackStackImmediate();
            Fragment fragment=getCurrentFragment();

            if(fragment instanceof ContactUsFragment){
                tv_header.setText(getResources().getString(R.string.txt_contactus));
            }
            if(fragment instanceof HelpFragment){
                tv_header.setText(getResources().getString(R.string.txt_help));
            }


        }else {
            finish();
        }
    }
    private Fragment getCurrentFragment(){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.password_activity_container);
        return f;
    }
}
