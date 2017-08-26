package com.mebee.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mebee.mall.R;
import com.mebee.mall.bean.Address;
import com.mebee.mall.utils.AddressProvider;

import java.util.List;

/**
 * Created by mebee on 2017/8/23.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private static final String TAG = "AddressAdapter";

    private LayoutInflater mInflater;
    private View mView;
    private List<Address> mAddresses;
    private AddressProvider mProvider;
    private OnClickListener mListener;


    public AddressAdapter(Context context) {
        mProvider = AddressProvider.getInstance(context);
        mAddresses = mProvider.getAll();
    }

    public void updateAdapter(){
        mAddresses = mProvider.getAll();
        notifyDataSetChanged();
    }

    public interface OnClickListener{
        void onClick();
    }

    public void setOnClickListener(OnClickListener listener){
        this.mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        mView = mInflater.inflate(R.layout.address_item_view,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Address addr = mAddresses.get(position);
        Log.d(TAG, "onBindViewHolder: " + addr.getName()+addr.getTel()+addr.getAddress()+addr.getId());
        holder.txtName.setText(addr.getName());
        holder.txtTel.setText(addr.getTel());
        holder.txtAddress.setText(addr.getAddress());
        holder.cbSelect.setChecked(addr.isChecked());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickListen(addr);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: ");
                mProvider.delete(addr.getId());
                mAddresses = mProvider.getAll();
                notifyDataSetChanged();
                return true;
            }
        });
    }

    private void onclickListen(Address address){
        Log.d(TAG, "onLongClick: "+ address.isChecked());
        if (!address.isChecked()) {
            for (Address mAddress : mAddresses) {
                mAddress.setChecked(false);
                mProvider.update(mAddress);
            }
            address.setChecked(true);
            mProvider.update(address);
            notifyDataSetChanged();
            if( mListener!= null){
                mListener.onClick();
            }
        }
    }


    @Override
    public int getItemCount() {
        return mAddresses==null? 0 : mAddresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CheckBox cbSelect;
        public TextView txtName;
        public TextView txtTel;
        public TextView txtAddress;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            cbSelect = (CheckBox) itemView.findViewById(R.id.cb_select_addr_item);
            txtName = (TextView) itemView.findViewById(R.id.txt_name_addr_item);
            txtTel = (TextView) itemView.findViewById(R.id.txt_tel_addr_item);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address_addr_item);
            view = itemView;
        }
    }
}
