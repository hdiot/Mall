package com.mebee.mall.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mebee.mall.R;
import com.mebee.mall.bean.ResMessage;
import com.mebee.mall.bean.User;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.JSONUtil;
import com.mebee.mall.utils.UserProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends Activity {



    private static final String TAG = "SplashActivity";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final CharSequence NOTEXISTERROR = "NotExistError";
    private static final CharSequence PASSWORDERROR = "PasswordError";
    private AlertDialog mAlert;
    private View mView;
    private OkhttpHelper mOkHttpHelper;
    private UserProvider mUserProvider;
    private Timer mTimer;
    private boolean isAnimateFinish = false;
    private boolean isUserCheckFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(this).inflate(R.layout.activity_splash,null);
        setContentView(mView);

        mOkHttpHelper = OkhttpHelper.getInstance();
        mUserProvider = UserProvider.getInstance(this);
        biludAlert();
        setSplashAnima();
        getUser();

    }

    /**
     * 设置动画
     */
    private void setSplashAnima(){
        new Timer().schedule(new TimerTask() {
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
                                isAnimateFinish = true;
                                enterHome();
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

    /**
     * 本地数据获取用户信息
     */
    private void getUser() {
        User user = mUserProvider.getmUser();
        if (user == null) {
            Toast.makeText(this, R.string.not_login, Toast.LENGTH_SHORT).show();
            call2Login();
            return;
        }

        login(user);
    }

    /**
     * 登录（已存在用户信息时自动登录）
     * @param user
     */
    private void login(User user){
        JSONObject json = new JSONObject();
        try {
            json.put(getString(R.string.login_key_name), user.getUser_name());
            json.put(getString(R.string.login_key_pwd), user.getPassword());

            if (json.isNull(getString(R.string.login_key_pwd)) ||
                    json.isNull(getString(R.string.login_key_pwd))){
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
                mAlert.show();
            }

            @Override
            public void OnSuccess(Response response, String s) {
                Log.d(TAG, "OnSuccess: " + s);
                if (s.contains(NOTEXISTERROR) && s.contains(PASSWORDERROR)) {
                    Toast.makeText(SplashActivity.this, R.string.login_overdue, Toast.LENGTH_SHORT).show();
                    call2Login();
                } else{
                    Type type = new TypeToken<ResMessage<User>>(){}.getType();
                    ResMessage<User> msg = JSONUtil.fromJson(s,type);
                    String cookie = response.header(SET_COOKIE);

                    mUserProvider.putUser(msg.getResult(),cookie);
                    isUserCheckFinish = true;
                    enterHome();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                mAlert.show();
            }
        });
    }

    /**
     * 进入主页
     */
    private void enterHome(){
        if (isAnimateFinish && isUserCheckFinish) {  // 动画和用户验证完毕后进入主页面
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    /**
     * 进入登陆页面
     */
    private void call2Login(){
        mUserProvider.clearData();
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * 创建 网络失败 Dialog
     */
    private void biludAlert(){
        mAlert = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getString(R.string.netword_fail))
                .setMessage(R.string.please_check_netword)
                .setPositiveButton(R.string.konw_it, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .create();
    }
}
