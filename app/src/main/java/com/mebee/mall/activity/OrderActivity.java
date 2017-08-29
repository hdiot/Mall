package com.mebee.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mebee.mall.R;
import com.mebee.mall.adapter.ChargeAdapter;
import com.mebee.mall.bean.Address;
import com.mebee.mall.bean.ShoppingCart;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {

    private static final String CHARGE = "charge";
    private static final String ADDRESS = "address";
    @BindView(R.id.toolbar_order)
    Toolbar toolbarOrder;
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

    /*private Toolbar toolbarOrder;
    private TextView txtNameOrder;
    private TextView txtTelOrder;
    private TextView txtAddressOrder;
    private LinearLayout layoutAddressOrder;
    private RecyclerView recyclerviewCart;
    private TextView txtTotalPriceCart;
    private Button btnChargeOrder;
    private LinearLayout layoutOrder;
    private LinearLayout activityOrder;*/

    private static final String TAG = "OrderActivity";
    private List<ShoppingCart> mCarts;
    private Address mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        getExtrasData();
        setListener();
        initRecyclerView();
        initAddress();
    }

    private void initUI() {
        toolbarOrder = (Toolbar) findViewById(R.id.toolbar_order);
        txtNameOrder = (TextView) findViewById(R.id.txt_name_order);
        txtTelOrder = (TextView) findViewById(R.id.txt_tel_order);
        txtAddressOrder = (TextView) findViewById(R.id.txt_address_order);
        txtTotalPriceCart = (TextView) findViewById(R.id.txt_price_tatal_cart);
        btnChargeOrder = (Button) findViewById(R.id.btn_charge_order);
        layoutAddressOrder = (LinearLayout) findViewById(R.id.layout_address_order);
        recyclerviewCart = (RecyclerView) findViewById(R.id.recyclerview_cart);
    }

    /**
     * 获取 上一个 Activity 传过来的信息
     */
    private void getExtrasData() {
        Intent intent = getIntent();
        mCarts = (List<ShoppingCart>) intent.getSerializableExtra(CHARGE);
        mAddress = (Address) intent.getSerializableExtra(ADDRESS);
        Log.d(TAG, "getExtrasData: " + mCarts.size());
        Log.d(TAG, "getExtrasData: " + mAddress.getName() + mAddress.getTel() + mAddress.getAddress());
    }


    /**
     * 设置点击事件监听
     */
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

    /**
     * 初始化 订单商品 RecyclerView
     */
    private void initRecyclerView() {
        if (mCarts != null) {
            ChargeAdapter adapter = new ChargeAdapter(mCarts);
            recyclerviewCart.setAdapter(adapter);
            recyclerviewCart.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    /**
     * 初始化地址数据
     */
    private void initAddress() {
        if (mAddress != null) {
            txtNameOrder.setText(mAddress.getName());
            txtTelOrder.setText(mAddress.getTel());
            txtAddressOrder.setText(mAddress.getAddress());
        }
    }

}
