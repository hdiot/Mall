package com.mebee.mall.bean;

import java.io.Serializable;

/**
 * Created by mebee on 2017/8/3.
 */
public class Ware implements Serializable{

    /**
     * id : 100001
     * name : 橙子
     * category : 柑橘类
     * price : 5.6
     * sales_volume : 800
     * picture_name_path : http://119.23.33.62:8080/Fruit_Store/GoodsImage/柑橘类image/柠檬-柑橘类.png
     * producing_area : 河南
     */

    private Long id;
    private String name;
    private String category;
    private double price;
    private int sales_volume;
    private String picture_name_path;
    private String producing_area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(int sales_volume) {
        this.sales_volume = sales_volume;
    }

    public String getPicture_name_path() {
        return picture_name_path;
    }

    public void setPicture_name_path(String picture_name_path) {
        this.picture_name_path = picture_name_path;
    }

    public String getProducing_area() {
        return producing_area;
    }

    public void setProducing_area(String producing_area) {
        this.producing_area = producing_area;
    }
}
