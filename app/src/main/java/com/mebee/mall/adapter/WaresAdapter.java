package com.mebee.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mebee.mall.R;
import com.mebee.mall.activity.WareDetailActivity;
import com.mebee.mall.bean.Ware;
import com.mebee.mall.utils.CartProvider;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 *
 * Created by mebee on 2017/8/3.
 */

public class WaresAdapter extends RecyclerView.Adapter<WaresAdapter.ViewHolder> {

    private static final String TAG = "WaresAdapter";

    private ITEMLAYOUTTYPE mLayoutType = ITEMLAYOUTTYPE.HORIZONTAL;
    private LayoutInflater mInflater;
    private List<Ware> mWares;
    private View mView;
    private OnItemClickListener mListener;
    private CartProvider mProvider;
    private Context mContext;


    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public enum ITEMLAYOUTTYPE{
        VERTICAL,
        HORIZONTAL
    }


    public WaresAdapter(ITEMLAYOUTTYPE itemlayoutType, List<Ware> wares) {
        this.mWares = wares;
        this.mLayoutType = itemlayoutType;
    }

    @Override
    public WaresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mInflater = LayoutInflater.from(parent.getContext());
        mProvider = CartProvider.getInstance(parent.getContext());
        if (mLayoutType == ITEMLAYOUTTYPE.VERTICAL) {
            if (viewType == 0) {
                mView = mInflater.inflate(R.layout.ware_item_view_horizon,parent,false);
            } else {
                mView = mInflater.inflate(R.layout.ware_item_view_vertical,parent,false);
            }
        }else {
            mView = mInflater.inflate(R.layout.ware_item_view_horizon,parent,false);
        }
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.goodImage.setImageURI(mWares.get(position).getPicture_name_path());
        holder.goodName.setText(mWares.get(position).getName());
        holder.goodPrice.setText(mWares.get(position).getPrice()+mContext.getString(R.string.unit));
        holder.goodProductPlace.setText(mWares.get(position).getProducing_area());
        holder.addIntoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProvider.put(mWares.get(position));
                Toast.makeText(mContext, R.string.add_into_car_successful, Toast.LENGTH_SHORT).show();
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WareDetailActivity.class);
                intent.putExtra("good", mWares.get(position));
                startActivity(mContext,intent,null);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position== 0 ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return mWares ==null ? 0 : mWares.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView goodImage;
        TextView goodName;
        TextView goodPrice;
        TextView goodProductPlace;
        Button addIntoCart;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            goodImage = (SimpleDraweeView) itemView.findViewById(R.id.img_good_pre);
            goodName = (TextView) itemView.findViewById(R.id.txt_good_name);
            goodPrice = (TextView) itemView.findViewById(R.id.txt_good_price);
            goodProductPlace = (TextView) itemView.findViewById(R.id.txt_good_product_place);
            addIntoCart = (Button) itemView.findViewById(R.id.btn_add_into_cart);
            view = itemView;
        }
    }

    public void loadMoreData(List<Ware> wares){
        if (wares != null) {
            int begin = getDataSize();
            addData(wares,begin);
        }
    }

    private int getDataSize() {
        return mWares == null? 0:mWares.size();
    }

    public void refreshData(List<Ware> wares){
        if (wares != null) {
            clearAll();
            addData(wares);
        }
    }

    private void addData(List<Ware> wares){
        addData(wares,0);
    }

    private void addData(List<Ware> wares, int position){
        if (wares != null && wares.size() >= 0) {
            mWares.addAll(position,wares);
            notifyItemRangeInserted(position,wares.size());
        }

    }

    private void clearAll(){
        if (mWares != null)
            mWares.clear();
        notifyDataSetChanged();
    }

    public List<Ware> getDatas(){
        return mWares;
    }

}
