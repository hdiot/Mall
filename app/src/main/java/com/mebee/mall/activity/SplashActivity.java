package com.mebee.mall.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mebee.mall.R;
import com.mebee.mall.bean.ResponseMessage;
import com.mebee.mall.bean.User;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.JSONUtil;
import com.mebee.mall.utils.UserProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends Activity {

    private View mView;
    private OkhttpHelper mOkHttpHelper;
    private UserProvider mUserProvider;
    private Timer mTimer;

    private static final String TAG = "SplashActivity";
    private AlertDialog mAlert;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAlert.show();
            mUserProvider.clearData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(this).inflate(R.layout.activity_splash,null);
        setContentView(mView);
        mOkHttpHelper = OkhttpHelper.getInstance();
        mUserProvider = UserProvider.getInstance(this);
        mTimer = new Timer();
        biludAlert();
        createShedule();
        getUser();

    }

    private void createShedule(){
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> mView.animate()
                        .scaleXBy(0.1f)
                        .scaleYBy(0.1f)
                        .alphaBy(0.1f)
                        .setDuration(2000)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .start());
            }
        },0);
    }

    private void getUser() {
        User user = mUserProvider.getmUser();
        if (user == null) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("name", user.getUser_name());
            json.put("password", user.getPassword());

            if (json.isNull("name")|| json.isNull("password")){
                mUserProvider.clearData();
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mOkHttpHelper.doPost(Constant.API.LOGIN_API, json.toString(), new BaseCallback<String>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void OnSuccess(Response response, String s) {
                Log.d(TAG, "OnSuccess: " + s);
                if (s.contains("NotExistError") && s.contains("PasswordError")) {
                    call2Login();
                } else{
                    ResponseMessage<User> msg = JSONUtil
                            .fromJson(s,new TypeToken<ResponseMessage<User>>(){}.getType());
                    String cookie = response.header("Set-Cookie");
                    mUserProvider.putUser(msg.getResult(),cookie);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void call2Login(){
        mUserProvider.clearData();
        Toast.makeText(this, "未登录", Toast.LENGTH_SHORT).show();
    }

    private void biludAlert(){
        mAlert = new AlertDialog.Builder(this)
                .setTitle("网络出错")
                .setMessage("请检查你的手机是否开启数据")
                .setPositiveButton("知道了", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .create();
    }
}
