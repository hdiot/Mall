package com.mebee.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mebee.mall.R;
import com.mebee.mall.bean.ShoppingCart;
import com.mebee.mall.widget.MyToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {

    private static final String CHARGE = "charge";
    @BindView(R.id.toolbar_order)
    MyToolbar toolbarOrder;
    @BindView(R.id.txt_name_order)
    TextView txtNameOrder;
    @BindView(R.id.txt_tel_order)
    TextView txtTelOrder;
    @BindView(R.id.txt_address_order)
    TextView txtAddressOrder;
    @BindView(R.id.layout_address_order)
    LinearLayout layoutAddressOrder;
    @BindView(R.id.recyclerview_cart)
    RecyclerView recyclerviewCart;
    @BindView(R.id.txt_total_price_cart)
    TextView txtTotalPriceCart;
    @BindView(R.id.btn_charge_order)
    Button btnChargeOrder;
    @BindView(R.id.layout_order)
    LinearLayout layoutOrder;
    @BindView(R.id.activity_order)
    LinearLayout activityOrder;

    private static final String TAG = "OrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        getExtrasData();
        setListener();
    }

    private void getExtrasData(){
        Intent intent = getIntent();
        List<ShoppingCart> carts = (List<ShoppingCart>) intent.getSerializableExtra(CHARGE);
        Log.d(TAG, "getExtrasData: " + carts.size());
    }


    private void setListener() {
        toolbarOrder.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layoutAddressOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btnChargeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initRecyclerView(){

    }

}
