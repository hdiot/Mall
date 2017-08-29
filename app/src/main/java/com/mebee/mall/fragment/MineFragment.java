package com.mebee.mall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.mebee.mall.R;
import com.mebee.mall.activity.LoginActivity;
import com.mebee.mall.adapter.OrdersAdapter;
import com.mebee.mall.bean.ResMessage;
import com.mebee.mall.bean.ResOrderInfo;
import com.mebee.mall.bean.User;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.JSONUtil;
import com.mebee.mall.utils.UserProvider;
import com.mebee.mall.widget.MyToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/1.
 */
public class MineFragment extends BaseFragment {
    private static final String TAG = "MineFragment";
    private static final CharSequence SUCESS = "Sucess";
    private static final CharSequence NOTLOGIN = "NotLogin";

    private MyToolbar mToolbar;
    private SimpleDraweeView mHead;
    private ExpandableListView mListView;
    private TextView mName;
    private OkhttpHelper mOkhttpHelper;
    private UserProvider mUserProvider;
    private User mUser;
    private List<ResOrderInfo> mResOrders;
    private List<List<ResOrderInfo>> mOrders;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view 返回当前Fragment 的 View
     */
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mOkhttpHelper = OkhttpHelper.getInstance();
        mUserProvider = UserProvider.getInstance(getContext());
        return view;
    }

    /** 初始化 当前Fragment  的子 View
     * @param view Fragment 的 View
     */
    @Override
    public void initView(View view) {
        mToolbar = (MyToolbar) view.findViewById(R.id.toolbar_mine);
        mHead = (SimpleDraweeView) view.findViewById(R.id.sdv_head_mine);
        mName = (TextView) view.findViewById(R.id.txt_name_mine);
        mListView = (ExpandableListView) view.findViewById(R.id.ex_listview_mine);
        setOnlicekListener();
    }

    /**
     * 初始化用户数据
     */
    @Override
    public void initData() {
        // 获取用户信息
        mUser = mUserProvider.getmUser();
        if (mUser != null) {        // 用户信息不为 null，则显示用户信息
            Log.d(TAG, "initUserInfo: " + mUserProvider.getmUser().getHead_path());
            mHead.setImageURI(mUserProvider.getmUser().getHead_path().replaceAll("\\\\","\\/"));
            mName.setText(mUserProvider.getmUser().getUser_name());
            getAllOrders();
        } else {                    // 用户信息为 null，提醒用户登录
            Toast.makeText(getContext(), R.string.not_login, Toast.LENGTH_SHORT).show();
            initExpanableListView(null);
        }
    }


    /**set
     * 为View 设置点击监听
     */
    private void setOnlicekListener() {
        mHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (mUser != null)
                    intent = new Intent(getActivity(), LoginActivity.class);
                else
                    intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        mHead.setOnLongClickListener(v -> {
            if (mUser != null) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/jpeg");
                startActivityForResult(intent, 1);
                return true;
            }
            return false;
        });
    }

    /**
     * 设置头像
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {     // 当data不为 null时，根据返回的 URI 上传头像图片
            Log.d(TAG, "onActivityResult: " + data.getData());
            mHead.setImageURI(data.getData());
            mOkhttpHelper.setmSessionid(mUserProvider.getmCookie());
            mOkhttpHelper.upLoadPicture(Constant.API.UPLOAD_PICTURE_API,
                    URI.create(data.getData().toString()),
                    new BaseCallback<String>() {
                        @Override
                        public void onRequestBefore(Request request) {

                        }

                        @Override
                        public void onFailure(Request request, IOException e) {
                            Toast.makeText(getActivity(), R.string.netword_fail, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnSuccess(Response response, String s) {
                            if (s.contains(SUCESS)) {
                                Toast.makeText(getActivity(), R.string.upload_succeed, Toast.LENGTH_SHORT).show();
                            } else if (s.contains(NOTLOGIN)) {
                                Toast.makeText(getActivity(), R.string.please_to_login, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Response response, int code, Exception e) {
                            Toast.makeText(getActivity(), R.string.netword_fail + code, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * 初始化 ExpanableListView(可折叠的双层LisView)
     * @param orders 订单信息
     */
    private void initExpanableListView(List<ResOrderInfo> orders){

        mOrders = new LinkedList<>();
        // 根据订单状态为订单分类，分别放进不同 List
        List<ResOrderInfo> list0 = new LinkedList<>();
        List<ResOrderInfo> list1 = new LinkedList<>();
        List<ResOrderInfo> list2 = new LinkedList<>();
        List<ResOrderInfo> list3 = new LinkedList<>();
        if (orders != null) {
            for (ResOrderInfo order : orders) {
                switch (order.getOrder_state()) {
                    case 0:
                        list0.add(order);
                        break;
                    case 1:
                        list1.add(order);
                        break;
                    case 2:
                        list2.add(order);
                        break;
                    case 3:
                        list3.add(order);
                        break;
                }
            }
        }
        mOrders.add(orders);
        mOrders.add(list0);
        mOrders.add(list1);
        mOrders.add(list2);
        mOrders.add(list3);

        OrdersAdapter adapter = new OrdersAdapter(mOrders,getContext());
        mListView.setAdapter(adapter);
    }

    /**
     * 获取所有订单信息
     */
    private void getAllOrders() {

        mOkhttpHelper.setmSessionid(mUserProvider.getmCookie());
        mOkhttpHelper.doPost(Constant.API.GET_ALL_ORDERS, "", new BaseCallback<String>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                initExpanableListView(null);
            }

            @Override
            public void OnSuccess(Response response, String s) {
                if (s.contains(NOTLOGIN)){
                    Toast.makeText(getContext(), R.string.not_login, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "OnSuccess: " + s);
                    Type type = new TypeToken<ResMessage<List<ResOrderInfo>>>(){}.getType();
                    ResMessage<List<ResOrderInfo>> listResMessage = JSONUtil.fromJson(s, type);
                    mResOrders = listResMessage.getResult();
                    initExpanableListView(mResOrders);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    /**
     * 根据 状态获取订单信息
     * @param state 状态
     */
    private void getOrdersByState(int state) {

        try {
            String param = new JSONObject().put("orderstate",state).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mOkhttpHelper.setmSessionid(mUserProvider.getmCookie());
        mOkhttpHelper.doPost(Constant.API.ALL_WARES, "", new BaseCallback<ResMessage<List<ResOrderInfo>>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void OnSuccess(Response response, ResMessage<List<ResOrderInfo>> listResMessage) {

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
}
