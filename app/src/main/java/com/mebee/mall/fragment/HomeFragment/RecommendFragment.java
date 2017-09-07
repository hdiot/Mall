package com.mebee.mall.fragment.homefragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mebee.mall.R;
import com.mebee.mall.activity.WareDetailActivity;
import com.mebee.mall.adapter.WaresAdapter;
import com.mebee.mall.bean.Ware;
import com.mebee.mall.bean.SliderLayoutData;
import com.mebee.mall.fragment.BaseFragment;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/1.
 */
public class RecommendFragment extends BaseFragment {
    private static final String TAG = "RecommendFragment";

    private LayoutInflater mInflater;
    private SliderLayout mSliderLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mTxtWarm;
    private OkhttpHelper mOkHttpHelper = OkhttpHelper.getInstance();
    private List<Ware> mWares;

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
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_recommend);
        mTxtWarm = (TextView) view.findViewById(R.id.txt_fail_warm_recommend);
        mTxtWarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecyclerData();
            }
        });
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

        SliderLayoutData sData1 = new SliderLayoutData(getString(R.string.melon),"http://news.xinhuanet.com/food/2015-10/15/128316374_14447844701091n.jpg");
        SliderLayoutData sData2 = new SliderLayoutData(getString(R.string.orange),"http://119.23.33.62:8080/Fruit_Store/GoodsImage/柑橘类image/橙子-柑橘类.png");
        SliderLayoutData sData3 = new SliderLayoutData(getString(R.string.pome),"http://119.23.33.62:8080/Fruit_Store/GoodsImage/仁果类image/枇杷-仁果类.png");
        SliderLayoutData sData4 = new SliderLayoutData(getString(R.string.other),"http://119.23.33.62:8080/Fruit_Store/GoodsImage/其它image/生菜-其它.png");

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

        if (mWares ==null) {
            initRecyclerData();
        }
        setRecyclerLayout();
    }

    public void setRecyclerLayout(){
        WaresAdapter waresAdapter =
                new WaresAdapter(com.mebee.mall.adapter.WaresAdapter.ITEMLAYOUTTYPE.VERTICAL, mWares);

        waresAdapter.setOnItemClickListener(new WaresAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra("good", mWares.get(position));
                startActivity(intent);
            }

        });



        mRecyclerView.setAdapter(waresAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /*return position%4==0?3:1;*/
                return position==0?3:1;

            }
        });

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mProgressBar.setVisibility(View.GONE);
        mTxtWarm.setVisibility(View.GONE);
    }

    @Override
    public void initRecyclerData() {
        super.initRecyclerData();
        mOkHttpHelper.doPost(Constant.API.ALL_WARES, "", new BaseCallback<List<Ware>>() {

            @Override
            public void onRequestBefore(Request request) {
                mProgressBar.setVisibility(View.VISIBLE);
                mTxtWarm.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), getString(R.string.netword_fail), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                mTxtWarm.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnSuccess(Response response, List<Ware> wares) {
                Log.d("OkHttpHelper", "OnSuccess: "+ wares.size());
                for (Ware ware : wares) {
                    Log.d("OkHttpHelper", "id: "+ ware.getId()+"--"+"name: "+ ware.getName());
                }
                mProgressBar.setVisibility(View.GONE);
                mTxtWarm.setVisibility(View.GONE);
                mWares = wares;
                setRecyclerLayout();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getActivity(), getString(R.string.netword_fail) + code, Toast.LENGTH_SHORT).show();
                mTxtWarm.setVisibility(View.VISIBLE);
            }
        });

    }
}
