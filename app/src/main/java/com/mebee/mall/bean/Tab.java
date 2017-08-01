package com.mebee.mall.bean;

/**
 * Created by mebee on 2017/8/2.
 */

public class Tab {
    private int title;      // 标题id
    private int icon;       // 图片id
    private Class fragment; // 对应的Fragment类

    public Tab( int title, int icon, Class fragment) {
        this.fragment = fragment;
        this.icon = icon;
        this.title = title;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }


}
