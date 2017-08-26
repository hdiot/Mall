package com.mebee.mall.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mebee.mall.bean.Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mebee on 2017/8/23.
 */

public class AddressProvider {
    private static final String TAG = "AddressProvider";

    public static final String ADDRESS_JSON = "address_json";
    private Map<String,Address> mAddresses = null;
    private Context mContext;
    private static AddressProvider sInstance;

    public static AddressProvider getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CartProvider.class){
                if (sInstance == null) {
                    sInstance = new AddressProvider(context);
                }
                return sInstance;
            }
        }
        return sInstance;
    }

    private AddressProvider(Context context) {
        this.mContext = context;
        mAddresses = new HashMap<>();
        list2map();
    }


    private List<Address> getDataFromLocal() {

        String json = PreferencesUtils.getString(mContext,ADDRESS_JSON);
        Log.d(TAG, "getDataFromLocal: " + json);
        List<Address> addresses = new ArrayList<>();
        if (json != null) {
            addresses = JSONUtil.fromJson(json, new TypeToken<List<Address>>(){}.getType());
        } else {
            addresses = new ArrayList<>();
        }

        return addresses;
    }

    private void list2map() {
        List<Address> addresses = getDataFromLocal();
        if (addresses != null && addresses.size() > 0) {
            for (Address address : addresses) {
                mAddresses.put(address.getId(),address);
            }
        }
    }

    private List<Address> map2List(){

        List<Address> addresses = new ArrayList<>();
        for (Map.Entry<String, Address> stringAddressEntry : mAddresses.entrySet()) {
            addresses.add(stringAddressEntry.getValue());
        }
        return addresses;
    }



    public void put(Address address) {
        update(address);
    }

    private void commit() {
        PreferencesUtils.putString(mContext, ADDRESS_JSON, JSONUtil.toJSON(map2List()));
    }

    public void update(Address address) {
        mAddresses.put(address.getId(),address);
        commit();
    }

    public void delete(String key) {
        mAddresses.remove(key);
        commit();
    }

    public List<Address> getAll() {
        return getDataFromLocal();
    }

    public void clearAll(){
        mAddresses.clear();
        commit();
    }


}
