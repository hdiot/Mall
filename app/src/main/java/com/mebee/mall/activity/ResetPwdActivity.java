package com.mebee.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mebee.mall.R;
import com.mebee.mall.bean.ResMessage;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.UserProvider;
import com.mebee.mall.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

public class ResetPwdActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_reset_pwd)
    Toolbar toolbarResetPwd;
    @BindView(R.id.et_id_reset_pwd)
    ClearEditText etIdResetPwd;
    @BindView(R.id.et_pwd_reset_pwd)
    ClearEditText etPwdResetPwd;
    @BindView(R.id.et_pwd2_reset_pwd)
    ClearEditText etPwd2ResetPwd;
    @BindView(R.id.btn_submit_reset_pwd)
    Button btnSubmitResetPwd;

    private OkhttpHelper mOkHttpHelper;
    private UserProvider mUserProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.bind(this);
        mOkHttpHelper = OkhttpHelper.getInstance();
        mUserProvider = UserProvider.getInstance(this);
        etIdResetPwd.setText(mUserProvider.getmUser().getUser_name());
        setListener();
    }

    private void setListener() {
        toolbarResetPwd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmitResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwd();
            }
        });
    }

    private void resetPwd() {
        String pwd = etPwdResetPwd.getText().toString();
        String pwd2 = etPwd2ResetPwd.getText().toString();

        if (pwd == "" || pwd2 == "") {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("name" , mUserProvider.getmUser().getUser_name());
            json.put("NewPassword", pwd);
            json.put("AgainPassword",pwd2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mOkHttpHelper.doPost(Constant.API.RESETPASSWORD_API, json.toString(), new BaseCallback<ResMessage<String>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(ResetPwdActivity.this, R.string.netword_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSuccess(Response response, ResMessage<String> resMessage) {
                String rs = resMessage.getResult();
                if (rs.contains("PasswordInconformity")) {
                    Toast.makeText(ResetPwdActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                } else if (rs.contains("Success")) {
                    Toast.makeText(ResetPwdActivity.this, "密码修改成功，重新登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPwdActivity.this,LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(ResetPwdActivity.this, R.string.netword_fail + code, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
