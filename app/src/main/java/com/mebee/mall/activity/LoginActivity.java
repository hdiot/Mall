package com.mebee.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mebee.mall.R;
import com.mebee.mall.bean.LoginRegisteRespMsg;
import com.mebee.mall.bean.User;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.JSONUtil;
import com.mebee.mall.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_login)
    Toolbar toolbarLogin;
    @BindView(R.id.et_id_login)
    ClearEditText etIdLogin;
    @BindView(R.id.et_pwd_login)
    ClearEditText etPwdLogin;
    @BindView(R.id.btn_signing_login)
    Button btnSigningLogin;
    @BindView(R.id.txt_registe_login)
    TextView txtRegisteLogin;

    private static final String TAG = "LoginActivity";

    private OkhttpHelper mOkhttpHelper = OkhttpHelper.getInstance();
    private String mID;
    private String mPWD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


    }

    private void login() {

        mID = etIdLogin.getText().toString();
        mPWD = etPwdLogin.getText().toString();


        if (mID.equals("") || mPWD.equals("")) {
            Toast.makeText(this, "«Î ‰»Î’À∫≈∫Õ√‹¬Î", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("name", mID);
            json.put("password", mPWD);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mOkhttpHelper.doPost(Constant.API.LOGIN_API, json.toString(), new BaseCallback<String>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void OnSuccess(Response response, String s) {
                if (s.contains("NotExistError")) {
                    Log.d(TAG, "OnSuccess: " + "’À∫≈≤ª¥Ê‘⁄");
                    return;
                }
                if (s.contains("PasswordError")) {
                    Log.d(TAG, "OnSuccess: " + "√‹¬Î¥ÌŒÛ");
                    return;
                }

                LoginRegisteRespMsg<User> loginMsg = JSONUtil.fromJson(s, new TypeToken<LoginRegisteRespMsg<User>>() {
                }.getType());

                Log.d(TAG, "OnSuccess: " + s);
                Log.d(TAG, "name: " + loginMsg.getResult().getUser_name());
                Log.d(TAG, "path: " + loginMsg.getResult().getHead_path());
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    @OnClick(R.id.btn_signing_login)
    public void onBtnSigningLoginClicked() {
        login();
    }

    @OnClick(R.id.txt_registe_login)
    public void onTxtRegisteLoginClicked() {
        startActivity(new Intent(this, RegisteActivity.class));
    }
}
