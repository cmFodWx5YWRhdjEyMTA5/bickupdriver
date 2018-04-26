package com.app.bickupdriver.utility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.app.bickupdriver.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;


public class ZoomActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zoom);


        ImageButton btn_close= (ImageButton) findViewById(R.id.btn_close);


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onBackPressed();
            }
        });


        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
      //  photoView.setImageResource(R.drawable.image);


        String image=getIntent().getStringExtra("avatar");
        if (image != null) {
            Picasso.with(this)
                    .load(image)
                    .placeholder(R.drawable.driver)
                    .error(R.drawable.driver)
                    .into(photoView);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
