package com.mebee.mall.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mebee.mall.R;
import com.mebee.mall.bean.CityBean;
import com.mebee.mall.bean.ProvinceBean;
import com.mebee.mall.utils.JSONUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mebee on 2017/9/9.
 */

public class AddressPicker extends LinearLayout {

    private static final String TAG = "AddressPicker";
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;
    private List<String> mAddressData;
    private List<ProvinceBean> mProvinceBeanList;
    private List<CityBean> mCityBeanList;
    private List<String> mProvinces = new ArrayList<>();
    private AddressPickerAdapter mPickAdapter;
    private AddressPickFinish mAddressPickFinish;
    private String mProvince;
    private String mCity;
    private String mRegion;
    private String [] mAddressDetail = {"","",""};

    public AddressPicker(Context context) {
        this(context, null);
    }

    public AddressPicker(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AddressPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);
        this.mTabLayout = new TabLayout(context);
        this.mTabLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        this.addView(this.mTabLayout);

        this.mRecyclerView = new RecyclerView(context);
        this.mRecyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.addView(this.mRecyclerView);

        initDatas(context);
        getProvinces();
        initRecycler();
        initTab();
        setListen();
    }

    public interface AddressPickFinish{
        void response(String [] s);
    }

    public void setAddressPickFinish(AddressPickFinish addressPickFinish){
        mAddressPickFinish = addressPickFinish;
    }

    private void initDatas(Context context) {
        Type pType = new TypeToken<List<ProvinceBean>>() {}.getType();
        mProvinceBeanList = JSONUtil.fromJson(context.getString(R.string.provinces), pType);
        Type cType = new TypeToken<List<CityBean>>(){}.getType();
        mCityBeanList = JSONUtil.fromJson(context.getString(R.string.citys),cType);
    }

    public void resetPicker(){
        mTabLayout.removeAllTabs();
        initTab();
        initRecycler();
    }

    private void initRecycler(){
        mAddressData = mProvinces;
        mPickAdapter = new AddressPickerAdapter();
        mRecyclerView.setAdapter(mPickAdapter);
    }

    private void getProvinces(){
        for (ProvinceBean provinceBean : mProvinceBeanList) {
            mProvinces.add(provinceBean.getProvince());
        }
    }

    private List<String> getCitys(String province){
        for (ProvinceBean provinceBean : mProvinceBeanList) {
            if (provinceBean.getProvince() == province) {
                return provinceBean.getCitys();
            }
        }
        return null;
    }

    private List<String> getRegions(String city){
        for (CityBean cityBean : mCityBeanList) {
            if (cityBean.getCity().contains(city)) {
                return cityBean.getRegions();
            }
        }
        return null;
    }

    private void initTab(){
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("请选择");
        mTabLayout.addTab(tab,true);
    }

    private void setListen(){
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                int count = mTabLayout.getTabCount();
                for (int i = count-1; i>position; i--){
                    mTabLayout.removeTabAt(i);
                    mAddressDetail[i] ="";
                }
                switch (position){
                    case 0:
                        mAddressData = mProvinces;
                        mPickAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        mAddressData = getCitys(mAddressDetail[0]);
                        mPickAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    class AddressPickerAdapter extends RecyclerView.Adapter<AddressPickerAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view  = inflater.inflate(R.layout.address_picker_item_view,parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(mAddressData.get(position));
            holder.itemView.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        int i = mTabLayout.getSelectedTabPosition();
                        mAddressDetail[i] = mAddressData.get(position);
                        mTabLayout.getTabAt(i).setText(mAddressDetail[i]);
                        switch (i){
                            case 0:
                                mAddressData = getCitys(mAddressDetail[i]);
                                break;
                            case 1:
                                mAddressData = getRegions(mAddressDetail[i]);
                                break;
                            case 2:
                                if (mAddressPickFinish != null)
                                    mAddressPickFinish.response(mAddressDetail);
                                break;
                        }
                        if (mAddressData != null) {
                            if (mAddressData.size()!=0 && i!=2) {
                                TabLayout.Tab tab1 = mTabLayout.newTab().setText("请选择");
                                mTabLayout.addTab(tab1,true);
                            }else {
                                if (mAddressPickFinish != null)
                                    mAddressPickFinish.response(mAddressDetail);
                            }
                            notifyDataSetChanged();
                        } else {
                            if (mAddressPickFinish != null)
                                mAddressPickFinish.response(mAddressDetail);
                        }
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return AddressPicker.this.mAddressData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            TextView textView;
            View item;
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_addr_item);
                item = itemView;
            }
        }
    }

}
