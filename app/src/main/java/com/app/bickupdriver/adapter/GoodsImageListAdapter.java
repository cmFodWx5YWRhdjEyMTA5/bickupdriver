package com.app.bickupdriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
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
import com.app.bickupdriver.utility.Utils;
import com.bumptech.glide.Glide;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <H1>Tixus</H1>
 * <H1>SellerListAdapter</H1>
 * <p>
 * <p>A Recycler view adapter for the list of sellers</p>
 *
 * @author Divya Thakur
 * @version 1.0
 * @since 9/27/16
 */
public class GoodsImageListAdapter extends RecyclerView.Adapter<GoodsImageListAdapter.GoodsImageViewHolder> {

    private final Context context;
    private final ArrayList<GoodsImage> goodsImageList;
    //private final String TAG = getClass().getSimpleName();

    /**
     * Parametrized Constructor
     *
     * @param context        Holds the context object
     * @param goodsImageList Holds the Seller List
     */
    public GoodsImageListAdapter(Context context, ArrayList<GoodsImage> goodsImageList) {
        this.context = context;
        this.goodsImageList = goodsImageList;
    }


    @Override
    public GoodsImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_goods_image, parent, false);
        return new GoodsImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoodsImageViewHolder holder, int position) {
        GoodsImage goodsImage = goodsImageList.get(position);
        String imageUrl = ConstantValues.BASE_URL + "/" + goodsImage.imageUrl;

        Utils.printLogs("TEST", "Image full path Path: " + imageUrl);
        Glide.with(context).load(imageUrl).into(holder.imgGoods);
    }

    @Override
    public int getItemCount() {
        if (goodsImageList != null) {
            return goodsImageList.size();
        } else return 0;
    }

    /**
     * Private class to hold the view items
     */
    public class GoodsImageViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imgGoods;

        public GoodsImageViewHolder(View itemView) {
            super(itemView);
            imgGoods = itemView.findViewById(R.id.img_goods_booking_details_fragment);
        }
    }
}
