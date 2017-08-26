package com.mebee.mall.bean;

import java.io.Serializable;

/**
 * Created by mebee on 2017/8/18.
 */

public class ShoppingCart extends Ware implements Serializable {

    private int count;
    private boolean isCheck = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

}
