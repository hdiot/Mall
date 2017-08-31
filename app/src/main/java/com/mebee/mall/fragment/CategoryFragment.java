package com.mebee.mall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mebee.mall.R;
import com.mebee.mall.activity.SearchActivity;
import com.mebee.mall.activity.WareDetailActivity;
import com.mebee.mall.adapter.CategoryAdapter;
import com.mebee.mall.adapter.WaresAdapter;
import com.mebee.mall.bean.CategoryWares;
import com.mebee.mall.bean.Ware;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.JSONUtil;
import com.mebee.mall.utils.SortWaresUtil;
import com.mebee.mall.widget.MyToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/1.
 */
public class CategoryFragment extends BaseFragment {
    private static final String TAG = "CategoryFragment";

    private MyToolbar mToolbar;
    private RecyclerView mCategoryRV;
    private RecyclerView mWaresRV;
    private TabLayout mTabLayout;
    private List<Ware> mWares;
    private OkhttpHelper mOkhttpHelper = OkhttpHelper.getInstance();
    private CategoryAdapter mCategoryAdapter;


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
        initToolbar();
    }

    @Override
    public void initData() {
        initTabLayout();
        initCategoryRecView();
        getCategoryWares(0);
        initWaresRecyclerView(mWares);
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });
    }

    private void initTabLayout(){
        TabLayout.Tab tab;
        tab = mTabLayout.newTab();
        tab.setText(R.string.synthetical);
        tab.setTag(getString(R.string.tab_synthetical));
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText(R.string.salesvolume);
        tab.setTag(getString(R.string.tab_salesvolume));
        mTabLayout.addTab(tab);
        tab = mTabLayout.newTab();
        tab.setText(R.string.price);
        tab.setTag(getString(R.string.tag_price));
        mTabLayout.addTab(tab);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: " + tab.getPosition());
                switch (tab.getPosition()){
                    case 0:
                        initWaresRecyclerView(mWares);
                        break;
                    case 1:
                        initWaresRecyclerView(SortWaresUtil.sortByVolumeDown(mWares));
                        break;
                    case 2:
                        initWaresRecyclerView(SortWaresUtil.sortByPriceUp(mWares));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        initWaresRecyclerView(SortWaresUtil.reverse(mWares));
                        break;
                    case 1:
                        initWaresRecyclerView(SortWaresUtil.sortByVolumeUp(mWares));
                        break;
                    case 2:
                        initWaresRecyclerView(SortWaresUtil.sortByPriceDown(mWares));
                        break;
                }
            }
        });
    }

    private void initCategoryRecView(){
        mCategoryAdapter = new CategoryAdapter();
        mCategoryAdapter.setItemOnClickListener(new CategoryAdapter.CategoryItemOnClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                getCategoryWares(position);
            }
        });

        mCategoryRV.setAdapter(mCategoryAdapter);
        mCategoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
}

    private void getCategoryWares(int position) {
        Log.d(TAG, "getCategoryWares: ");
        JSONObject json = new JSONObject();
        try {
            json.put("category",CategoryAdapter.categorys[position]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mOkhttpHelper.doPost(Constant.API.CATEGORY_WARES, json.toString(), new BaseCallback<String>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), R.string.netword_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSuccess(Response response, String s) {
                Log.d(TAG, "OnSuccess: " + s);
                if (s.contains("NULL")) {
                    mWares = new ArrayList<>();
                }else {
                    CategoryWares<List<Ware>> wares = JSONUtil
                            .fromJson(s, new TypeToken<CategoryWares<List<Ware>>>() {}.getType());
                    mWares = wares.getResult();
                    initWaresRecyclerView(mWares);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getActivity(), R.string.netword_fail + code, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWaresRecyclerView(List<Ware> wares){
        if (wares != null) {
            WaresAdapter adapter = new WaresAdapter(WaresAdapter.ITEMLAYOUTTYPE.HORIZONTAL,wares);
            adapter.setOnItemClickListener((v, position) -> {
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra("good", mWares.get(position));
                startActivity(intent);
            });
            mWaresRV.setAdapter(adapter);
            mWaresRV.setLayoutManager(new LinearLayoutManager(getContext()));
            mWaresRV.setItemAnimator(new DefaultItemAnimator());
        }
    }
}
