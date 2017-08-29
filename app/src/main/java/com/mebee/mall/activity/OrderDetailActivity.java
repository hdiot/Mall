package com.mebee.mall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mebee.mall.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_orderDetail)
    Toolbar toolbarOrderDetail;
    @BindView(R.id.txt_tel_orderDetail)
    TextView txtTelOrderDetail;
    @BindView(R.id.txt_address_orderDetail)
    TextView txtAddressOrderDetail;
    @BindView(R.id.txt_orderId_orderDetail)
    TextView txtOrderIdOrderDetail;
    @BindView(R.id.txt_receiverName_orderDetail)
    TextView txtReceiverNameOrderDetail;
    @BindView(R.id.txt_warescount_orderDetail)
    TextView txtWarescountOrderDetail;
    @BindView(R.id.xrv_wares_orderDetail)
    XRecyclerView xrvWaresOrderDetail;
    @BindView(R.id.txt_totalpri_orderDetail)
    TextView txtTotalpriOrderDetail;
    @BindView(R.id.txt_postFree_orderDetail)
    TextView txtPostFreeOrderDetail;
    @BindView(R.id.txt_payment_orderdetail)
    TextView txtPaymentOrderdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        toolbarOrderDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
