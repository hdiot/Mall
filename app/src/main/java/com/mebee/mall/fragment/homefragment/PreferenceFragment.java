package com.mebee.mall.fragment.homefragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mebee.mall.R;
import com.mebee.mall.adapter.WaresAdapter;
import com.mebee.mall.bean.Ware;
import com.mebee.mall.fragment.BaseFragment;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/1.
 */
public class PreferenceFragment extends BaseFragment {

    private XRecyclerView xRecyclerView;
    private ProgressBar progressBar;
    private OkhttpHelper mOkhttpHelper;
    private WaresAdapter mAdapter;
    private List<Ware> mWares;
    private CallState mCallState = CallState.LOAD;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = inflater;
        View view = layoutInflater.inflate(R.layout.fragment_preference,container,false);
        mOkhttpHelper = OkhttpHelper.getInstance();
        mAdapter = new WaresAdapter(WaresAdapter.ITEMLAYOUTTYPE.HORIZONTAL, null);
        return view;
    }

    /**
     * 初始化Fragment的子View
     * @param view
     */
    @Override
    public void initView(View view) {
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.xrv_preference);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_preference);
        progressBar.setVisibility(View.GONE);
    }


    /**
     * 请求数据
     */
    private void loadWaresData(){
        mOkhttpHelper.doPost(Constant.API.ALL_WARES, "", new BaseCallback<List<Ware>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                xRecyclerView.loadMoreComplete();
                xRecyclerView.refreshComplete();
                Toast.makeText(getActivity(), R.string.netword_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSuccess(Response response, List<Ware> wares) {
                mWares = wares;
                switch (mCallState) {
                    case LOAD:
                        loadFinish();
                        break;
                    case REFRESH:
                        refreshFinsh();
                        break;
                    case LOADMORE:
                        loadMoreFinsh();
                        break;
                }
                mCallState = CallState.LOAD;
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getActivity(), getString(R.string.netword_fail)
                        + getString(R.string.error_code)
                        + code, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 数据加载状态
     */
    enum CallState{
        LOAD,
        REFRESH,
        LOADMORE
    }

    /**
     * 初始化数据与 View
     */
    private void loadFinish(){
        mAdapter = new WaresAdapter(WaresAdapter.ITEMLAYOUTTYPE.HORIZONTAL,mWares);
        mAdapter.setOnItemClickListener(new WaresAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setAdapter(mAdapter);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mCallState = CallState.REFRESH;
                loadWaresData();
            }

            @Override
            public void onLoadMore() {
                mCallState = CallState.LOADMORE;
                loadWaresData();
            }
        });
    }

    /**
     * 下拉刷新数据
     */
    private void refreshFinsh(){
        mAdapter.refreshData(mWares);
        xRecyclerView.refreshComplete();
        xRecyclerView.scrollToPosition(0);
    }

    /**
     * 下拉加载数据
     */
    private void loadMoreFinsh(){
        mAdapter.loadMoreData(mWares);
        xRecyclerView.loadMoreComplete();
    }

    @Override
    public void initData() {
        loadWaresData();
    }
}
