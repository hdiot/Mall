package com.mebee.mall.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mebee.mall.R;
import com.mebee.mall.bean.ShoppingCart;

import java.math.BigDecimal;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by mebee on 2017/8/26.
 */

public class ChargeAdapter extends RecyclerView.Adapter<ChargeAdapter.ViewHolder> {

    private List<ShoppingCart> mCarts;
    private LayoutInflater mInflater;

    public ChargeAdapter(List<ShoppingCart> mCarts) {
        this.mCarts = mCarts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.cart_item_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d(TAG, "position: "+position);

        ShoppingCart cart = mCarts.get(position);

        holder.isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        holder.isSelected.setChecked(cart.isChecked());
        holder.picture.setImageURI(cart.getPicture_name_path());
        holder.name.setText(cart.getName());
        holder.price.setText(String.valueOf(cart.getPrice())+"ิช/ฝ๏");
        holder.volume.setText(String.valueOf(cart.getCount()));
        holder.totalPrice.setText(getTotalPrice(cart)+"ิช");

    }

    private double getTotalPrice(ShoppingCart cart) {

        double totalPrice =cart.getCount() * cart.getPrice();
        BigDecimal decimal = new BigDecimal(totalPrice);

        totalPrice = decimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        return totalPrice;
    }

    @Override
    public int getItemCount() {
        return mCarts == null? 0 : mCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox isSelected;
        SimpleDraweeView picture;
        TextView name;
        TextView price;
        EditText volume;
        TextView totalPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            isSelected = (CheckBox) itemView.findViewById(R.id.cb_select_cart);
            isSelected.setVisibility(View.GONE);
            picture = (SimpleDraweeView) itemView.findViewById(R.id.dv_picture_cart);
            name = (TextView) itemView.findViewById(R.id.txt_name_cart);
            price = (TextView) itemView.findViewById(R.id.txt_price_cart);
            volume = (EditText) itemView.findViewById(R.id.et_volume_cart);
            volume.setEnabled(false);
            totalPrice = (TextView) itemView.findViewById(R.id.txt_price_tatal_cart);
        }
    }
}
