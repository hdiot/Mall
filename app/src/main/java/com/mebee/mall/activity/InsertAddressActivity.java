package com.mebee.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.mebee.mall.R;
import com.mebee.mall.bean.Address;
import com.mebee.mall.utils.AddressProvider;
import com.mebee.mall.widget.AddressPicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertAddressActivity extends AppCompatActivity {

    private static final String TAG = "InsertAddressActivity";
    @BindView(R.id.toolbar_addr_insert)
    Toolbar toolbarAddrInsert;
    @BindView(R.id.et_recever_addr_insert)
    EditText etReceverAddrInsert;
    @BindView(R.id.et_tel_addr_insert)
    EditText etTelAddrInsert;
    @BindView(R.id.tv_area_addr_insert)
    TextView tvAreaAddrInsert;
    @BindView(R.id.et_detail_addr_insert)
    EditText etDetailAddrInsert;
    @BindView(R.id.bt_insert_address)
    Button btInsertAddress;
    @BindView(R.id.picker_insert_address)
    AddressPicker pickerInsertAddress;
    @BindView(R.id.activity_insert_address)
    LinearLayout activityInsertAddress;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private AddressProvider mAddressProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_address);
        ButterKnife.bind(this);
        mAddressProvider = AddressProvider.getInstance(this);
        initLocation();
        startLocation();
        setListener();
    }

    private void setListener() {

        toolbarAddrInsert.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvAreaAddrInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerInsertAddress.setVisibility(View.VISIBLE);
                btInsertAddress.setVisibility(View.GONE);
            }
        });

        pickerInsertAddress.setAddressPickFinish(new AddressPicker.AddressPickFinish() {
            @Override
            public void response(String[] s) {
                pickerInsertAddress.setVisibility(View.GONE);
                btInsertAddress.setVisibility(View.VISIBLE);
                tvAreaAddrInsert.setText(s[0]+s[1]+s[2]);
            }
        });

        btInsertAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAddress();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }


    private void insertAddress(){
        String tel = etTelAddrInsert.getText().toString();
        String receiver = etReceverAddrInsert.getText().toString();
        String area = tvAreaAddrInsert.getText().toString();
        String detail = etDetailAddrInsert.getText().toString();

        if (tel.equals("")||receiver.equals("")||area.equals("")||detail.equals("")){
            Toast.makeText(this, R.string.address_can_not_null, Toast.LENGTH_SHORT).show();
            return;
        }

        Address address = new Address();
        address.setAddress(area+detail);
        address.setName(receiver);
        address.setTel(tel);
        address.setId(Calendar.getInstance().getTime().toString());
        mAddressProvider.put(address);
        Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(InsertAddressActivity.this,AddressManagerActivity.class));
        finish();
    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setGpsFirst(true);
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setHttpTimeOut(30000);
        //可选，设置定位间隔。默认为2秒
        mOption.setInterval(2000);
        //可选，设置是否返回逆地理地址信息。默认是true
        mOption.setNeedAddress(true);
        //可选，设置是否单次定位。默认是false
        mOption.setOnceLocation(false);
        //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        mOption.setOnceLocationLatest(false);
        //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        //可选，设置是否使用传感器。默认是false
        mOption.setSensorEnable(false);
        //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setWifiScan(true);
        //可选，设置是否使用缓存定位，默认为true
        mOption.setLocationCacheEnable(true);
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    tvAreaAddrInsert.setText(location.getProvince() + " " +
                            location.getCity() + " " +
                            location.getDistrict());
                    etDetailAddrInsert.setText(location.getPoiName());
                    stopLocation();
                } else {

                    tvAreaAddrInsert.setHint("定位失败");
                }
            } else {
                tvAreaAddrInsert.setHint("定位失败");
            }
        }
    };


    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

}
