package com.mebee.mall.bean;

/**
 * Created by mebee on 2017/8/30.
 */

public class OrderDetailWare {

    String name;
    String pic_path;
    double price;
    double count;
    double summation;

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSummation() {
        return summation;
    }

    public void setSummation(double summation) {
        this.summation = summation;
    }

}
