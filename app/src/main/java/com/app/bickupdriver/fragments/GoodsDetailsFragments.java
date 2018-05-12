package com.app.bickupdriver.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.BookingDetailsAcceptRejectActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.TypesGoods;
import com.app.bickupdriver.interfaces.HandlerGoodsNavigations;
import com.app.bickupdriver.utility.ConstantValues;
import com.xw.repo.BubbleSeekBar;


public class GoodsDetailsFragments extends Fragment implements View.OnClickListener {

    public static String TAG=GoodsDetailsFragments.class.getSimpleName();
    private BookingDetailsAcceptRejectActivity mBookingDetailsAcceptRejectActivity;
    private Activity mActivity;
    private Button btnSaveBooking;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private BookingDetailsAcceptRejectActivity mActivityReference;
    private ImageView imgOneHelper;
    private ImageView imgTwoHelper;
    private ImageView imgTickOneHelper;
    private ImageView imgTickTwoHelper;
    private EditText edtDescription;
    private TextView btnComeNow;
    private TextView btnComeLater;
    private ImageView imgUploadImage;
    private ImageView imghelperCheckBox;
    private ImageView imgTypesGoods;

    public GoodsDetailsFragments() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showPopUp(int choosetraveller) {
        final Dialog openDialog = new Dialog(mActivityReference);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.calender_view_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       /* TextView travellerName = (TextView)openDialog.findViewById(R.id.txt_traveller_name_dialog);

        TextView travellerCost = (TextView)openDialog.findViewById(R.id.txt_traveller_cost);
        ImageView travellerImage = (ImageView)openDialog.findViewById(R.id.img_traveller);*/
        Button btnCancel = (Button)openDialog.findViewById(R.id.btn_cancel);

        Button btnok = (Button)openDialog.findViewById(R.id.btn_ok);


        btnCancel.setTypeface(mTypefaceRegular);
        btnok.setTypeface(mTypefaceRegular);
       /* travellerName.setTypeface(mTypefaceBold);
        travellerCost.setTypeface(mTypefaceRegular);
        btnDisagree.setTypeface(mTypefaceBold);
        btnAgree.setTypeface(mTypefaceBold);*/
        if(choosetraveller==1){

        }
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                showPopUpForTime(0);

            }
        });
        openDialog.show();

    }


    private void showPopUpForTime(int choosetraveller) {
        final Dialog openDialog = new Dialog(mActivityReference);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.time_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final TextView fifteenMinuit = (TextView)openDialog.findViewById(R.id.fifteen_minuit);
        final TextView thirtyMinuit = (TextView)openDialog.findViewById(R.id.thirty_minuit);
        final TextView fourtyMinuit = (TextView)openDialog.findViewById(R.id.fourty_minuit);
        final TextView minuitText = (TextView)openDialog.findViewById(R.id.minuit_txt);
        final TextView hourtext = (TextView)openDialog.findViewById(R.id.hourtext);
        minuitText.setTypeface(mTypefaceRegular);
        fifteenMinuit.setTypeface(mTypefaceRegular);
        thirtyMinuit.setTypeface(mTypefaceRegular);
        fourtyMinuit.setTypeface(mTypefaceRegular);
        setColorWhite(fifteenMinuit);
        setColorWhite(thirtyMinuit);
        setColorWhite(fourtyMinuit);
        final com.xw.repo.BubbleSeekBar seekBar=(com.xw.repo.BubbleSeekBar)openDialog.findViewById(R.id.seekbar);
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                  hourtext.setText(String.valueOf(progress));
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        fifteenMinuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minuitText.setText("15");
                fifteenMinuit.setTextColor(getActivity().getResources().getColor(R.color.grey_text_color));
                thirtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                fourtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                setColorYellow(view);
                setColorWhite(thirtyMinuit);
                setColorWhite(fourtyMinuit);

            }


        });

        thirtyMinuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minuitText.setText("30");

                thirtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.grey_text_color));
                fifteenMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                fourtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                setColorYellow(view);
                setColorWhite(fifteenMinuit);
                setColorWhite(fourtyMinuit);
            }
        });

        fourtyMinuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorYellow(view);
                minuitText.setText("45");
                fourtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.grey_text_color));
                fifteenMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                thirtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                setColorWhite(thirtyMinuit);
                setColorWhite(fifteenMinuit);
            }
        });

        TextView travellerCost = (TextView)openDialog.findViewById(R.id.txt_traveller_cost);
        ImageView travellerImage = (ImageView)openDialog.findViewById(R.id.img_traveller);
        Button btnCancel = (Button)openDialog.findViewById(R.id.btn_cancel);

        Button btnok = (Button)openDialog.findViewById(R.id.btn_ok);
        btnCancel.setTypeface(mTypefaceRegular);
        btnok.setTypeface(mTypefaceRegular);
       /* travellerName.setTypeface(mTypefaceBold);
        travellerCost.setTypeface(mTypefaceRegular);
        btnDisagree.setTypeface(mTypefaceBold);
        btnAgree.setTypeface(mTypefaceBold);*/
        if(choosetraveller==1){

        }
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDialog.dismiss();

            }
        });
        openDialog.show();

    }

    private void setColorYellow(View view) {
        TextView view1=(TextView)view;
        StateListDrawable bgShape = (StateListDrawable)view1.getBackground();
        bgShape.setColorFilter(Color.parseColor("#e6ba13"), PorterDuff.Mode.SRC_ATOP);
    }
    private void setColorWhite(TextView view) {
        StateListDrawable bgShape = (StateListDrawable)view.getBackground().mutate();
        bgShape.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_goods_details_fragments, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {

        mTypefaceRegular= Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_BOLD);
        setTypefaceToviews(view);

        imgOneHelper=(ImageView)view.findViewById(R.id.img_helper_single);
        imgTwoHelper=(ImageView)view.findViewById(R.id.img_double_helper);

        imgTickOneHelper=(ImageView)view.findViewById(R.id.tick_single_helper);
        imgTickTwoHelper=(ImageView)view.findViewById(R.id.tick_double_helper);
        imghelperCheckBox=(ImageView)view.findViewById(R.id.check_box_img);
        imgTypesGoods=(ImageView)view.findViewById(R.id.img_types_goods);
        edtDescription=(EditText)view.findViewById(R.id.edt_description);
        btnComeNow=(TextView)view.findViewById(R.id.btn_come_now);
        btnComeLater=(TextView)view.findViewById(R.id.btn_come_later);
        imgUploadImage=(ImageView)view.findViewById(R.id.img_upload_image);

        imgOneHelper.setOnClickListener(this);
        imgOneHelper.setTag(R.drawable.ac_sing_helper);
        imgTwoHelper.setOnClickListener(this);
        imgTwoHelper.setTag(R.drawable.de_double_helper);
        btnComeNow.setOnClickListener(this);
        btnComeNow.setTag(true);
        btnComeLater.setOnClickListener(this);
        btnComeLater.setTag(false);
        imgTypesGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mActivityReference, TypesGoods.class);
                startActivity(intent);
            }
        });

        imghelperCheckBox.setOnClickListener(this);
        imghelperCheckBox.setTag(false);

        edtDescription.setTypeface(mTypefaceRegular);
        btnComeNow.setTypeface(mTypefaceRegular);
        btnComeLater.setTypeface(mTypefaceRegular);

        btnSaveBooking=(Button)view.findViewById(R.id.btn_save_booking);
        btnSaveBooking.setOnClickListener(this);
        btnSaveBooking.setTypeface(mTypefaceRegular);
    }

    private void setTypefaceToviews(View view) {
        TextView txtHelper=(TextView)view.findViewById(R.id.txt_helper);
        TextView txtChooseHelper=(TextView)view.findViewById(R.id.txt_choose_helper);
        TextView txtNohelperRequired=(TextView)view.findViewById(R.id.txt_no_helper_required);
        TextView txtDescription=(TextView)view.findViewById(R.id.txt_description);
        TextView txtComingTime=(TextView)view.findViewById(R.id.txt_coming_time);
        TextView txtdateTime=(TextView)view.findViewById(R.id.txt_date_time);

        txtHelper.setTypeface(mTypefaceRegular);
        txtChooseHelper.setTypeface(mTypefaceRegular);
        txtComingTime.setTypeface(mTypefaceRegular);
        txtNohelperRequired.setTypeface(mTypefaceRegular);
        txtDescription.setTypeface(mTypefaceRegular);
        txtdateTime.setTypeface(mTypefaceRegular);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference=(BookingDetailsAcceptRejectActivity)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View view) {
        HandlerGoodsNavigations handlerGoodsNavigations=mActivityReference;
        int id=view.getId();
        switch (id){
            case R.id.btn_save_booking:
                handlerGoodsNavigations.callBookingDetailsFragment();
                break;
            case R.id.btn_come_now:
                btnComeNow.setBackground(mActivityReference.getResources().getDrawable(R.drawable.sm_btn));
                btnComeLater.setBackgroundColor(mActivityReference.getResources().getColor(R.color.white));
                btnComeNow.setTextColor(mActivityReference.getResources().getColor(R.color.white));
                btnComeLater.setTextColor(mActivityReference.getResources().getColor(R.color.grey_text_color));
                btnComeNow.setTag(true);
                btnComeLater.setTag(false);
                break;
            case R.id.btn_come_later:
                btnComeNow.setBackgroundColor(mActivityReference.getResources().getColor(R.color.white));
                btnComeLater.setBackground(mActivityReference.getResources().getDrawable(R.drawable.sm_btn));
                btnComeNow.setTextColor(mActivityReference.getResources().getColor(R.color.grey_text_color));
                btnComeLater.setTextColor(mActivityReference.getResources().getColor(R.color.white));
                btnComeNow.setTag(false);
                btnComeLater.setTag(true);
                showPopUp(0);
                break;
            case R.id.img_helper_single:
                imgOneHelper.setImageResource(R.drawable.ac_sing_helper);
                imgTwoHelper.setImageResource(R.drawable.de_double_helper);
                imgTickTwoHelper.setVisibility(View.GONE);
                imgTickOneHelper.setVisibility(View.VISIBLE);
                imgOneHelper.setTag(true);
                imgTwoHelper.setTag(false);
                break;
            case R.id.img_double_helper:
                imgOneHelper.setImageResource(R.drawable.de_sing_helper);
                imgTwoHelper.setImageResource(R.drawable.ac_double_helper);
                imgTickTwoHelper.setVisibility(View.VISIBLE);
                imgTickOneHelper.setVisibility(View.GONE);
                imgOneHelper.setTag(false);
                imgTwoHelper.setTag(true);
                break;
            case R.id.check_box_img:
                boolean ischecked= (boolean) imghelperCheckBox.getTag();
                if(!ischecked){
                    imghelperCheckBox.setImageResource(R.drawable.ac_checkbox);
                    imghelperCheckBox.setTag(true);
                }else{
                    imghelperCheckBox.setImageResource(R.drawable.de_checkbox);
                    imghelperCheckBox.setTag(false);
                }
                break;

        }
    }


}
