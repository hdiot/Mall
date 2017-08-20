package com.mebee.mall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mebee.mall.R;
import com.mebee.mall.bean.Wares;
import com.mebee.mall.http.OkhttpHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WareDetailActivity extends Activity {

    @BindView(R.id.slider_ware_detail)
    SliderLayout sliderWareDetail;
    @BindView(R.id.txt_ware_name)
    TextView txtWareName;
    @BindView(R.id.txt_ware_price)
    TextView txtWarePrice;
    @BindView(R.id.bt_reduce_ware)
    Button btReduceWare;
    //@BindView(R.id.et_volume_ware)
    //EditText etVolumeWare;
    @BindView(R.id.bt_add_ware)
    Button btAddWare;
    @BindView(R.id.toolbar_ware_detail)
    Toolbar toolbarWareDetail;
    private Wares mWares;
    private OkhttpHelper mOkhttpHelper;
    private Toolbar toolbar;
    private EditText etVolume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        ButterKnife.bind(this);

        mWares = (Wares) getIntent().getSerializableExtra("good");
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_ware_detail);
        etVolume = (EditText) findViewById(R.id.et_volume_ware);
        initToolbar();
        initSlider();
        initWareInfo();
    }

    private void initWareInfo() {
        txtWareName.setText(mWares.getName());
        txtWarePrice.setText(String.valueOf(mWares.getPrice()));
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WareDetailActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void initSlider() {
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView
                .image(mWares.getPicture_name_path())
                .setScaleType(BaseSliderView.ScaleType.Fit);
        sliderWareDetail.addSlider(textSliderView);
    }
}
