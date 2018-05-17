package com.app.bickupdriver.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.app.bickupdriver.R;
import com.app.bickupdriver.utility.ConstantValues;

import java.util.Calendar;

public class RevenueActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ImageView imageView;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
         hidebottom();
        TextView tv_header = (TextView) findViewById(R.id.txt_activty_header);
        tv_header.setText(getResources().getString(R.string.txt_revenue));
        calendar=Calendar.getInstance();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mTypefaceRegular= Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_BOLD);
        tv_header.setTypeface(mTypefaceRegular);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        imageView=(ImageView) findViewById(R.id.img_tick_toolbar);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.calander_bg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance( RevenueActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(RevenueActivity.this.getFragmentManager(), "datePicker");
            }
        });
        ImageView imageView2=(ImageView) findViewById(R.id.backImage_header);
        imageView2.setVisibility(View.VISIBLE);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }
    private void hidebottom() {
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_revenue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        Activity activity;
        private Typeface mTypefaceRegular;
        private Typeface mTypefaceBold;
        int sectionNumber;

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            if(getArguments()!=null){
                sectionNumber=getArguments().getInt(ARG_SECTION_NUMBER);
            }

            View rootView = inflater.inflate(R.layout.fragment_revenue, container, false);
            mTypefaceRegular= Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
            mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);

            TextView txt1=(TextView) rootView.findViewById(R.id.txt1);
            TextView txt2=(TextView) rootView.findViewById(R.id.txt2);
            TextView txt3=(TextView) rootView.findViewById(R.id.txt3);
            TextView txt4=(TextView) rootView.findViewById(R.id.txt4);

            Button btn=(Button)rootView.findViewById(R.id.btn_change);
            btn.setTypeface(mTypefaceRegular);
            if(sectionNumber==3){
                btn.setVisibility(View.GONE);
            }


            TextView txt9=(TextView) rootView.findViewById(R.id.txt_date);
            TextView txt10=(TextView) rootView.findViewById(R.id.txt_transection);
            TextView txt11=(TextView) rootView.findViewById(R.id.txt_total);
            TextView txt12=(TextView) rootView.findViewById(R.id.txt_total_label);

            txt9.setTypeface(mTypefaceBold);
            txt10.setTypeface(mTypefaceBold);
            txt11.setTypeface(mTypefaceBold);
            txt12.setTypeface(mTypefaceRegular);


            txt1.setTypeface(mTypefaceRegular);
            txt2.setTypeface(mTypefaceRegular);
            txt3.setTypeface(mTypefaceRegular);
            txt4.setTypeface(mTypefaceRegular);

            TextView txt5=(TextView) rootView.findViewById(R.id.txt_payment_type);
            TextView txt6=(TextView) rootView.findViewById(R.id.txt_card_payment_type);
            TextView txt7=(TextView) rootView.findViewById(R.id.txt_cash_payment_type);
            TextView txt8=(TextView) rootView.findViewById(R.id.txt_payment_type);

            txt5.setTypeface(mTypefaceRegular);
            txt6.setTypeface(mTypefaceRegular);
            txt7.setTypeface(mTypefaceRegular);
            txt8.setTypeface(mTypefaceRegular);

            TextView a=(TextView) rootView.findViewById(R.id.a);
            TextView b=(TextView) rootView.findViewById(R.id.b);
            TextView c=(TextView) rootView.findViewById(R.id.c);
            TextView d=(TextView) rootView.findViewById(R.id.d);

            a.setTypeface(mTypefaceRegular);
            b.setTypeface(mTypefaceRegular);
            c.setTypeface(mTypefaceRegular);
            d.setTypeface(mTypefaceRegular);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            this.activity=activity;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.txt_all);
                case 1:
                    return getResources().getString(R.string.txt_cash);
                case 2:
                    return getResources().getString(R.string.txt_card);
            }
            return null;
        }
    }
}
