package com.mebee.mall.bean;

/**
 * Created by mebee on 2017/8/30.
 */

public class OrderWare {

    /**
     * id : 100001
     * order_id : 100017
     * weight : 1
     */

    private String id;
    private String order_id;
    private int weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
