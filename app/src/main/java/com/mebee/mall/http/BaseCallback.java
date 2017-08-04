package com.mebee.mall.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/4.
 */

public abstract class BaseCallback<T> {
    public Type mType;

    public BaseCallback() {
        this.mType = getSuperClassTypeParameter(getClass());
    }

    static Type getSuperClassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    public abstract void onRequestBefore(Request request);

    public abstract void onFailure(Request request, IOException e);

    public abstract void OnSuccess(Response response, T t);

    public abstract void onError(Response response, int code, Exception e);
}
