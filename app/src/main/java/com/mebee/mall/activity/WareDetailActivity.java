package com.mebee.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mebee.mall.R;
import com.mebee.mall.adapter.CommentAdapter;
import com.mebee.mall.bean.Ware;
import com.mebee.mall.utils.CartProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WareDetailActivity extends CheckPermissionsActivity {

    private static final String TAG = "WareDetailActivity";

    @BindView(R.id.toolbar_ware_detail)
    Toolbar toolbarWareDetail;
    @BindView(R.id.slider_ware_wareDetail)
    SliderLayout sliderWareWareDetail;
    @BindView(R.id.txt_wareName_wareDetail)
    TextView txtWareNameWareDetail;
    @BindView(R.id.txt_warePrice_wareDetail)
    TextView txtWarePriceWareDetail;
    @BindView(R.id.et_volumeWare_wareDetail)
    EditText etVolumeWareWareDetail;
    @BindView(R.id.txt_address_wareDetail)
    TextView txtAddressWareDetail;
    @BindView(R.id.view_waredetail)
    View viewWaredetail;
    @BindView(R.id.btn_add2cart_wareDetail)
    Button btnAdd2cartWareDetail;
    @BindView(R.id.txt_carCount_wareDetail)
    TextView txtCarCountWareDetail;
    @BindView(R.id.img_cart_wareDetail)
    ImageView imgCartWareDetail;
    @BindView(R.id.txt_commentCount_wareDetail)
    TextView txtCommentCountWareDetail;
    @BindView(R.id.rv_comment_wareDeatil)
    RecyclerView rvCommentWareDeatil;
    @BindView(R.id.ly_address_oderDetail)
    LinearLayout lyAddressOderDetail;
    @BindView(R.id.activity_ware_detail)
    RelativeLayout activityWareDetail;
    private Ware mWare;
    private CartProvider mCartProvider;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        ButterKnife.bind(this);
        mCartProvider = CartProvider.getInstance(this);
        mWare = (Ware) getIntent().getSerializableExtra("good");
        initView();
        setListener();
        initLocation();
        startLocation();
    }

    private void setListener() {
        imgCartWareDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WareDetailActivity.this,MainActivity.class);
                intent.putExtra("waredetail",1);
                startActivity(intent);
                Toast.makeText(WareDetailActivity.this, "跳转到购物车模块", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        btnAdd2cartWareDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add2cart();
            }
        });
        lyAddressOderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WareDetailActivity.this,AddressManagerActivity.class));
                Toast.makeText(WareDetailActivity.this, "定位功能未完善", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void add2cart() {
        String count = etVolumeWareWareDetail.getText().toString();
        int valume = Integer.valueOf(count);
        if (valume <= 0) {
            Toast.makeText(this, "请输入正确的商品数量", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < valume; i++) {
                mCartProvider.put(mWare);
            }
            Toast.makeText(this, "成功加入购物车", Toast.LENGTH_SHORT).show();
            initCartView();
        }
    }

    private void initView() {
        initWareInfo();
        initToolbar();
        initSlider();
        initComment();
        initCartView();
    }

    private void initCartView() {
        txtCarCountWareDetail.setText(mCartProvider.getAll().size() + "");
    }

    private void initWareInfo() {
        txtWareNameWareDetail.setText(mWare.getName());
        txtWarePriceWareDetail.setText(mWare.getPrice() + getString(R.string.unit));
    }

    private void initToolbar() {
        toolbarWareDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initSlider() {
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView
                .image(mWare.getPicture_name_path())
                .setScaleType(BaseSliderView.ScaleType.Fit);
        sliderWareWareDetail.addSlider(textSliderView);
    }

    private void initComment() {
        rvCommentWareDeatil.setLayoutManager(new LinearLayoutManager(this));
        rvCommentWareDeatil.setAdapter(new CommentAdapter());
        rvCommentWareDeatil.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }


    private void initLocation(){
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
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
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
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    txtAddressWareDetail.setText(location.getAddress());
                } else {
                    txtAddressWareDetail.setText("定位失败");
                }
            } else {
                txtAddressWareDetail.setText("定位失败");
            }
        }
    };

    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void startLocation(){
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
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
