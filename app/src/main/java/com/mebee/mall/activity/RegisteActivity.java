package com.mebee.mall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mebee.mall.R;
import com.mebee.mall.bean.ResponseMessage;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

public class RegisteActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_register)
    Toolbar toolbarRegister;
    @BindView(R.id.et_id_register)
    ClearEditText etIdRegister;
    @BindView(R.id.et_pws_registe)
    ClearEditText etPwsRegiste;
    @BindView(R.id.btn_signing_registe)
    Button btnSigningRegiste;

    private OkhttpHelper mOkhttpHelper = OkhttpHelper.getInstance();
    private String mId;
    private String mPwd;

    private static final String TAG = "RegisteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar(){
        toolbarRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_signing_registe)
    public void onViewClicked() {
        registe();
    }

    private void registe(){
        mId = etIdRegister.getText().toString();
        mPwd = etPwsRegiste.getText().toString();

        if (mId.isEmpty() || mPwd.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("user_name", mId);
            json.put("password", mPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mOkhttpHelper.doPost(Constant.API.REGISTE_API, json.toString(), new BaseCallback<ResponseMessage<String>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(RegisteActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSuccess(Response response, ResponseMessage<String> msg) {
                Log.d(TAG, "OnSuccess: " + msg.getResult());
                if (msg.getResult().equals("UserExist")) {
                    Toast.makeText(RegisteActivity.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (msg.getResult().equals("RegisterSuccess")) {
                    Toast.makeText(RegisteActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                }

            }


            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(RegisteActivity.this, "网络出错" + code, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
