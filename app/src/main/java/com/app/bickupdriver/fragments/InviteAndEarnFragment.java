package com.app.bickupdriver.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.SharedPrefManager;
import com.app.bickupdriver.utility.Utils;


public class InviteAndEarnFragment extends Fragment implements View.OnClickListener {


    public static String TAG = InviteAndEarnFragment.class.getSimpleName();
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Context mActivityReference;
    private ImageView ivShare;
    private ImageView ivCopyReferralCode;
    private TextView txtEarn;
    private TextView txtYourBank;
    private TextView txtRefferel;
    private TextView txtCode;
    private String SHARED_TEXT;


    public InviteAndEarnFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTypefaceRegular = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_BOLD);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite_and_earn_screen, container, false);
        initializeViews(view);


        SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
        String referralCode = sharedPrefManager.getStringData(SharedPrefManager.REFERRAL_CODE);
        Utils.printLogs(TAG, "Referral Code Driver invite and earn : " + referralCode);

        txtCode.setText("" + referralCode);
        SHARED_TEXT = "Download this app at: https://play.google.com/store/apps/details?id=com.app.bickup_user and earn by using this code " + referralCode;
        return view;
    }


    private void initializeViews(View view) {
        txtEarn = (TextView) view.findViewById(R.id.earn_txt_1);
        txtYourBank = (TextView) view.findViewById(R.id.txt_your_bank);
        txtRefferel = (TextView) view.findViewById(R.id.txt_reffral_code);
        txtCode = (TextView) view.findViewById(R.id.txt_code);
        ivCopyReferralCode = view.findViewById(R.id.iv_copy_referral_code);
        ivShare = view.findViewById(R.id.img_share);
        ivShare.setOnClickListener(this);

        ivCopyReferralCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ivCopyReferralCode.setSelected(true);
                copyReferralCodeToClipboard();
                return false;
            }
        });
    }


    private void copyReferralCodeToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", SHARED_TEXT);
        clipboard.setPrimaryClip(clipData);
        Utils.showToast("Copied", getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ivCopyReferralCode.setBackgroundColor(getContext().getColor(R.color.greyColor));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void openShareDialog() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, SHARED_TEXT);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_share:
                openShareDialog();
                break;
        }
    }
}
