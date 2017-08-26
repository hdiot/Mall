package com.mebee.mall.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mebee.mall.R;
import com.mebee.mall.activity.OrderActivity;
import com.mebee.mall.adapter.AddressAdapter;
import com.mebee.mall.adapter.CartAdapter;
import com.mebee.mall.bean.Address;
import com.mebee.mall.bean.OrderWareInfo;
import com.mebee.mall.bean.RequestOrderInfo;
import com.mebee.mall.bean.ResponseMessage;
import com.mebee.mall.bean.ShoppingCart;
import com.mebee.mall.http.BaseCallback;
import com.mebee.mall.http.OkhttpHelper;
import com.mebee.mall.utils.AddressProvider;
import com.mebee.mall.utils.CartProvider;
import com.mebee.mall.utils.Constant;
import com.mebee.mall.utils.UserProvider;
import com.mebee.mall.widget.AddressPopWin;
import com.mebee.mall.widget.MyToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mebee on 2017/8/1.
 */
public class CartFragment extends BaseFragment implements CartAdapter.OnDataUpdateListener{




    private CartProvider mCartProvider;
    private CartAdapter mCartAdapter;
    private RecyclerView mRecyclerView;
    private MyToolbar mToolbar;
    private CheckBox mCheckBox;
    private Button mDelete;
    private Button mCharge;
    private TextView mTotalPrice;
    private LinearLayout mAddressLayout;
    private static final String TAG = "CartFragment";
    private View mView;

    private TextView TV_Name;
    private TextView TV_Tel;
    private TextView TV_Address;

    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private View mAlertView;
    private EditText ReceiverName;
    private EditText ReceiverTel;
    private EditText ReceiverAddress;
    private Button DialogCancle;
    private Button DialogSure;

    private AddressProvider mAddressProvider;
    private AddressPopWin mAddressPopWin;

    private OkhttpHelper mOkhttpHelper;
    private UserProvider mUserProvider;
    private Address mAddress;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_cart, null, false);
        ButterKnife.bind(mView);
        mCartProvider = CartProvider.getInstance(getContext());
        mAddressProvider = AddressProvider.getInstance(getContext());
        mUserProvider = UserProvider.getInstance(getContext());
        mOkhttpHelper = OkhttpHelper.getInstance();

        SharedPreferences preferences = getActivity().getSharedPreferences("MEBEE_MALL", Context.MODE_PRIVATE);

        Log.d(TAG, "createView: "+preferences.getString("cart_json","---"));

        return mView;
    }

    @Override
    public void initView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_cart);
        mCheckBox = (CheckBox) view.findViewById(R.id.cb_choose_all_cart);
        mDelete = (Button) view.findViewById(R.id.btn_delete_cart);
        mCharge = (Button) view.findViewById(R.id.btn_add_order_cart);
        mTotalPrice = (TextView) view.findViewById(R.id.txt_total_price_cart);
        mAddressLayout = (LinearLayout) view.findViewById(R.id.layout_address);

        TV_Name = (TextView) view.findViewById(R.id.txt_name_cart);
        TV_Tel = (TextView) view.findViewById(R.id.txt_tel_cart);
        TV_Address = (TextView) view.findViewById(R.id.txt_address_cart);
    }


    @Override
    public void initData() {

        setAddressData();
        setOnClickListener();
        initRecyclerView();
        createAddDialog();
    }

    private void setAddressData(){
        List<Address> addresses = mAddressProvider.getAll();
        if (addresses != null){
            for (Address address : addresses) {
                if (address.isChecked()){
                    mAddress = address;
                    break;
                }
            }
        }

        if (mAddress != null){
            TV_Name.setText(mAddress.getName());
            TV_Tel.setText(mAddress.getTel());
            TV_Address.setText(mAddress.getAddress());
        } else {
            TV_Name.setText("");
            TV_Tel.setText("");
            TV_Address.setText("未选择收件人信息");
        }
    }

    private void setOnClickListener(){

        mAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressWindow();
            }
        });

        mCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartAdapter.deleteCart();
            }
        });
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBox.isChecked()) {
                    mCartAdapter.changeItemCheckStatus(true);
                } else {
                    mCartAdapter.changeItemCheckStatus(false);
                }
            }
        });
    }

    private void showAddressWindow(){
        mAddressPopWin = new AddressPopWin(getContext());

        mAddressPopWin.setOnClickListener(new AddressPopWin.OnClickListener() {
            @Override
            public void onAddClick() {

                mDialog.show();
            }
        });

        mAddressPopWin.setOnItemClickListener(new AddressAdapter.OnClickListener() {
            @Override
            public void onClick() {
                setAddressData();
            }
        });

        mAddressPopWin.showAtLocation(mView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

    private void createAddDialog() {
        mBuilder = new AlertDialog.Builder(getContext());
        mDialog = mBuilder.create();

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = 0.8f;
        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(R.drawable.edittext_shap);

        mAlertView = View.inflate(getContext(),R.layout.add_address_dialog_view,null);
        ReceiverName = (EditText) mAlertView.findViewById(R.id.et_name_addr_dialog);
        ReceiverTel = (EditText) mAlertView.findViewById(R.id.et_tel_addr_dialog);
        ReceiverAddress = (EditText) mAlertView.findViewById(R.id.et_address_addr_dialog);
        DialogCancle = (Button) mAlertView.findViewById(R.id.btn_cancle_addr_dialog);
        DialogSure = (Button) mAlertView.findViewById(R.id.btn_sure_addr_dialog);
        DialogCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        DialogSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReceiverAddress();
            }
        });
        mDialog.setView(mAlertView, 0, 0, 0, 0);

    }

    private void addReceiverAddress() {
        String name = ReceiverName.getText().toString();
        String tel = ReceiverTel.getText().toString();
        String addr = ReceiverAddress.getText().toString();

        if (name.equals("")||tel.equals("")||addr.equals("")){
            Toast.makeText(getContext(), "信息不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Address address = new Address();
            address.setName(name);
            address.setTel(tel);
            address.setAddress(addr);
            address.setId(Calendar.getInstance().getTime().toString());
            mAddressProvider.put(address);
            mAddressPopWin.updateAddressAdapter();
            mDialog.dismiss();
        }
    }

    private void createOrder() {

        if (mAddress == null) {
            Toast.makeText(getContext(), "请添加地址", Toast.LENGTH_SHORT).show();
            return;
        }

        List<ShoppingCart> carts = mCartProvider.getAll();
        ArrayList<ShoppingCart> summationCart = new ArrayList<>();
        RequestOrderInfo orderInfo = new RequestOrderInfo();
        List<OrderWareInfo> wareInfoList = new ArrayList<>();
        for (ShoppingCart cart : carts) {
            if (cart.isChecked()) {
                OrderWareInfo wareInfo = new OrderWareInfo();
                wareInfo.setCount(cart.getCount()+"");
                wareInfo.setId(cart.getId().toString());
                wareInfoList.add(wareInfo);
                summationCart.add(cart);
            }
        }
        if (wareInfoList.size() != 0){
            orderInfo.setWareList(wareInfoList);
            orderInfo.setAddressname(mAddress.getName());
            orderInfo.setTelephone(mAddress.getTel());
            mOkhttpHelper.setmSessionid(mUserProvider.getmCookie());
            Log.d(TAG, "createOrder: " +requestOrderInfo2Json(orderInfo));
            mOkhttpHelper.doPost(Constant.API.INSERTORDER_API, requestOrderInfo2Json(orderInfo),
                    new BaseCallback<ResponseMessage<String>>() {
                @Override
                public void onRequestBefore(Request request) {
                    Log.d(TAG, "onRequestBefore: ");
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(getActivity(), "网络出错", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnSuccess(Response response, ResponseMessage responseMessage) {
                    Log.d(TAG, "OnSuccess: " + responseMessage.getResult().toString());
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    intent.putExtra("charge",summationCart);
                    startActivity(intent);
                    mCartAdapter.deleteCart();

                }

                @Override
                public void onError(Response response, int code, Exception e) {
                    Toast.makeText(getActivity(), "网络出错" + code, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getContext(), "请选择商品", Toast.LENGTH_SHORT).show();
        }
    }

    private String requestOrderInfo2Json(RequestOrderInfo info){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("telephone",info.getTelephone());
            jsonObject.put("addressname",info.getAddressname());
            for (OrderWareInfo wareInfo : info.getWareList()) {
                jsonObject.put(wareInfo.getId(),wareInfo.getCount());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject==null? "" : jsonObject.toString();
    }

    @Override
    public void initRecyclerView() {
        List<ShoppingCart> carts = mCartProvider.getAll();
        if (carts == null || carts.size()==0) {
            Toast.makeText(getContext(), "购物车空空如也，赶紧添加吧", Toast.LENGTH_SHORT).show();
        }
        mTotalPrice.setText(mCartProvider.getTotalPrice()+"");
        mCartAdapter = new CartAdapter(getContext(),carts,this);
        mRecyclerView.setAdapter(mCartAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onUpdate() {
        mTotalPrice.setText(mCartProvider.getTotalPrice()+"?");
    }

    @Override
    public void onChecked(boolean isCheck) {
        mCheckBox.setChecked(isCheck);
    }

}
