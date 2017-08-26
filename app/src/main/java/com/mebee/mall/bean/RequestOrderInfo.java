package com.mebee.mall.bean;

import java.util.List;

/**
 * Created by mebee on 2017/8/24.
 */

public class RequestOrderInfo {

    /**
     * telephone : 13560522684
     * addressname : 广东海洋大学
     * 100001 : 12
     * 500003 : 11
     */

    private String telephone;
    private String addressname;
    private List<OrderWareInfo> wareList;

    public List<OrderWareInfo> getWareList() {
        return wareList;
    }

    public void setWareList(List<OrderWareInfo> wareList) {
        this.wareList = wareList;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddressname() {
        return addressname;
    }

    public void setAddressname(String addressname) {
        this.addressname = addressname;
    }

}
