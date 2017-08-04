package com.mebee.mall.fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.mebee.mall.R;
import com.mebee.mall.bean.Tab;
import com.mebee.mall.fragment.homefragment.MarketFragment;
import com.mebee.mall.fragment.homefragment.PreferenceFragment;
import com.mebee.mall.fragment.homefragment.RecommendFragment;
import com.mebee.mall.fragment.homefragment.StoreFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mebee on 2017/8/1.
 */

public class HomeFragment extends BaseFragment {


    private View mView;
    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void initView(View view) {
        mTabHost = (FragmentTabHost) view.findViewById(R.id.home_tabhost);

    }

    @Override
    public void initData() {

        if (mTabHost != null){
            Tab home = new Tab(R.string.recommend,0,RecommendFragment.class);
            Tab cart = new Tab(R.string.preference,0,PreferenceFragment.class);
            Tab category = new Tab(R.string.market,0,MarketFragment.class);
            Tab mine = new Tab(R.string.store,0,StoreFragment.class);

            List<Tab> mTabs = new ArrayList<>();
            mTabs.add(home);
            mTabs.add(cart);
            mTabs.add(category);
            mTabs.add(mine);

            mTabHost.setup(getActivity(), getChildFragmentManager(),
                    android.R.id.tabcontent);

            for (Tab tab : mTabs) {
                Log.d("initData: ", "----initData:---- ");
                TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
                tabSpec.setIndicator(buildIndicator(getString(tab.getTitle())));
                mTabHost.addTab(tabSpec,tab.getFragment(),null);
            }
        }
    }

    private View buildIndicator(String title){
        View view = mInflater.inflate(R.layout.home_tab_indicator,null);
        TextView textView = (TextView) view.findViewById(R.id.txt_home_tab_indacator);
        textView.setText(title);
        textView.setBackgroundResource(R.drawable.selector_bg_tab);
        return view;
    }



}
