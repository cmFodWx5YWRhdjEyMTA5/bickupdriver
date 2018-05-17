package com.app.bickupdriver.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.bickupdriver.R;

public class PickupLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBack;
    private EditText edtPickupLocation;
    private EditText edtFloorNumber;
    private EditText edtUnitNumber;
    private EditText edtContactPersonname;
    private EditText edtContactPersonNumber;
    private EditText edtComments;
    private LinearLayout liBuilding;
    private LinearLayout liVilla;
    private TextView txtMe;
    private TextView txtOther;
    private ImageView imgBuilding;
    private ImageView imgVilla;

    private TextView txtBuildings;
    private TextView txtVilla;
    private LinearLayout liMe;
    private LinearLayout liOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_pickup_location);


        initializeViews();
    }

    private void initializeViews() {
        TextView txtHeader=(TextView)findViewById(R.id.txt_activty_header);
        txtHeader.setText(getResources().getString(R.string.txt_pick_up));
        imgBack=(ImageView)findViewById(R.id.backImage_header);

        edtPickupLocation=(EditText)findViewById(R.id.edt_pickupLocation);
        edtFloorNumber=(EditText)findViewById(R.id.edt_floor_number);
        edtUnitNumber=(EditText)findViewById(R.id.edt_unit_number);
        edtContactPersonname=(EditText)findViewById(R.id.edt_contact_peron_name);
        edtContactPersonNumber=(EditText)findViewById(R.id.edt_edt_contact_person_number);
        edtComments=(EditText)findViewById(R.id.edt_comments);
        txtMe=(TextView)findViewById(R.id.txt_me);
        txtOther=(TextView)findViewById(R.id.txt_other);
        txtBuildings=(TextView)findViewById(R.id.txt_building);
        txtVilla=(TextView)findViewById(R.id.txt_villa);

        imgBuilding=(ImageView) findViewById(R.id.img_building);
        imgVilla=(ImageView) findViewById(R.id.img_villa);


        liBuilding=(LinearLayout)findViewById(R.id.li_building);
        liVilla=(LinearLayout)findViewById(R.id.li_villa);

        liMe=(LinearLayout)findViewById(R.id.li_me);
        liOther=(LinearLayout)findViewById(R.id.li_other);
        
        liMe.setOnClickListener(this);
        liOther.setOnClickListener(this);
        liBuilding.setOnClickListener(this);
        liVilla.setOnClickListener(this);
        
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backImage_header:
                finish();
                break;
            case R.id.li_me:
                liMe.setBackground(this.getResources().getDrawable(R.drawable.sm_btn));
                liOther.setBackgroundColor(this.getResources().getColor(R.color.white));
                txtMe.setTextColor(this.getResources().getColor(R.color.white));
                txtOther.setTextColor(this.getResources().getColor(R.color.grey_text_color));
                liMe.setTag(true);
                liOther.setTag(false);
                break;
            case R.id.li_other:
                liOther.setBackground(this.getResources().getDrawable(R.drawable.sm_btn));
                liMe.setBackgroundColor(this.getResources().getColor(R.color.white));
                txtOther.setTextColor(this.getResources().getColor(R.color.white));
                txtMe.setTextColor(this.getResources().getColor(R.color.grey_text_color));
                liOther.setTag(true);
                liMe.setTag(false);
                break;

            case R.id.li_building:
                liBuilding.setBackground(this.getResources().getDrawable(R.drawable.sm_btn));
                liVilla.setBackgroundColor(this.getResources().getColor(R.color.white));
                txtBuildings.setTextColor(this.getResources().getColor(R.color.white));
                txtVilla.setTextColor(this.getResources().getColor(R.color.grey_text_color));
                imgBuilding.setImageResource(R.drawable.ac_home);
                imgVilla.setImageResource(R.drawable.de_villa);
                liBuilding.setTag(true);
                liVilla.setTag(false);
                break;
            case R.id.li_villa:
                liBuilding.setBackgroundColor(this.getResources().getColor(R.color.white));
                liVilla.setBackground(this.getResources().getDrawable(R.drawable.sm_btn));
                txtBuildings.setTextColor(this.getResources().getColor(R.color.grey_text_color));
                txtVilla.setTextColor(this.getResources().getColor(R.color.white));
                imgBuilding.setImageResource(R.drawable.de_home);
                imgVilla.setImageResource(R.drawable.ac_home);
                liBuilding.setTag(false);
                liVilla.setTag(true);
                break;
        }

    }


    }


