package com.mebee.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mebee.mall.utils.CartProvider;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mebee on 2017/8/20.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>  {

    private CartProvider mProvider;
    private LayoutInflater mInflater;
    private View mView;
    private List<ShoppingCart> mCarts;
    private OnDataUpdateListener mCallBack;

    private static final String TAG = "CartAdapter";

    public CartAdapter(Context context, List<ShoppingCart> carts, OnDataUpdateListener callback) {
        mProvider = CartProvider.getInstance(context);
        mCarts = carts;
        mCallBack = callback;

        Log.d(TAG, "cart: create" + mCarts.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mInflater = LayoutInflater.from(parent.getContext());
        mView = mInflater.inflate(R.layout.cart_item_view,parent,false);

        return new ViewHolder(mView);
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
        holder.isSelected.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cart.setCheck(!cart.isChecked());
            commit(cart);
            checkListen();
            mCallBack.onUpdate();
        });

        holder.volume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String count = s.toString();
                if (count.equals(""))
                    count = String.valueOf(cart.getCount());
                if (count.equals("0"))
                    count = "1";
                cart.setCount(Integer.valueOf(count));
                commit(cart);
                mCallBack.onUpdate();
                holder.totalPrice.setText(getTotalPrice(cart)+"ิช/ฝ๏");
            }
        });
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
            picture = (SimpleDraweeView) itemView.findViewById(R.id.dv_picture_cart);
            name = (TextView) itemView.findViewById(R.id.txt_name_cart);
            price = (TextView) itemView.findViewById(R.id.txt_price_cart);
            volume = (EditText) itemView.findViewById(R.id.et_volume_cart);
            totalPrice = (TextView) itemView.findViewById(R.id.txt_price_tatal_cart);
        }
    }


    private void checkListen() {
        int count = 0;
        int checkNum = 0;
        if (!isCartsNull()) {
            count = getItemCount();
            for (ShoppingCart cart : mCarts) {
                if (!cart.isChecked()) {
                    mCallBack.onChecked(false);
                    return;
                }
                checkNum += 1;
            }
            if (count == checkNum)
                mCallBack.onChecked(true);
        }
    }

    public void deleteCart() {
        if (!isCartsNull()) {
            for (Iterator iterator = mCarts.iterator(); iterator.hasNext(); ) {
                ShoppingCart cart = (ShoppingCart) iterator.next();
                if (cart.isChecked()) {
                    int position = mCarts.indexOf(cart);
                    Log.d(TAG, "deleteCart: " + cart.getName());
                    mProvider.delete(cart);
                    iterator.remove();
                    notifyItemRemoved(position);
                    mCallBack.onUpdate();
                }
            }

        }
    }

    public void changeItemCheckStatus(boolean ischeck){
        if ( !isCartsNull()){
            int i = 0;
            for (ShoppingCart cart : mCarts) {
                cart.setCheck(ischeck);
                notifyItemChanged(i);
                commit(cart);
                i++;
            }
            mCallBack.onUpdate();
        }
    }

    private void commit(ShoppingCart cart) {
        mProvider.update(cart);
    }

    private boolean isCartsNull() {

        return (mCarts == null || mCarts.size() == 0);
    }

    public interface OnDataUpdateListener {
        void onUpdate();

        void onChecked(boolean isCheck);
    }
}
