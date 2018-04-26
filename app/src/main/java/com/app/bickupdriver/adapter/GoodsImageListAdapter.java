package com.app.bickupdriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.model.GoodsImage;
import com.app.bickupdriver.utility.ConstantValues;
import com.koushikdutta.ion.Ion;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <H1>Tixus</H1>
 * <H1>SellerListAdapter</H1>
 *
 * <p>A Recycler view adapter for the list of sellers</p>
 *
 * @author Divya Thakur
 * @since 9/27/16
 * @version 1.0
 */
public class GoodsImageListAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final ArrayList<GoodsImage> goodsImageList;
    //private final String TAG = getClass().getSimpleName();

    /**
     * Parametrized Constructor
     * @param context Holds the context object
     * @param goodsImageList Holds the Seller List
     */
    public GoodsImageListAdapter(Context context, ArrayList<GoodsImage> goodsImageList) {
        this.context = context;
        this.goodsImageList = goodsImageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        try {
            // This method will inflate the custom layout and return a viewholder
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                    R.layout.row_goods_image, viewGroup, false);

            ViewHolder listHolder = new ViewHolder(mainGroup);
            this.setWidgetReferences(mainGroup, listHolder);
            return listHolder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        try {
            //final Seller seller = goodsImageList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            this.setDataOnViews(viewHolder, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the widget references
     * @param viewGroup holds the view object
     * @param viewHolder holds the view holder object
     */
    private void setWidgetReferences(ViewGroup viewGroup, ViewHolder viewHolder) {

        try {
            viewHolder.img_goods = viewGroup.findViewById(R.id.img_goods);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the data on the views
     * @param viewHolder holds the ViewHolder object
     */
    private void setDataOnViews(ViewHolder viewHolder, int i) {

        try {
            if(goodsImageList.get(i).imageUrl != null &&
                    goodsImageList.get(i).imageUrl != null) {
                Ion.with(viewHolder.img_goods)
                        .placeholder(R.drawable.good_img)
                        .load(ConstantValues.BASE_URL + "/" +
                                goodsImageList.get(i).imageUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        try {
            if (goodsImageList != null)
                return goodsImageList.size();
            else
                return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Private class to hold the view items
     */
    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public CircleImageView img_goods;
    }
}
