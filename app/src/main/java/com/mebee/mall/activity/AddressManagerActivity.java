package com.mebee.mall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.mebee.mall.R;
import com.mebee.mall.adapter.AddressAdapter;
import com.mebee.mall.bean.Address;
import com.mebee.mall.utils.AddressProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressManagerActivity extends Activity {

    private static final String TAG = "AddressManagerActivity";

    @BindView(R.id.toolbar_addressManage)
    Toolbar toolbarAddress;
    @BindView(R.id.rv_addrs_addressManage)
    RecyclerView rvAddrsAddressManage;
    @BindView(R.id.btn_add_addressManage)
    Button btnAddAddressManage;

    private AddressProvider mAddressProvider;
    private List<Address> mAddresses;
    private AddressAdapter mAddressAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addres_managers);
        ButterKnife.bind(this);
        mAddressProvider = AddressProvider.getInstance(this);
        setListener();
        initAddressesData();

    }

    private void setListener() {
        toolbarAddress.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });btnAddAddressManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressManagerActivity.this,InsertAddressActivity.class));
            }
        });
    }

    private void initAddressesData() {
        mAddresses = mAddressProvider.getAll();
        mAddressAdapter = new AddressAdapter(this);
        rvAddrsAddressManage.setAdapter(mAddressAdapter);
        rvAddrsAddressManage.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateAddressAdapter(){
        if (mAddressAdapter != null) {
            mAddressAdapter.updateAdapter();
        }
    }


    public void setOnItemClickListener(AddressAdapter.OnClickListener listener){
        if (mAddressAdapter != null) {
            mAddressAdapter.setOnClickListener(listener);
        }
    }



}
