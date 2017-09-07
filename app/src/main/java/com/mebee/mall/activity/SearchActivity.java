package com.mebee.mall.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mebee.mall.R;
import com.mebee.mall.adapter.WaresAdapter;
import com.mebee.mall.bean.ResMessage;
import com.mebee.mall.bean.Ware;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "SearchActivity";

    @BindView(R.id.toolbar_search)
    Toolbar toolbarSearch;
    @BindView(R.id.xrv_search_result)
    XRecyclerView xrvSearchResult;
    private SearchView searchView;

    private OkhttpHelper mOkHttpHelper;
    private List<Ware> mWares = new ArrayList<>();
    private WaresAdapter mWaresAdapter;
    private String mInput = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarSearch);// 此必须放在 setNavigationOnClickListener 前
        toolbarSearch.setNavigationOnClickListener(v -> finish());
        mOkHttpHelper = OkhttpHelper.getInstance();
        initRecyclerView();
        setListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);       // 加载searchview
        searchView.setOnQueryTextListener(this);                                // 为搜索框设置监听事件
        searchView.setSubmitButtonEnabled(true);                                // 设置是否显示搜索按钮
        searchView.setQueryHint(getString(R.string.please_inout_ware_name));    // 设置提示信息
        //searchView.setIconifiedByDefault(true);                               // 设置搜索默认为图标
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        mInput = query;
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void search(String s) {
        JSONObject json = new JSONObject();
        try {
            json.put(getString(R.string.name), s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mOkHttpHelper.doPost(Constant.API.FIND_WARE_BY_NAME_API,
                json.toString(), new BaseCallback<String>() {
                    @Override
                    public void onRequestBefore(Request request) {

                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Toast.makeText(SearchActivity.this, R.string.netword_fail, Toast.LENGTH_SHORT).show();
                        refreshFinsh();
                    }

                    @Override
                    public void OnSuccess(Response response, String s) {
                        Log.d(TAG, "OnSuccess: " + s);
                        if (s.contains(getString(R.string.NULL))) {
                            Toast.makeText(SearchActivity.this, "没有相应商品", Toast.LENGTH_SHORT).show();
                            refreshFinsh();
                        } else {
                            Type type = new TypeToken<ResMessage<Ware>>(){}.getType();
                            ResMessage<Ware> resMessage = JSONUtil.fromJson(s, type);
                            mWares = new ArrayList<>();
                            mWares.add(resMessage.getResult());
                            refreshFinsh();
                        }
                    }


                    @Override
                    public void onError(Response response, int code, Exception e) {
                        //Toast.makeText(SearchActivity.this, R.string.netword_fail+code, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }

    private void setListener(){
        xrvSearchResult.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                search(mInput);
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    private void initRecyclerView() {

        mWaresAdapter = new WaresAdapter(WaresAdapter.ITEMLAYOUTTYPE.HORIZONTAL,new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrvSearchResult.setLayoutManager(layoutManager);
        xrvSearchResult.setAdapter(mWaresAdapter);
        xrvSearchResult.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xrvSearchResult.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);

    }


    /**
     * 下拉刷新数据
     */
    private void refreshFinsh(){
        mWaresAdapter.refreshData(mWares);
        xrvSearchResult.refreshComplete();
        xrvSearchResult.scrollToPosition(0);
    }

    /**
     * 下拉加载数据
     */
    private void loadMoreFinsh(){
        mWaresAdapter.loadMoreData(mWares);
        xrvSearchResult.loadMoreComplete();
    }
}
