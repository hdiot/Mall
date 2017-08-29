package com.mebee.mall.activity;

import android.app.Activity;
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
import com.mebee.mall.bean.Ware;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WareDetailActivity extends Activity {

    @BindView(R.id.toolbar_ware_detail)
    Toolbar toolbarWareDetail;
    @BindView(R.id.slider_ware_detail)
    SliderLayout sliderWareDetail;
    @BindView(R.id.txt_ware_name)
    TextView txtWareName;
    @BindView(R.id.txt_ware_price)
    TextView txtWarePrice;
    @BindView(R.id.bt_reduce_ware)
    Button btReduceWare;
    @BindView(R.id.et_volume_ware)
    EditText etVolumeWare;
    @BindView(R.id.bt_add_ware)
    Button btAddWare;
    private Ware mWare;

    /*private OkhttpHelper mOkhttpHelper;
    private Toolbar toolbarWareDetail;
    private SliderLayout sliderWareDetail;
    private TextView txtWareName;
    private TextView txtWarePrice;
    private EditText etVolumeWare;
    private Button btReduceWare;
    private Button btAddWare;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        ButterKnife.bind(this);
        mWare = (Ware) getIntent().getSerializableExtra("good");
        initView();
    }

    private void initView() {
        toolbarWareDetail = (Toolbar) findViewById(R.id.toolbar_ware_detail);
        etVolumeWare = (EditText) findViewById(R.id.et_volume_ware);
        sliderWareDetail = (SliderLayout) findViewById(R.id.slider_ware_detail);
        txtWareName = (TextView) findViewById(R.id.txt_ware_name);
        btReduceWare = (Button) findViewById(R.id.bt_reduce_ware);
        btAddWare = (Button) findViewById(R.id.bt_add_ware);
        txtWarePrice = (TextView) findViewById(R.id.txt_ware_price);


        initToolbar();
        initSlider();
        initWareInfo();
    }

    private void initWareInfo() {
        txtWareName.setText(mWare.getName());
        txtWarePrice.setText(String.valueOf(mWare.getPrice()));
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
        sliderWareDetail.addSlider(textSliderView);
    }
}
