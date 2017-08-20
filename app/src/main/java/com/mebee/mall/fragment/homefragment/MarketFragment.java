package com.mebee.mall.fragment.homefragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mebee.mall.R;
import com.mebee.mall.activity.WareDetailActivity;
import com.mebee.mall.adapter.WaresAdapter;
import com.mebee.mall.bean.Wares;
import com.mebee.mall.fragment.BaseFragment;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.widget.MyToolbar;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/1.
 */
public class MarketFragment extends BaseFragment {

    private static final String TAG = "MarketFragment";
    
    private View mView;
    private MyToolbar mToolbar;
    private SliderLayout mSliderLayout;
    private RecyclerView mRecyclerView;
    private TabLayout mTabLayout;
    private OkhttpHelper mOkhttpHelper = OkhttpHelper.getInstance();
    private List<Wares> mWares;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_market, container, false);
        return mView;
    }

    @Override
    public void initView(View view) {
        mToolbar = (MyToolbar) view.findViewById(R.id.toolbar_market);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout_market);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.sliderlayout_market);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_market);

        initTabLayout();
    }

    @Override
    public void initData() {
        initSliderData();
        initRecyclerData();
    }

    @Override
    public void initSliderData() {
        super.initSliderData();
        if (mWares == null) {
            Log.d(TAG, "initSliderData:  ");
            getWareDta();
        } else {
            //initSliderView();
        }
    }

    @Override
    public void initSliderView() {
        super.initSliderView();
        if (mWares != null) {
            TextSliderView textSliderView = new TextSliderView(this.getActivity());
            textSliderView
                    .image(mWares.get(0).getPicture_name_path())
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mSliderLayout.addSlider(textSliderView);
            textSliderView = new TextSliderView(this.getActivity());
            textSliderView
                    .image(mWares.get(mWares.size()/2).getPicture_name_path())
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mSliderLayout.addSlider(textSliderView);
            textSliderView = new TextSliderView(this.getActivity());
            textSliderView
                    .image(mWares.get(mWares.size()-1).getPicture_name_path())
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mSliderLayout.addSlider(textSliderView);
        }
    }

    private void initTabLayout() {

        Map<String, String> tabsInfo = new HashMap<>();
        tabsInfo.put("TAG_COMPRE", "综合");
        tabsInfo.put("TAG_VOLUME", "销量");
        tabsInfo.put("TAG_PRICE", "价格");

        for (Map.Entry<String, String> Entry : tabsInfo.entrySet()) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setTag(Entry.getKey());
            tab.setText(Entry.getValue());
            mTabLayout.addTab(tab);
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void initRecyclerData() {
        super.initRecyclerData();
        if (mWares != null) {
            Log.d(TAG, "initRecyclerData: ");
            getWareDta();
        } else {
            //initRecyclerView();
        }
    }

    @Override
    public void initRecyclerView() {
        super.initRecyclerView();
        if (mWares != null) {
            WaresAdapter adapter = new WaresAdapter(com.mebee.mall.adapter.WaresAdapter.ITEMLAYOUTTYPE.HORIZONTAL,mWares);
            adapter.setOnItemClickListener(new WaresAdapter.OnItemClickListener() {
                @Override
                public void onClick(View v, int position) {
                    Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                    intent.putExtra("good", mWares.get(position));
                    startActivity(intent);
                }

                @Override
                public void onAddClick(View v, int position) {

                }
            });
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setNestedScrollingEnabled(false);
        }
    }

    private void getWareDta(){
       mOkhttpHelper.doPost(Constant.API.ALL_WARES, "", new BaseCallback<List<Wares>>() {
           @Override
           public void onRequestBefore(Request request) {

           }

           @Override
           public void onFailure(Request request, IOException e) {

           }

           @Override
           public void OnSuccess(Response response, List<Wares> wares) {
               mWares = wares;
               initSliderView();
               initRecyclerView();
           }

           @Override
           public void onError(Response response, int code, Exception e) {

           }
       });
   }
}
