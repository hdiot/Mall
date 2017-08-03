package com.mebee.mall.fragment.homefragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mebee.mall.R;
import com.mebee.mall.adapter.GoodRecyclerAdapter;
import com.mebee.mall.bean.SliderLayoutData;
import com.mebee.mall.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mebee on 2017/8/1.
 */
public class RecommendFragment extends BaseFragment {

    private LayoutInflater mInflater;
    private SliderLayout mSliderLayout;
    private RecyclerView mRecyclerView;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_recommend,container,false);
        return view;
    }

    @Override
    public void initView(View view) {
        mSliderLayout = (SliderLayout) view.findViewById(R.id.recommend_slider);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recommend_recyler);
    }

    @Override
    public void initData() {
        initSliderView();
    }

    @Override
    public void initSliderData() {
        super.initSliderData();


        List<SliderLayoutData> sList = new ArrayList<>();

        SliderLayoutData sData1 = new SliderLayoutData("瓜类","http://img.yimutian.com/sells/b5af84c8c75be7a366ab4ea90012305f.jpg");
        SliderLayoutData sData2 = new SliderLayoutData("橘类","http://www.mf08s.com/m/care/uploadfile/201608/15/0435542.png");
        SliderLayoutData sData3 = new SliderLayoutData("果仁","http://img4.imgtn.bdimg.com/it/u=1395053853,391671355&fm=26&gp=0.jpg");
        SliderLayoutData sData4 = new SliderLayoutData("其他","http://img1.imgtn.bdimg.com/it/u=1088597934,2532466225&fm=26&gp=0.jpg");

        sList.add(sData1);
        sList.add(sData2);
        sList.add(sData3);
        sList.add(sData4);

        for (SliderLayoutData sliderLayoutData : sList) {
            TextSliderView textSliderView = new TextSliderView(this.getActivity());
            Log.d("initSliderData: ",sliderLayoutData.getUrl());
            textSliderView
                    .description(sliderLayoutData.getDescription())
                    .image(sliderLayoutData.getUrl())
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mSliderLayout.addSlider(textSliderView);
        }

    }

    @Override
    public void initSliderView() {
        super.initSliderView();

        initSliderData();

        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setDuration(2000);

    }

    @Override
    public void initRecyclerView() {
        super.initRecyclerView();
        initRecyclerData();
    }

    @Override
    public void initRecyclerData() {
        super.initRecyclerData();
        GoodRecyclerAdapter goodRecyclerAdapter = new GoodRecyclerAdapter(null);
        mRecyclerView.setAdapter(goodRecyclerAdapter);
    }
}
