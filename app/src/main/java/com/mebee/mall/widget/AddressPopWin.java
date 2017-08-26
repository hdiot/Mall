package com.mebee.mall.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.mebee.mall.R;
import com.mebee.mall.adapter.AddressAdapter;
import com.mebee.mall.bean.Address;
import com.mebee.mall.utils.AddressProvider;

import java.util.List;

/**
 * Created by mebee on 2017/8/23.
 */

public class AddressPopWin extends PopupWindow {
    private static final String TAG = "AddressPopWin";

    private ImageButton ImgBtn_Close;
    private ImageButton ImgBtn_Add;
    private RecyclerView RV_Address;
    private LinearLayout LY_Add_Address;
    private View mView;
    private OnClickListener mListener;
    private AddressProvider mAddressesProvider;
    private List<Address> mAddresses;
    private Context mContext;
    private AddressAdapter mAddressAdapter;

    public interface OnClickListener{
        public void onAddClick();
    }

    public void setOnClickListener(OnClickListener listener){
        this.mListener = listener;
    }

    public AddressPopWin(Context context) {
        super(context);
        this.mAddressesProvider = AddressProvider.getInstance(context);
        this.mView = LayoutInflater.from(context).inflate(R.layout.address_adit_dialog,null);
        ImgBtn_Close = (ImageButton) mView.findViewById(R.id.imgbtn_close_addrdialog);
        RV_Address = (RecyclerView) mView.findViewById(R.id.rv_address_addrdialog);
        LY_Add_Address = (LinearLayout) mView.findViewById(R.id.ly_add_address);

        initAddressesData();

        setListener();

        setWindowParms();
    }

    private void initAddressesData() {
        mAddresses = mAddressesProvider.getAll();
        mAddressAdapter = new AddressAdapter(mContext);
        RV_Address.setAdapter(mAddressAdapter);
        RV_Address.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void updateAddressAdapter(){
        if (mAddressAdapter != null) {
            mAddressAdapter.updateAdapter();
        }
    }


    public void setOnItemClickListener(AddressAdapter.OnClickListener listener){
        if (mAddressAdapter != null) {
            mAddressAdapter.setOnClickListener(listener);
        }
    }

    private void setListener(){
        ImgBtn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        LY_Add_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onAddClick();
            }
        });
    }

    private void setWindowParms(){
        this.setContentView(this.mView);
        // 设置弹出窗体的宽和高
        Log.d(TAG, "setWindowParms: "+mView.getHeight()/2);
        this.setHeight(1000);
        //this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色
        ColorDrawable dw = new ColorDrawable(0x00FFFF);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);


        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.address_window_anim);
    }
}
