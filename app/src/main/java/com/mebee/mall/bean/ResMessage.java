package com.mebee.mall.bean;

/**
 * Created by mebee on 2017/8/19.
 */

public class ResMessage<T>{

    /**
     * Result : NotExistError
     */

    private T Result;

    public T getResult() {
        return Result;
    }

    public void setResult(T Result) {
        this.Result = Result;
    }
}
