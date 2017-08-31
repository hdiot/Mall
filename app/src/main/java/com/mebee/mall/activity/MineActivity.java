package com.mebee.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mebee.mall.R;
import com.mebee.mall.utils.UserProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_mine)
    Toolbar toolbarMine;
    @BindView(R.id.sdv_head_mine)
    SimpleDraweeView sdvHeadMine;
    @BindView(R.id.txt_headname_mine)
    TextView txtHeadnameMine;
    @BindView(R.id.txt_name_mine)
    TextView txtNameMine;
    @BindView(R.id.txt_id_mine)
    TextView txtIdMine;
    @BindView(R.id.txt_pwd_manage_mine)
    TextView txtPwdManageMine;
    @BindView(R.id.txt_address_manage_mine)
    TextView txtAddressManageMine;
    @BindView(R.id.txt_about_mine)
    TextView txtAboutMine;
    @BindView(R.id.txt_logout_mine)
    TextView txtLogoutMine;

    private UserProvider mUserProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        mUserProvider = UserProvider.getInstance(this);
        setListener();
        setUserData();
    }

    private void setUserData(){
        txtHeadnameMine.setText(mUserProvider.getmUser().getUser_name());
        txtNameMine.setText(mUserProvider.getmUser().getUser_name());
        txtIdMine.setText(mUserProvider.getmUser().getUser_id());
        sdvHeadMine.setImageURI(mUserProvider.getmUser().getHead_path().replaceAll("\\\\","\\/"));
    }

    private void setListener() {
        toolbarMine.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtPwdManageMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineActivity.this,ResetPwdActivity.class));
            }
        });
        txtAddressManageMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineActivity.this, AddressActivity.class));
            }
        });

        txtAboutMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineActivity.this, AboutActivity.class));
            }
        });

        txtLogoutMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserProvider.clearData();
                startActivity(new Intent(MineActivity.this,LoginActivity.class));
                finish();
                finishAffinity();
            }
        });
    }
}
