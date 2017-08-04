package com.mebee.mall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mebee.mall.R;
import com.mebee.mall.bean.Good;

public class WareDetailActivity extends AppCompatActivity {

    private Good mGood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        mGood = (Good) getIntent().getSerializableExtra("good");
    }
}
