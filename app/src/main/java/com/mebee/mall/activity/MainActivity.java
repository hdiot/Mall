package com.mebee.mall.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mebee.mall.R;
import com.mebee.mall.bean.Tab;
import com.mebee.mall.fragment.CartFragment;
import com.mebee.mall.fragment.CategoryFragment;
import com.mebee.mall.fragment.HomeFragment;
import com.mebee.mall.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private List<Tab> mTabs = new ArrayList<Tab>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                // other setters
                .setRequestListeners(requestListeners)
                .build();
        Fresco.initialize(this, config);
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);*/
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        initTab();
    }

    /**
     * 初始化FragmentTabHost
     */
    private void initTab() {

        Tab home = new Tab(R.string.home,R.drawable.selector_icon_home,HomeFragment.class);
        Tab cart = new Tab(R.string.cart,R.drawable.selector_icon_cart,CartFragment.class);
        Tab category = new Tab(R.string.category,R.drawable.selector_icon_category,CategoryFragment.class);
        Tab mine = new Tab(R.string.mine,R.drawable.selector_icon_mine,MineFragment.class);


        mTabs.add(home);
        mTabs.add(category);
        mTabs.add(cart);
        mTabs.add(mine);

        // 实例化 FragmentTabHost
        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) this.findViewById(R.id.main_tabhost);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.main_tab_cotent);

        // 根据 Tab 信息 创建  Tab Fragment
        for (Tab mTab : mTabs) {

            Log.d("ID", "initTab: "+mTab.getTitle()+"--"+mTab.getIcon());

            TabHost.TabSpec tabspec = mTabHost.newTabSpec(getString(mTab.getTitle()));

            tabspec.setIndicator( buildIndicator(mTab));

            mTabHost.addTab(tabspec,mTab.getFragment(),null);
        }
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    /**
     * 为 Tab 创建指示器
     * @param tab
     * @return
     */
    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.main_tab_indicator,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_main_tab);
        TextView textView = (TextView) view.findViewById(R.id.txt_main_indicator);
        imageView.setBackgroundResource(tab.getIcon());
        textView.setText(tab.getTitle());
        return view;

    }
}
