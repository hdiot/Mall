package com.mebee.mall.fragment.homefragment;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.mebee.mall.R;
import com.mebee.mall.activity.WareDetailActivity;
import com.mebee.mall.adapter.GoodRecyclerAdapter;
import com.mebee.mall.bean.Good;
import com.mebee.mall.bean.SliderLayoutData;
import com.mebee.mall.fragment.BaseFragment;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/1.
 */
public class RecommendFragment extends BaseFragment {

    private LayoutInflater mInflater;
    private SliderLayout mSliderLayout;
    private RecyclerView mRecyclerView;
    private OkhttpHelper mOkHttpHelper = OkhttpHelper.getInstance();
    private Gson mGson = new Gson();
    private List<Good> mGoods;

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
        initRecyclerView();
    }

    @Override
    public void initSliderData() {
        super.initSliderData();


        List<SliderLayoutData> sList = new ArrayList<>();

        SliderLayoutData sData1 = new SliderLayoutData("瓜类","http://119.23.33.62:8080/Fruit_Store/GoodsImage/瓜类image/木瓜-瓜类.png");
        SliderLayoutData sData2 = new SliderLayoutData("橘类","http://119.23.33.62:8080/Fruit_Store/GoodsImage/柑橘类image/橙子-柑橘类.png");
        SliderLayoutData sData3 = new SliderLayoutData("仁果","http://119.23.33.62:8080/Fruit_Store/GoodsImage/仁果类image/枇杷-仁果类.png");
        SliderLayoutData sData4 = new SliderLayoutData("其他","http://119.23.33.62:8080/Fruit_Store/GoodsImage/其它image/生菜-其它.png");

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

    }

    @Override
    public void initRecyclerView() {
        super.initRecyclerView();

        if (mGoods==null) {
            initRecyclerData();
        }
        setRecyclerLayout();
    }

    public void setRecyclerLayout(){
        GoodRecyclerAdapter goodRecyclerAdapter =
                new GoodRecyclerAdapter(GoodRecyclerAdapter.ITEMLAYOUTTYPE.VERTICAL,mGoods);

        goodRecyclerAdapter.setOnItemClickListener(new GoodRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra("good", mGoods.get(position));
                startActivity(intent);
            }

            @Override
            public void onAddClick(View v, int position) {

            }
        });



        mRecyclerView.setAdapter(goodRecyclerAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position==0?3:1;
            }
        });
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 20;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    public void initRecyclerData() {
        super.initRecyclerData();
        String url = "http://119.23.33.62:8080/Fruit_Store/fruit/getAll.action";
        mOkHttpHelper.doPost(url, "", new BaseCallback<List<Good>>() {

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void OnSuccess(Response response, List<Good> goods) {
                Log.d("OkHttpHelper", "OnSuccess: "+goods.size());
                for (Good good : goods) {
                    Log.d("OkHttpHelper", "id: "+good.getId()+"--"+"name: "+good.getName());
                }
                mGoods = goods;
                setRecyclerLayout();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
}
