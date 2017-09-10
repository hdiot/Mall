package com.mebee.mall.bean;

import java.util.List;

/**
 * Created by mebee on 2017/9/9.
 */

public class ProvinceBean {

    /**
     * province : 福建省
     * citys : ["福州市","厦门市","莆田市","三明市","泉州市","漳州市","南平市","龙岩市","宁德市"]
     */

    private String province;
    private List<String> citys;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<String> getCitys() {
        return citys;
    }

    public void setCitys(List<String> citys) {
        this.citys = citys;
    }

}
