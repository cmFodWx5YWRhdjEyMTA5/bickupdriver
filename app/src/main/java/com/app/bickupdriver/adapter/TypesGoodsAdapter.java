package com.app.bickupdriver.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.R;

import java.util.ArrayList;

/**
 * Created by fluper-pc on 7/10/17.
 */

public class TypesGoodsAdapter extends BaseAdapter {

    private ArrayList<String> goodsList;
    private Activity activity;
    private String[] array;
    private LayoutInflater inflater;
    int openRow;
    public TypesGoodsAdapter(Activity activity, String[] array,int openRow){
        //this.goodsList=goodsList;
        this.openRow=openRow;
        this.array=array;
        this.activity=activity;
        inflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int i) {
        return array[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView=null;
        if(openRow==0) {
             rowView = inflater.inflate(R.layout.row_types_goods, null);
            TextView textView=(TextView)rowView.findViewById(R.id.txt_good_name);
            textView.setText(array[i]);
            ImageView imageView=(ImageView)rowView.findViewById(R.id.img_row_checkbox);
            imageView.setTag(false);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean ischecked= (boolean) view.getTag();
                    ImageView image=(ImageView)view;
                    if(!ischecked){
                        image.setImageResource(R.drawable.ac_checkbox);
                        image.setTag(true);
                    }else{
                        image.setImageResource(R.drawable.de_checkbox);
                        image.setTag(false);
                    }
                }
            });
        }else {
            rowView = inflater.inflate(R.layout.row_driver_contacts, null);
            TextView textView=(TextView)rowView.findViewById(R.id.driver_name);
            textView.setText(array[i]);
        //    ImageView imageView=(ImageView)rowView.findViewById(R.id.img_row_checkbox);
          //  imageView.setTag(false);
        }

        return rowView;
    }
}
