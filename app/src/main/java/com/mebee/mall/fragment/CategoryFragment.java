package com.mebee.mall.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mebee.mall.R;
import com.mebee.mall.adapter.CategoryAdapter;
import com.mebee.mall.adapter.WaresAdapter;
import com.mebee.mall.bean.Wares;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.widget.MyToolbar;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/1.
 */
public class CategoryFragment extends BaseFragment {

    private MyToolbar mToolbar;
    private RecyclerView mCategoryRV;
    private RecyclerView mWaresRV;
    private TabLayout mTabLayout;
    private List<Wares> mWares;
    private OkhttpHelper mOkhttpHelper = OkhttpHelper.getInstance();


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        return view;
    }

    @Override
    public void initView(View view) {
        mToolbar = (MyToolbar) view.findViewById(R.id.toolbar_category);
        mCategoryRV = (RecyclerView) view.findViewById(R.id.rv_categorylist);
        mWaresRV = (RecyclerView) view.findViewById(R.id.rv_wares_caregory);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout_category);
    }

    @Override
    public void initData() {
        initTabLayout();
        initCategoryRecView();
        initWaresRecView();
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
    }

    @Override
    public void initRecyclerView() {

    }

    @Override
    public void initRecyclerData() {

    }

    private void initTabLayout(){
        TabLayout.Tab tab;
        tab = mTabLayout.newTab();
        tab.setText("综合");
        tab.setTag("TAG_COMPRE");
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag("TAG_VOLUNE");
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag("TAG_PRICE");
        mTabLayout.addTab(tab);

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

    private void initCategoryRecView(){
        mCategoryRV.setAdapter(new CategoryAdapter());
        mCategoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
}

    private  void initWaresRecView(){
        mOkhttpHelper.doPost(Constant.API.ALL_WARES, "" , new BaseCallback<List<Wares>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void OnSuccess(Response response, List<Wares> wares) {
                mWares = wares;
                WaresAdapter adapter = new WaresAdapter(com.mebee.mall.adapter.WaresAdapter.ITEMLAYOUTTYPE.HORIZONTAL,mWares);
                adapter.setOnItemClickListener(new WaresAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(View v, int position) {

                    }

                    @Override
                    public void onAddClick(View v, int position) {

                    }
                });
                mWaresRV.setAdapter(adapter);
                mWaresRV.setLayoutManager(new LinearLayoutManager(getContext()));
                mWaresRV.setItemAnimator(new DefaultItemAnimator());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

}
