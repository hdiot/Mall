package com.mebee.mall.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/4.
 */

public class OkhttpHelper {

    private static final String TAG = "OkhttpHelper";

    private OkHttpClient mClient;
    private Gson mGson;
    private Handler mHandler;

    private static String mSessionid = new String();

    public static String getmSessionid() {
        return mSessionid;
    }

    public static void setmSessionid(String mSessionid) {
        OkhttpHelper.mSessionid = mSessionid;
    }

    private OkhttpHelper() {
        mClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkhttpHelper getInstance() {
        return new OkhttpHelper();
    }

    public void doGet(String url, BaseCallback callBack) {
        Request request = buildRequest(url, null, HttpMethodType.GET);
        doRequest(request, callBack);
    }

    public void upLoadPicture(String url, URI uri, BaseCallback callback) {
        Request request = buildUploadRequest(url,uri);
        doRequest(request,callback);
    }


    public void doPost(String url, String json, BaseCallback callBack) {
        Request request = buildRequest(url, json, HttpMethodType.POST);
        doRequest(request, callBack);
    }

    public void doRequest(Request request, BaseCallback callback) {
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                e.printStackTrace();
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: ");
                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if (callback.mType == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object object = mGson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback, response, object);
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                            callbackError(callback, response, response.code(), e);
                        }
                    }
                } else {
                    Log.d(TAG, "onResponse: " + response.body().string() + response.code());
                }
            }
        });
    }


    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object) {
        if (callback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.OnSuccess(response, object);
                }
            });
        }
    }

    private void callbackError(final BaseCallback callback, final Response response, final int errCode,
                               final Exception e) {
        if (callback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onError(response, errCode, e);
                }
            });
        }
    }

    private void callbackBeforeRequest(final BaseCallback callback, final Request request) {
        if (callback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onRequestBefore(request);
                }
            });
        }
    }

    private void callbackFailure(final BaseCallback callback, final Request request,
                                 final Exception e) {
        if (callback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(request, (IOException) e);
                }
            });
        }
    }


    private Request buildRequest(String url, String json, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Log.d(TAG, "buildRequest: " + mSessionid);

        if (methodType == HttpMethodType.GET) {
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            builder.addHeader("Cookie", mSessionid);
            builder.post(body);
        }
        return builder.build();
    }

    public Request buildUploadRequest(String url, URI uri) {

        File image = new File(uri);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Log.d(TAG, "buildRequest: " + mSessionid);

        MediaType IMG = MediaType.parse("uploadfile");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uploadfile", image.getName(), RequestBody.create(IMG,image))
                .build();
        builder.addHeader("Cookie", mSessionid);
        builder.post(body);


        return builder.build();


    }

    enum HttpMethodType {
        POST,
        GET
    }
}
