package com.mebee.mall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mebee.mall.R;
import com.mebee.mall.activity.OrderDetailActivity;
import com.mebee.mall.bean.ResOrderInfo;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by mebee on 2017/8/26.
 */

public class OrdersAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "OrdersAdapter";
    private static final String ADDRESSID = "addressid";
    private static final String ORDERID = "orderid";
    private static final String ORDERSTATE = "orderstate";
    private List<List<ResOrderInfo>> mDatas;

    private final String [] mStates = {"全部订单", "待付款" , "代发货" , "待收货", "待评论"};
    private Activity mContext;

    public OrdersAdapter(List<List<ResOrderInfo>> mDatas, Activity context) {
        this.mDatas = mDatas;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return mStates.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition)==null? 0:mDatas.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mStates[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas == null ? null : mDatas.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_category_item_view,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.txt_order_category);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.tvTitle.setText((CharSequence) getGroup(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_view,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.view = convertView;
            childViewHolder.rvImg = (RecyclerView) convertView.findViewById(R.id.rv_order_imgs_orderitem);
            childViewHolder.tvId = (TextView) convertView.findViewById(R.id.txt_order_id);
            childViewHolder.tvState = (TextView) convertView.findViewById(R.id.txt_order_state);
            childViewHolder.tvSummatio = (TextView) convertView.findViewById(R.id.txt_order_summation);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        ResOrderInfo info = (ResOrderInfo) getChild(groupPosition,childPosition);
        switch (info.getOrder_state()) {
            case 0:
                childViewHolder.tvState.setText(mContext.getString(R.string.order_state) + mContext.getString(R.string.wait_for_pay));
                break;
            case 1:
                childViewHolder.tvState.setText(mContext.getString(R.string.order_state)+mContext.getString(R.string.wait_for_deliver));
                break;
            case 2:
                childViewHolder.tvState.setText(mContext.getString(R.string.order_state)+mContext.getString(R.string.wait_for_receive));
                break;
            case 3:
                childViewHolder.tvState.setText(mContext.getString(R.string.order_state)+mContext.getString(R.string.wait_for_comment));
                break;
        }
        childViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(ORDERID,info.getId());
                intent.putExtra(ADDRESSID, info.getAddress_id());
                intent.putExtra(ORDERSTATE, info.getOrder_state());
                mContext.finish();
                startActivity(mContext,intent,null);
            }
        });
        childViewHolder.tvId.setText(mContext.getString(R.string.order_id)+info.getId());
        childViewHolder.rvImg.setAdapter(new OrderItemImgAdapter());
        childViewHolder.rvImg.setLayoutManager(new GridLayoutManager(mContext,3));
        convertView.requestFocus();
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        TextView tvTitle;
    }

    class ChildViewHolder{
        RecyclerView rvImg;
        TextView tvState;
        TextView tvId;
        TextView tvSummatio;
        View view;
    }
}
