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
import com.mebee.mall.activity.MineActivity;
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
     * @return view ���ص�ǰFragment �� View
     */
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mOkhttpHelper = OkhttpHelper.getInstance();
        mUserProvider = UserProvider.getInstance(getContext());
        return view;
    }

    /**
     * ��ʼ�� ��ǰFragment  ���� View
     * @param view Fragment �� View
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
     * ��ʼ���û�����
     */
    @Override
    public void initData() {
        // ?????????
        mUser = mUserProvider.getmUser();
        if (mUser != null) {        // �û���Ϣ��Ϊ null������ʾ�û���Ϣ
            Log.d(TAG, "initUserInfo: " + mUserProvider.getmUser().getHead_path());
            mHead.setImageURI(mUserProvider.getmUser().getHead_path().replaceAll("\\\\","\\/"));
            mName.setText(mUserProvider.getmUser().getUser_name());
            getAllOrders();
        } else {                    // �û���ϢΪ null�������û���¼
            Toast.makeText(getContext(), R.string.not_login, Toast.LENGTH_SHORT).show();
            initExpanableListView(null);
        }
    }


    /**
     * set
     * ΪView ���õ������
     */
    private void setOnlicekListener() {
        mHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (mUser == null)
                    intent = new Intent(getActivity(), LoginActivity.class);
                else
                    intent = new Intent(getActivity(), MineActivity.class);
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
     * ����ͷ��
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {     // ��data��Ϊ nullʱ�����ݷ��ص� URI �ϴ�ͷ��ͼƬ
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
     * ��ʼ�� ExpanableListView(���۵���˫��LisView)
     * @param orders ������Ϣ
     */
    private void initExpanableListView(List<ResOrderInfo> orders){

        mOrders = new LinkedList<>();
        // ���ݶ���״̬Ϊ�������࣬�ֱ�Ž���ͬ List
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

        OrdersAdapter adapter = new OrdersAdapter(mOrders,getActivity());
        mListView.setAdapter(adapter);
    }

    /**
     * ��ȡ���ж�����Ϣ
     */
    private void getAllOrders() {

        mOkhttpHelper.setmSessionid(mUserProvider.getmCookie());
        mOkhttpHelper.doPost(Constant.API.GET_ALL_ORDERS_API, "", new BaseCallback<String>() {
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
     * ���� ״̬��ȡ������Ϣ
     * @param state ״̬
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
