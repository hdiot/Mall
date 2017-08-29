package com.mebee.mall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.mebee.mall.R;
import com.mebee.mall.bean.ResMessage;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Âß¼­¸ú LoginActivity Ò»Ñù
 */
public class RegisteActivity extends AppCompatActivity {

    private Toolbar toolbarRegister;
    private ClearEditText etIdRegister;
    private ClearEditText etPwsRegiste;
    private Button btnSigningRegiste;

    private OkhttpHelper mOkhttpHelper;
    private String mId;
    private String mPwd;

    private static final String TAG = "RegisteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        mOkhttpHelper = OkhttpHelper.getInstance();
        initUI();
        setListener();
    }

    private void setListener() {
        toolbarRegister.setNavigationOnClickListener(v -> finish());
        btnSigningRegiste.setOnClickListener(v -> registe());
    }

    private void initUI(){
        toolbarRegister = (Toolbar) findViewById(R.id.toolbar_register);
        etIdRegister = (ClearEditText) findViewById(R.id.et_id_register);
        etPwsRegiste = (ClearEditText) findViewById(R.id.et_pws_registe);
        btnSigningRegiste = (Button) findViewById(R.id.btn_signing_registe);
    }

    private void registe(){
        mId = etIdRegister.getText().toString();
        mPwd = etPwsRegiste.getText().toString();

        if (mId.isEmpty() || mPwd.isEmpty()) {
            Toast.makeText(this, R.string.please_to_input_name_pwd, Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put(getString(R.string.registe_key_name), mId);
            json.put(getString(R.string.registe_key_password), mPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mOkhttpHelper.doPost(Constant.API.REGISTE_API, json.toString(), new BaseCallback<ResMessage<String>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(RegisteActivity.this, R.string.netword_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSuccess(Response response, ResMessage<String> msg) {
                Log.d(TAG, "OnSuccess: " + msg.getResult());
                if (msg.getResult().equals("UserExist")) {
                    Toast.makeText(RegisteActivity.this, R.string.user_existed, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (msg.getResult().equals("RegisterSuccess")) {
                    Toast.makeText(RegisteActivity.this, R.string.registe_success, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(RegisteActivity.this, R.string.netword_fail + code, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
