package com.mebee.mall.fragment.homefragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mebee.mall.R;
import com.mebee.mall.fragment.BaseFragment;

/**
 * Created by mebee on 2017/8/1.
 */
public class StoreFragment extends BaseFragment {

    private WebView webView;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = inflater;
        View view = layoutInflater.inflate(R.layout.fragment_store,container,false);

        return view;
    }

    @Override
    public void initView(View view) {
        webView = (WebView) view.findViewById(R.id.wv_store);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //WebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.loadUrl("http://qingmang.me/magazines/");
        webView.setWebViewClient(new WebViewClient(){  //设置不适用第三方浏览器打开网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    public void initData() {

    }

}
