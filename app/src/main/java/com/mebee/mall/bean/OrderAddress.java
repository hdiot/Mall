package com.mebee.mall.bean;

/**
 * Created by mebee on 2017/8/30.
 */

public class OrderAddress {

    /**
     * address_id : 56
     * account_id : 56
     * telephone : 2388111
     * address_name : 广东海洋大学
     */

    private String address_id;
    private String account_id;
    private String telephone;
    private String address_name;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }
}
