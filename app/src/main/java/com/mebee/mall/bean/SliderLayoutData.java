package com.mebee.mall.bean;

/**
 * Created by mebee on 2017/8/3.
 */

public class SliderLayoutData {
    String url;
    String description;

    public SliderLayoutData(String description, String url) {
        this.description = description;
        this.url = url;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
