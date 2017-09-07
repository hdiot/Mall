package com.mebee.mall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.mebee.mall.R;
import com.mebee.mall.bean.City;
import com.yiguo.adressselectorlib.AddressSelector;
import com.yiguo.adressselectorlib.CityInterface;
import com.yiguo.adressselectorlib.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

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
    @BindView(R.id.address_selector)
    AddressSelector addressSelector;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;


    private ArrayList<City> cities1 = new ArrayList<>();
    private ArrayList<City> cities2 = new ArrayList<>();
    private ArrayList<City> cities3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_address);
        ButterKnife.bind(this);
        initLocation();
        startLocation();
        initData();
        setListen();
    }

    private void initData(){
        try {
            JSONArray jsonArray= new JSONArray(getString(R.string.cities1));
            for(int i =0;i<jsonArray.length();i++){
                cities1.add(new Gson().fromJson(jsonArray.get(i).toString(),City.class));
            }
            JSONArray jsonArray2= new JSONArray(getString(R.string.cities2));
            for(int i =0;i<jsonArray2.length();i++){
                cities2.add(new Gson().fromJson(jsonArray2.get(i).toString(),City.class));
            }
            JSONArray jsonArray3= new JSONArray(getString(R.string.cities3));
            for(int i =0;i<jsonArray3.length();i++){
                cities3.add(new Gson().fromJson(jsonArray3.get(i).toString(),City.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initAddressSelector(){
        addressSelector.setCities(cities1);
    }

    private void displaySelector(){
        initAddressSelector();
        addressSelector.setVisibility(View.VISIBLE);
        addressSelector.setEnabled(true);
    }

    private void hideSeletor(){
        addressSelector.setVisibility(View.INVISIBLE);
        addressSelector.setEnabled(false);
    }

    private void setListen() {

        addressSelector.setOnItemClickListener(new OnItemClickListener() {
            final String[] a1 = {""};
            final String[] a2 = {""};
            final String[] a3 = {""};
            @Override
            public void itemClick(AddressSelector addressSelector, CityInterface cityInterface, int i) {
                switch (i){

                    case 0:
                        addressSelector.setCities(cities2);
                        a1[0] = cityInterface.getCityName();
                        break;
                    case 1:
                        addressSelector.setCities(cities3);
                        a2[0] = cityInterface.getCityName();
                        break;
                    case 2:
                        Toast.makeText(InsertAddressActivity.this, "tabPosition ：" + i + " " + cityInterface.getCityName(), Toast.LENGTH_SHORT).show();
                        a3[0] = cityInterface.getCityName();
                        tvAreaAddrInsert.setText(a1[0]+a2[0]+a3[0]);
                        hideSeletor();
                        break;
                }
            }
        });
        addressSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()){
                    case 0:
                        addressSelector.setCities(cities1);
                        break;
                    case 1:
                        addressSelector.setCities(cities2);
                        break;
                    case 2:
                        addressSelector.setCities(cities3);
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });
        toolbarAddrInsert.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvAreaAddrInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelector();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
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
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    tvAreaAddrInsert.setText(location.getProvince() + " " + location.getCity() + " " + location.getDistrict());
                    etDetailAddrInsert.setText(location.getPoiName());
                    stopLocation();
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    tvAreaAddrInsert.setHint("定位失败");
                }
            } else {
                tvAreaAddrInsert.setHint("定位失败");
            }
        }
    };

    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        //locationOption.setNeedAddress(cbAddress.isChecked());
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        //locationOption.setGpsFirst(cbGpsFirst.isChecked());
        // 设置是否开启缓存
        //locationOption.setLocationCacheEnable(cbCacheAble.isChecked());
        // 设置是否单次定位
        //locationOption.setOnceLocation(cbOnceLocation.isChecked());
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        //locationOption.setOnceLocationLatest(cbOnceLastest.isChecked());
        //设置是否使用传感器
        //locationOption.setSensorEnable(cbSensorAble.isChecked());
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        //String strInterval = etInterval.getText().toString();
        /*if (!TextUtils.isEmpty(strInterval)) {
            try{
                // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
                locationOption.setInterval(Long.valueOf(strInterval));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }*/

        //String strTimeout = etHttpTimeout.getText().toString();
        /*if(!TextUtils.isEmpty(strTimeout)){
            try{
                // 设置网络请求超时时间
                locationOption.setHttpTimeOut(Long.valueOf(strTimeout));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }*/
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
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
