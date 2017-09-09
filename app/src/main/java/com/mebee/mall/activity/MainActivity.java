package com.mebee.mall.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
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

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {
    private static final String TAG = "MainActivity";

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private ViewPager mViewPager;
    private List<Tab> mTabs = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

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
        initView();
        initTab();
        initPager();
        ifWareDetailCall();
    }

    private void initView(){
        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) this.findViewById(R.id.main_tabhost);
        mViewPager = (ViewPager) this.findViewById(R.id.main_viewpager);
    }

    private void ifWareDetailCall(){
        if (getIntent().getIntExtra("waredetail",0) == 1) {
            mTabHost.setCurrentTab(2);
            mViewPager.setCurrentItem(2);
        }
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
        mTabHost.setup(this,getSupportFragmentManager(),R.id.main_tab_cotent);

        // 根据 Tab 信息 创建  Tab Fragment
        for (int i = 0; i < mTabs.size(); i++) {
            Tab mTab = mTabs.get(i);
            TabHost.TabSpec tabspec = mTabHost.newTabSpec(String.valueOf(i));
            tabspec.setIndicator( buildIndicator(mTab));
            mTabHost.addTab(tabspec,mTab.getFragment(),null);
        }
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setOnTabChangedListener(this);
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


    private void initPager(){
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new CategoryFragment());
        mFragmentList.add(new CartFragment());
        mFragmentList.add(new MineFragment());

        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),mFragmentList));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);
        widget.setDescendantFocusability(oldFocusability);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        mTabHost.setCurrentTab(Integer.parseInt(tabId));
        mViewPager.setCurrentItem(Integer.parseInt(tabId));
    }


    class PagerAdapter extends FragmentStatePagerAdapter{

        private List<Fragment> fragments;

        public PagerAdapter(FragmentManager fm, List<Fragment> fl) {
            super(fm);
            this.fragments = fl;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
