package com.mebee.mall.bean;

import java.util.List;

/**
 * Created by mebee on 2017/9/9.
 */


public class CityBean {
    /**
     * city : 贵阳市
     * regions : ["乌当区","云岩区","修文县","南明区","小河区","开阳县","息烽县","清镇市","白云区","花溪区"]
     */
    private String city;
    private List<String> regions;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }
}


