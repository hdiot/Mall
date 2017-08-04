package com.mebee.mall.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mebee.mall.R;
import com.mebee.mall.bean.Good;

import java.util.List;

/**
 * Created by mebee on 2017/8/3.
 */

public class GoodRecyclerAdapter extends RecyclerView.Adapter<GoodRecyclerAdapter.ViewHolder> {

    private static final String TAG = "GoodRecyclerAdapter";

    private static final String RECYCLERVIEW = "ADAPTER";
    private LayoutInflater mInflater;
    private List<Good> mGoods;
    private View mView;
    ITEMLAYOUTTYPE mLayoutType = ITEMLAYOUTTYPE.HORIZONTAL;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onClick(View v, int position);
        void onAddClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public enum ITEMLAYOUTTYPE{
        VERTICAL,
        HORIZONTAL
    }

    public GoodRecyclerAdapter(ITEMLAYOUTTYPE itemlayoutType,List<Good> goods) {
        Log.d(RECYCLERVIEW, "GoodRecyclerAdapter");
        this.mGoods = goods;
        this.mLayoutType = itemlayoutType;
    }

    @Override
    public GoodRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(RECYCLERVIEW, "onCreateViewHolder:");
        mInflater = LayoutInflater.from(parent.getContext());
        mView = mInflater.inflate(R.layout.ware_item_view_vertical,parent,false);

        if (mLayoutType == ITEMLAYOUTTYPE.VERTICAL) {
            mView = mInflater.inflate(R.layout.ware_item_view_vertical,parent,false);
        }else {
            mView = mInflater.inflate(R.layout.ware_item_view_horizon,parent,false);
        }

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.goodImage.setImageURI(mGoods.get(position).getPicture_name_path());
        holder.goodName.setText(mGoods.get(position).getName());
        holder.goodPrice.setText(String.valueOf(mGoods.get(position).getPrice()));
        holder.goodProductPlace.setText(mGoods.get(position).getProducing_area());

        holder.addIntoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddClick(v,position);
                Log.d(TAG, "onClick: add ");
            }
        });

        holder.ItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v,position);
                Log.d(TAG, "onClick: item ");
            }
        });
    }


    @Override
    public int getItemCount() {
        return mGoods==null ? 0 : mGoods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "ViewHolder";
        SimpleDraweeView goodImage;
        TextView goodName;
        TextView goodPrice;
        TextView goodProductPlace;
        Button addIntoCart;
        View ItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: ");
            goodImage = (SimpleDraweeView) itemView.findViewById(R.id.img_good_pre);
            goodName = (TextView) itemView.findViewById(R.id.txt_good_name);
            goodPrice = (TextView) itemView.findViewById(R.id.txt_good_price);
            goodProductPlace = (TextView) itemView.findViewById(R.id.txt_good_product_place);
            addIntoCart = (Button) itemView.findViewById(R.id.btn_add_into_cart);
            ItemView = itemView;
        }
    }

}
