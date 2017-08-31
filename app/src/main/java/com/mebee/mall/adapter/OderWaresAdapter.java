package com.mebee.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mebee.mall.R;
import com.mebee.mall.bean.OrderDetailWare;

import java.util.List;

/**
 * Created by mebee on 2017/8/30.
 */

public class OderWaresAdapter extends RecyclerView.Adapter<OderWaresAdapter.ViewHolder> {

    private List<OrderDetailWare> mWares;
    private Context mContext;

    public OderWaresAdapter(List<OrderDetailWare> wares) {
        this.mWares = wares;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_detail_wares_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtSummation.setText(mWares.get(position).getSummation()+mContext.getString(R.string.rmb));
        holder.txtWeight.setText(mWares.get(position).getCount()+mContext.getString(R.string.weight_unit));
        holder.txtPrice.setText(mWares.get(position).getPrice()+mContext.getString(R.string.unit));
        holder.txtName.setText(mWares.get(position).getName());
        holder.sdvPicture.setImageURI(mWares.get(position).getPic_path());
    }

    @Override
    public int getItemCount() {
        return mWares == null? 0 : mWares.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView sdvPicture;
        TextView txtName;
        TextView txtPrice;
        TextView txtWeight;
        TextView txtSummation;
        public ViewHolder(View itemView) {
            super(itemView);
            sdvPicture = (SimpleDraweeView) itemView.findViewById(R.id.sdv_ware_item);
            txtName = (TextView) itemView.findViewById(R.id.txt_ware_name);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_ware_price);
            txtWeight = (TextView) itemView.findViewById(R.id.txt_ware_count);
            txtSummation = (TextView) itemView.findViewById(R.id.txt_summation);
        }
    }
}
