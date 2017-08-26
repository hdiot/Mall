package com.mebee.mall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.mebee.mall.bean.ResponseMessage;
import com.mebee.mall.bean.ResponseOrderInfo;
import com.mebee.mall.bean.User;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.JSONUtil;
import com.mebee.mall.utils.UserProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    private Toolbar mToolbar;
    private SimpleDraweeView mHead;
    private ExpandableListView mListView;
    private TextView mName;
    private OkhttpHelper mOkhttpHelper;
    private UserProvider mUserProvider;
    private User mUser;
    private List<ResponseOrderInfo> mResponseOrders;
    private List<List<ResponseOrderInfo>> mOrders;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mOkhttpHelper = OkhttpHelper.getInstance();
        mUserProvider = UserProvider.getInstance(getContext());
        return view;
    }

    @Override
    public void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_mine);
        mHead = (SimpleDraweeView) view.findViewById(R.id.sdv_head_mine);
        mName = (TextView) view.findViewById(R.id.txt_name_mine);
        mListView = (ExpandableListView) view.findViewById(R.id.ex_listview_mine);
        setOnlicekListener();
        initUserInfo();
    }


    private void setOnlicekListener() {
        mHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
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

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d(TAG, "onChildClick: " + groupPosition +"---" + childPosition +"---" + id);
                return true;
            }
        });

        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: " + groupPosition +"---" + id);
                return false;
            }
        });
    }


    private void initUserInfo() {

        mUser = mUserProvider.getmUser();
        if (mUser != null) {
            Log.d(TAG, "initUserInfo: " + mUserProvider.getmUser().getHead_path());
            mHead.setImageURI(mUserProvider.getmUser().getHead_path());
            mName.setText(mUserProvider.getmUser().getUser_name());
            getAllOrders();
        } else {
            Toast.makeText(getContext(), "ÉÐÎ´µÇÂ¼", Toast.LENGTH_SHORT).show();
            initExpanableListView(null);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
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
                            Toast.makeText(getActivity(), "ÍøÂç³ö´í", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnSuccess(Response response, String s) {
                            if (s.contains("Sucess")) {
                                Toast.makeText(getActivity(), "ÉÏ´«³É¹¦", Toast.LENGTH_SHORT).show();
                            } else if (s.contains("NotLogin")) {
                                Toast.makeText(getActivity(), "ÇëÏÈµÇÂ¼", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Response response, int code, Exception e) {
                            Toast.makeText(getActivity(), "ÍøÂç³ö´í" + code, Toast.LENGTH_SHORT).show();
                        }
                    });
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void initData() {
    }

    private void initExpanableListView(List<ResponseOrderInfo> orders){

        mOrders = new LinkedList<>();

        List<ResponseOrderInfo> list0 = new LinkedList<>();
        List<ResponseOrderInfo> list1 = new LinkedList<>();
        List<ResponseOrderInfo> list2 = new LinkedList<>();
        List<ResponseOrderInfo> list3 = new LinkedList<>();
        if (orders != null) {
            for (ResponseOrderInfo order : orders) {
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

    private void getAllOrders() {

        mOkhttpHelper.setmSessionid(mUserProvider.getmCookie());
        mOkhttpHelper.doPost(Constant.API.GET_ALL_ORDERS, "", new BaseCallback<String>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void OnSuccess(Response response, String s) {
                if (s.contains("NotLogin")){
                    Toast.makeText(getContext(), "Î´µÇÂ¼", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "OnSuccess: " + s);
                    ResponseMessage<List<ResponseOrderInfo>> listResponseMessage = JSONUtil.fromJson(s,
                            new TypeToken<ResponseMessage<List<ResponseOrderInfo>>>(){}.getType());
                    mResponseOrders = listResponseMessage.getResult();
                    initExpanableListView(mResponseOrders);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void getOrdersByState(int state) {

        try {
            String param = new JSONObject().put("orderstate",state).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mOkhttpHelper.setmSessionid(mUserProvider.getmCookie());
        mOkhttpHelper.doPost(Constant.API.ALL_WARES, "", new BaseCallback<ResponseMessage<List<ResponseOrderInfo>>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void OnSuccess(Response response, ResponseMessage<List<ResponseOrderInfo>> listResponseMessage) {

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
}
