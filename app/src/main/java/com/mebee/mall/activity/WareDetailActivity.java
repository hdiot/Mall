package com.mebee.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mebee.mall.R;
import com.mebee.mall.adapter.CommentAdapter;
import com.mebee.mall.bean.Ware;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WareDetailActivity extends Activity {

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
    private Ware mWare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        ButterKnife.bind(this);
        mWare = (Ware) getIntent().getSerializableExtra("good");

        initView();
    }

    private void initView() {
        initWareInfo();
        initToolbar();
        initSlider();
        initComment();
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

}
