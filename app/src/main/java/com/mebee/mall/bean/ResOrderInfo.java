package com.mebee.mall.bean;

/**
 * Created by mebee on 2017/8/24.
 */

public class ResOrderInfo {

    /**
     * id : 3
     * order_state : 0
     * account_id : 2
     * telephone : 15989874568
     * address_id : 1003
     */

    private String id;
    private int order_state;
    private String account_id;
    private String telephone;
    private String address_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
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

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }
}
