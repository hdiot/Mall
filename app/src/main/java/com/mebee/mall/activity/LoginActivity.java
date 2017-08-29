package com.mebee.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.mebee.mall.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final CharSequence NOTEXISTERROR = "NotExistError";
    private static final CharSequence PASSWORDERROR = "PasswordError";
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

    /*private Toolbar toolbarLogin;
    private ClearEditText etIdLogin;
    private ClearEditText etPwdLogin;
    private Button btnSigningLogin;
    private TextView txtRegisteLogin;
    private LinearLayout activityLogin;*/

    private OkhttpHelper mOkhttpHelper;
    private UserProvider mUserProvider;
    private String mID;
    private String mPWD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mOkhttpHelper = OkhttpHelper.getInstance();
        mUserProvider = UserProvider.getInstance(this);
        setListener();
    }

    private void initView() {
        toolbarLogin = (Toolbar) findViewById(R.id.toolbar_login);
        etIdLogin = (ClearEditText) findViewById(R.id.et_id_login);
        etPwdLogin = (ClearEditText) findViewById(R.id.et_pwd_login);
        btnSigningLogin = (Button) findViewById(R.id.btn_signing_login);
        txtRegisteLogin = (TextView) findViewById(R.id.txt_registe_login);
    }

    /**
     * 设置点击监听
     */
    private void setListener() {
        toolbarLogin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSigningLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        txtRegisteLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisteActivity.class));
            }
        });
    }


    /**
     * 登录
     */
    private void login() {

        mID = etIdLogin.getText().toString();
        mPWD = etPwdLogin.getText().toString();

        // 判断信息是否为空
        if (mID.equals("") || mPWD.equals("")) {
            Toast.makeText(this, R.string.please_to_input_name_pwd, Toast.LENGTH_SHORT).show();
            return;
        }

        // 将信息转为 Json
        JSONObject json = new JSONObject();
        try {
            json.put(getString(R.string.login_key_name), mID);
            json.put(getString(R.string.login_key_pwd), mPWD);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 登录请求
        mOkhttpHelper.doPost(Constant.API.LOGIN_API, json.toString(), new BaseCallback<String>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(LoginActivity.this, R.string.netword_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSuccess(Response response, String s) {
                if (s.contains(NOTEXISTERROR)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.user_not_exist), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (s.contains(PASSWORDERROR)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.password_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                ResMessage<User> loginMsg = JSONUtil.fromJson(s, new TypeToken<ResMessage<User>>() {
                }.getType());

                String cookie = response.headers().get(SET_COOKIE);
                Log.d(TAG, "OnSuccess: " + s + "--" + cookie);
                mUserProvider.putUser(loginMsg.getResult(), cookie);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(LoginActivity.this, R.string.netword_fail + code, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
