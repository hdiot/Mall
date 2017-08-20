package com.mebee.mall.bean;

import java.io.Serializable;

/**
 * Created by mebee on 2017/8/18.
 */

public class ShoppingCart extends Wares implements Serializable {

    private int count;
    private boolean isCheck = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

}
