package com.app.bickupdriver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.bickupdriver.R;

import java.util.ArrayList;

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
public class TypesOfGoodsAdapter extends RecyclerView.Adapter {

    private final Context context;
    private String[] typesOfGoods;
     //private final ArrayList<GoodsImage> typesOfGoods;
    //private final String TAG = getClass().getSimpleName();

    /**
     * Parametrized Constructor
     * @param context Holds the context object
     * @param typesOfGoods Holds the Seller List
     */
    public TypesOfGoodsAdapter(Context context, String[] typesOfGoods) {
        this.context = context;
        this.typesOfGoods = typesOfGoods;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        try {
            // This method will inflate the custom layout and return a viewholder
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                    R.layout.goods_detail_layout, viewGroup, false);

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
            //final Seller seller = typesOfGoods.get(position);
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
            viewHolder.tvTypeOfGood = viewGroup.findViewById(R.id.tvTypeOfGood);
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
            viewHolder.tvTypeOfGood.setText(typesOfGoods[i]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        try {
            if (typesOfGoods != null)
                return typesOfGoods.length;
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

        public TextView tvTypeOfGood;
    }
}
