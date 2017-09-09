package com.mebee.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mebee.mall.R;
import com.mebee.mall.adapter.OderWaresAdapter;
import com.mebee.mall.bean.OrderAddress;
import com.mebee.mall.bean.OrderDetailWare;
import com.mebee.mall.bean.OrderWare;
import com.mebee.mall.bean.ResMessage;
import com.mebee.mall.bean.Ware;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private static final String TAG = "OrderDetailActivity";
    private static final String ORDERID = "orderid";
    private static final String ADDRESSID = "addressid";
    private static final String ORDERSTATE = "orderstate";

    @BindView(R.id.toolbar_orderDetail)
    Toolbar toolbarOrderDetail;
    @BindView(R.id.txt_tel_orderDetail)
    TextView txtTelOrderDetail;
    @BindView(R.id.txt_address_orderDetail)
    TextView txtAddressOrderDetail;
    @BindView(R.id.txt_orderId_orderDetail)
    TextView txtOrderIdOrderDetail;
    @BindView(R.id.txt_receiverName_orderDetail)
    TextView txtReceiverNameOrderDetail;
    @BindView(R.id.txt_warescount_orderDetail)
    TextView txtWarescountOrderDetail;
    @BindView(R.id.xrv_wares_orderDetail)
    XRecyclerView xrvWaresOrderDetail;
    @BindView(R.id.txt_totalpri_orderDetail)
    TextView txtTotalpriOrderDetail;
    @BindView(R.id.txt_postFree_orderDetail)
    TextView txtPostFreeOrderDetail;
    @BindView(R.id.txt_payment_orderdetail)
    TextView txtPaymentOrderdetail;
    @BindView(R.id.btn_cancel_orderDetail)
    Button btnCancelOrderDetail;
    @BindView(R.id.btn_next_orderDetail)
    Button btnNextOrderDetail;

    private String[] mStates;
    private String mOrderId;
    private String mAddressId;
    private int mOrderState;
    private OkhttpHelper mOkhttpHelper;
    private List<OrderWare> mOrderWares;
    private Map<String, Ware> mWareMap = new HashMap<>();
    private Map<String, OrderWare> mOrderWareMap = new HashMap<>();
    private List<OrderDetailWare> mODWares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        mOkhttpHelper = OkhttpHelper.getInstance();
        mOrderId = getIntent().getStringExtra(ORDERID);
        mAddressId = getIntent().getStringExtra(ADDRESSID);
        mOrderState = getIntent().getIntExtra(ORDERSTATE, 0);

        mStates = new String[]{
                getString(R.string.pay_immediately),
                getString(R.string.call_deliver),
                getString(R.string.confirm_receipt),
                getString(R.string.comment_immediately)};

        setBtnState();
        setListener();
        Log.d(TAG, "onCreate: " + mOrderId + mAddressId);
        getAddress(mAddressId);
        getWares(mOrderId);
    }

    private void setBtnState() {

        btnNextOrderDetail.setText(mStates[mOrderState]);
        if (mOrderState == 3) {
            btnCancelOrderDetail.setEnabled(false);
        }
    }

    private void setListener() {
        toolbarOrderDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDetailActivity.this,MainActivity.class));
                finish();
            }
        });
        xrvWaresOrderDetail.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                xrvWaresOrderDetail.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                xrvWaresOrderDetail.loadMoreComplete();
            }
        });
        btnCancelOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder();
            }
        });
        btnNextOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderState == 3) {
                    Toast.makeText(OrderDetailActivity.this, "立即评论", Toast.LENGTH_SHORT).show();
                } else {
                    changeOrderState();
                }
            }
        });

        xrvWaresOrderDetail.setNestedScrollingEnabled(false);
    }


    private void getAddress(String addresid) {
        mOkhttpHelper.doPost(Constant.API.GET_ADDRESS_BY_ID_API,
                formParams("addressId", addresid),
                new BaseCallback<OrderAddress>() {
                    @Override
                    public void onRequestBefore(Request request) {

                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void OnSuccess(Response response, OrderAddress orderAddress) {
                        if (orderAddress != null) {
                            txtAddressOrderDetail.setText(orderAddress.getAddress_name());
                            txtOrderIdOrderDetail.setText(mOrderId);
                            txtTelOrderDetail.setText(orderAddress.getTelephone());
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });
    }

    private void getWares(String orderid) {
        mOkhttpHelper.doPost(Constant.API.GET_ALL_WARES_BY_ORDER_ID_API,
                formParams("orderid", orderid),
                new BaseCallback<String>() {
                    @Override
                    public void onRequestBefore(Request request) {

                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void OnSuccess(Response response, String s) {
                        Log.d(TAG, "OnSuccess: " + s);
                        if (s.contains("NotExitGoods")) {

                        } else {
                            Type type = new TypeToken<ResMessage<List<OrderWare>>>() {
                            }.getType();
                            ResMessage<List<OrderWare>> resMessage = JSONUtil.fromJson(s, type);
                            mOrderWares = resMessage.getResult();
                            for (OrderWare mOrderWare : mOrderWares) {
                                Log.d(TAG, "getWaresOnSuccess: " + mOrderWare.getId());
                                mOrderWareMap.put(mOrderWare.getId(), mOrderWare);
                                getWare(mOrderWare.getId());
                            }
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });
    }

    private void getWare(String id) {
        mOkhttpHelper.doPost(Constant.API.GET_WARE_BY_ID_API,
                formParams("id", id),
                new BaseCallback<Ware>() {
                    @Override
                    public void onRequestBefore(Request request) {

                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void OnSuccess(Response response, Ware ware) {
                        if (ware != null) {
                            Log.d(TAG, "OnSuccess: " + id);
                            mWareMap.put(id, ware);
                            initXRV();
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });
    }

    private void initXRV() {
        xrvWaresOrderDetail.setAdapter(new OderWaresAdapter(map2List()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrvWaresOrderDetail.setLayoutManager(layoutManager);
        xrvWaresOrderDetail.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xrvWaresOrderDetail.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        txtTotalpriOrderDetail.setText(getSummation() + getString(R.string.rmb));
    }

    private String formParams(String key, String value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    private List<OrderDetailWare> map2List() {
        mODWares = new ArrayList<>();
        for (Map.Entry<String, OrderWare> entry : mOrderWareMap.entrySet()) {
            Log.d(TAG, "map2List: " + entry.getKey());
            Ware ware = mWareMap.get(entry.getKey());
            if (ware != null) {
                OrderDetailWare orderDetailWare = new OrderDetailWare();
                orderDetailWare.setCount(entry.getValue().getWeight());
                orderDetailWare.setName(ware.getName());
                orderDetailWare.setPic_path(ware.getPicture_name_path());
                orderDetailWare.setPrice(ware.getPrice());
                DecimalFormat df = new DecimalFormat("#.00");
                double s = ware.getPrice() * entry.getValue().getWeight();
                orderDetailWare.setSummation(Double.valueOf(df.format(s)));
                mODWares.add(orderDetailWare);
            }
        }

        return mODWares;
    }

    private String getSummation() {
        double summation = 0.0;
        for (OrderDetailWare mODWare : mODWares) {
            summation += mODWare.getSummation();
        }

        DecimalFormat format = new DecimalFormat("#.00");
        return format.format(summation);
    }

    private void changeOrderState() {
        JSONObject json = new JSONObject();
        try {
            json.put("orderstate", mOrderState + 1 + "");
            json.put("orderid", mOrderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mOkhttpHelper.doPost(Constant.API.CHANGE_ORDER_STATE_API, json.toString(),
                new BaseCallback<ResMessage<String>>() {
                    @Override
                    public void onRequestBefore(Request request) {
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                    }

                    @Override
                    public void OnSuccess(Response response, ResMessage<String> stringResMessage) {
                        Log.d(TAG, "OnSuccess: " + stringResMessage.getResult());
                        if (stringResMessage.getResult().contains("Sucess")) {
                            mOrderState++;
                            setBtnState();
                        }
                    }


                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });
    }

    private void cancelOrder() {
        JSONObject json = new JSONObject();
        try {
            json.put("orderld", mOrderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mOkhttpHelper.doPost(Constant.API.DELETE_ORDER_BY_ID_API, json.toString(),
                new BaseCallback<ResMessage<String>>() {
                    @Override
                    public void onRequestBefore(Request request) {

                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void OnSuccess(Response response, ResMessage<String> stringResMessage) {
                        Log.d(TAG, "OnSuccess: " + stringResMessage.getResult());
                        if (stringResMessage.getResult().contains("Sucess")) {
                            btnCancelOrderDetail.setEnabled(false);
                            btnNextOrderDetail.setEnabled(false);
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this,MainActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
