package com.mebee.mall.fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mebee.mall.R;

/**
 * Created by mebee on 2017/8/1.
 */
public class HomeFragment extends BaseFragment {


    private View mView;
    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initSliderView() {

    }

    @Override
    public void initSliderData() {

    }

    @Override
    public void initRecyclerView() {

    }

    @Override
    public void initRecyclerData() {

    }

}