package com.mebee.mall.utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.mebee.mall.bean.Wares;
import com.mebee.mall.bean.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mebee on 2017/8/18.
 */

public class CartProvider {

    public static final  String  CART_JSON = "cart_json";

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

    private CartProvider(Context context) {
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
        List<ShoppingCart> carts = null;
        if (json != null) {
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>(){}.getType());
        } else {
            carts = new ArrayList<>();
        }

        return carts;
    }

    private ShoppingCart convertData(Wares wares) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(wares.getId());
        cart.setName(wares.getName());
        cart.setPicture_name_path(wares.getPicture_name_path());
        cart.setPrice(wares.getPrice());
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

    public void put(Wares wares) {
        ShoppingCart cart = convertData(wares);
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




}
