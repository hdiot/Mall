package com.mebee.mall.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mebee.mall.R;
import com.mebee.mall.bean.Good;

import java.util.List;

/**
 * Created by mebee on 2017/8/3.
 */

public class GoodRecyclerAdapter extends RecyclerView.Adapter<GoodRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Good> mGoods;

    public GoodRecyclerAdapter(List<Good> goods) {
        this.mGoods = goods;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.good_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri uri = Uri.parse(mGoods.get(position).getPicture_name_path());
        holder.goodImage.setImageURI(uri);
        holder.goodName.setText(mGoods.get(position).getName());
        holder.goodPrice.setText(String.valueOf(mGoods.get(position).getPrice()));
        holder.goodProductPlace.setText(mGoods.get(position).getProducing_area());
    }


    @Override
    public int getItemCount() {
        return mGoods.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView goodImage;
        TextView goodName;
        TextView goodPrice;
        TextView goodProductPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            goodImage = (SimpleDraweeView) itemView.findViewById(R.id.img_good_pre);
            goodName = (TextView) itemView.findViewById(R.id.txt_good_name);
            goodPrice = (TextView) itemView.findViewById(R.id.txt_good_price);
            goodProductPlace = (TextView) itemView.findViewById(R.id.txt_good_product_place);
        }
    }
}
