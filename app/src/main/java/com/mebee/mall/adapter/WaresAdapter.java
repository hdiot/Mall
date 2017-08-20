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
import com.mebee.mall.bean.Wares;

import java.util.List;

/**
 *
 * Created by mebee on 2017/8/3.
 */

public class WaresAdapter extends RecyclerView.Adapter<WaresAdapter.ViewHolder> {

    private static final String TAG = "WaresAdapter";

    private static final String RECYCLERVIEW = "ADAPTER";
    private LayoutInflater mInflater;
    private List<Wares> mWares;
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

    public WaresAdapter(ITEMLAYOUTTYPE itemlayoutType, List<Wares> wares) {
        Log.d(RECYCLERVIEW, "WaresAdapter");
        this.mWares = wares;
        this.mLayoutType = itemlayoutType;
    }

    @Override
    public WaresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(RECYCLERVIEW, "onCreateViewHolder:");
        mInflater = LayoutInflater.from(parent.getContext());

        if (mLayoutType == ITEMLAYOUTTYPE.VERTICAL) {
            mView = mInflater.inflate(R.layout.ware_item_view_vertical,parent,false);
        }else {
            mView = mInflater.inflate(R.layout.ware_item_view_horizon,parent,false);
        }

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.goodImage.setImageURI(mWares.get(position).getPicture_name_path());
        holder.goodName.setText(mWares.get(position).getName());
        holder.goodPrice.setText(String.valueOf(mWares.get(position).getPrice()));
        holder.goodProductPlace.setText(mWares.get(position).getProducing_area());

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
        return mWares ==null ? 0 : mWares.size();
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
