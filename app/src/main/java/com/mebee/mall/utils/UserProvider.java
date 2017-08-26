package com.mebee.mall.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mebee.mall.bean.User;

/**
 * Created by mebee on 2017/8/24.
 */

public class UserProvider {
    private static final String COOKIE = "cookie";
    private static final String USER_JSON = "user";
    private User mUser;
    private String mCookie = "";
    private static UserProvider mInstance;
    private Context mContext;

    private static final String TAG = "UserProvider";


    public static UserProvider getInstance(Context context) {

        if (mInstance == null) {
            synchronized (CartProvider.class){
                if (mInstance == null) {
                    mInstance = new UserProvider(context);
                }
                return mInstance;
            }
        }

        return mInstance;
    }

    private UserProvider(Context context) {
        this.mContext = context;
        this.mCookie = "";
        this.mUser = new User();
        getCookieFromLocal();
        getUserFromLocal();
    }

    private void getUserFromLocal(){
        String userJson = PreferencesUtils.getString(mContext,USER_JSON);
        mUser = JSONUtil.fromJson(userJson,new TypeToken<User>(){}.getType());
        Log.d(TAG, "getUserFromLocal: " + userJson);
    }

    private void getCookieFromLocal(){
        mCookie = PreferencesUtils.getString(mContext,COOKIE,"");
        Log.d(TAG, "getCookieFromLocal: " + mCookie);
    }

    private void update(){
        PreferencesUtils.putString(mContext,COOKIE, mCookie);
        PreferencesUtils.putString(mContext,USER_JSON,JSONUtil.toJSON(mUser));
    }

    public void putUser(User user, String cookie){
        this.mUser = user;
        this.mCookie = cookie;
        update();
    }

    public void clearData(){
        this.mUser = null;
        this.mCookie = "";
        PreferencesUtils.remove(mContext,USER_JSON);
        PreferencesUtils.remove(mContext,COOKIE);
    }

    public String getmCookie() {
        return mCookie;
    }

    public User getmUser() {
        return mUser;
    }
}
