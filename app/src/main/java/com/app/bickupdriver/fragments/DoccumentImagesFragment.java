package com.app.bickupdriver.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.ZoomActivity;
import com.squareup.picasso.Picasso;


public class DoccumentImagesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_doccument_images, container, false);
         initialize(view);
         return view;
    }

    private void initialize(View view) {
        String image = null;
        ImageView doccumentImage=view.findViewById(R.id.doccument_image);
        if(getArguments()!=null){
             image=getArguments().getString(ConstantValues.USER_IMAGE);}

        if(image!=null) {
            Picasso.with(getActivity())
                    .load(image)
                    .placeholder(R.drawable.driver)
                    .error(R.drawable.driver)
                    .into(doccumentImage);
        }


        final String finalImage = image;
        doccumentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().startActivity(new Intent(getActivity(), ZoomActivity.class).putExtra("avatar", finalImage));

            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
