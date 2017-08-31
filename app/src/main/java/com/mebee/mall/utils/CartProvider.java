package com.mebee.mall.utils;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.mebee.mall.bean.ShoppingCart;
import com.mebee.mall.bean.Ware;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mebee on 2017/8/18.
 */

public class CartProvider {

    public static final  String  CART_JSON = "cart_json";
    private static final String TAG = "CartProvider";

    private SparseArray<ShoppingCart> mDatas = null;
    private Context mContext;
    private static CartProvider sInstance;

    public static CartProvider getInstance(Context context){
        if (sInstance == null) {
            synchronized (CartProvider.class){
                if (sInstance == null) {
                    sInstance = new CartProvider(context);
                }

                return sInstance;
            }
        }
        return sInstance;
    }

    CartProvider(Context context) {
        mDatas = new SparseArray<>();
        mContext = context;
        list2Sparse();
    }

    private void list2Sparse() {
        List<ShoppingCart> carts = getDataFromLocal();
        if (carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                mDatas.put(cart.getId().intValue(),cart);
            }
        }
    }

    private List<ShoppingCart> sparse2List(){
        int size = mDatas.size();
        List<ShoppingCart> carts = new ArrayList<>(size);
        for (int i = 0; i < size; i++){
            carts.add(mDatas.valueAt(i));
        }
        return carts;
    }

    private List<ShoppingCart> getDataFromLocal() {

        String json = PreferencesUtils.getString(mContext,CART_JSON);
        Log.d(TAG, "getDataFromLocal: " + json);
        List<ShoppingCart> carts = null;
        if (json != null) {
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>(){}.getType());
        } else {
            carts = new ArrayList<>();
        }

        return carts;
    }

    private ShoppingCart convertData(Ware ware) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(ware.getId());
        cart.setName(ware.getName());
        cart.setPicture_name_path(ware.getPicture_name_path());
        cart.setPrice(ware.getPrice());
        return cart;
    }

    public void put(ShoppingCart cart) {
        ShoppingCart t = mDatas.get(cart.getId().intValue());
        if (t != null) {
            t.setCount(t.getCount() + 1);
        } else {
            t = cart;
            t.setCount(1);
        }
        update(t);
    }

    public void put(Ware ware) {
        ShoppingCart cart = convertData(ware);
        put(cart);
    }

    private void commit() {
        List<ShoppingCart> carts = sparse2List();
        PreferencesUtils.putString(mContext, CART_JSON, JSONUtil.toJSON(carts));
    }

    public void update(ShoppingCart cart) {
        mDatas.put(cart.getId().intValue(), cart);
        commit();
    }

    public void delete(ShoppingCart cart) {
        mDatas.delete(cart.getId().intValue());
        commit();
    }

    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }

    public void clearAll(){
        mDatas.clear();
        commit();
    }


    public String getTotalPrice(){

        double totalPrice = 0.00;
        if (mDatas != null) {
            for (int i= 0; i < mDatas.size(); i++){
                ShoppingCart carts =  mDatas.valueAt(i);
                if (carts.isChecked()) {
                    totalPrice += carts.getPrice() * carts.getCount();
                }
            }
        }
        DecimalFormat decimal = new DecimalFormat("#.00");
        Log.d(TAG, "getTotalPrice: " + decimal.format(totalPrice));
        return decimal.format(totalPrice);
    }

}
