package com.app.bickupdriver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.app.bickupdriver.adapter.ViewPagerAdapter;
import com.app.bickupdriver.fragments.DoccumentImagesFragment;
import com.app.bickupdriver.utility.ConstantValues;

import java.util.ArrayList;
import java.util.List;

;
;

public class ImagesActivity extends AppCompatActivity {

    private ArrayList<String> licenseImagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_imges_view_pager);
        getData();
        ViewPager viewPager=(ViewPager) findViewById(R.id.view_pager);
        ImageView imageViewCross=(ImageView) findViewById(R.id.crossImage);
        imageViewCross.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {

                                                  finish();
                                              }
                                          });


        if(licenseImagesList!=null||licenseImagesList.size()>0) {
            openImages(licenseImagesList, viewPager);
        }
    }

    private void getData() {
        licenseImagesList=getIntent().getStringArrayListExtra("list");
    }


    public  void  openImages(ArrayList<String> images, ViewPager viewPager){
        ViewPagerAdapter mPageAdapter;
        List<Fragment> fragments = buildFragments(images);
        if(viewPager.getAdapter()!=null){
            ViewPagerAdapter viewPagerAdapter=(ViewPagerAdapter)viewPager.getAdapter();
            viewPagerAdapter.notifyDataSetChanged();
        }
        mPageAdapter = new ViewPagerAdapter(this,this.getSupportFragmentManager(),fragments, images);
        viewPager.setAdapter(mPageAdapter);
    }

    private List<Fragment> buildFragments(ArrayList<String> images) {
        List<android.support.v4.app.Fragment> fragments = new ArrayList<Fragment>();
        if(images!=null) {
            for (int i = 0; i < images.size(); i++) {
                Bundle b = new Bundle();
                b.putInt("position", i);
                b.putString(ConstantValues.USER_IMAGE,images.get(i));
                fragments.add(Fragment.instantiate(this, DoccumentImagesFragment.class.getName(), b));
            }
        }
        return fragments;
    }
}
