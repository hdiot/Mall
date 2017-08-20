package com.mebee.mall.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mebee.mall.R;

/**
 * Created by mebee on 2017/8/16.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private LayoutInflater mInflater;

    String [] categorys = {"柑橘类","浆果类","仁果类","坚果类","核果类","瓜类","其他"};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());

        View view = mInflater.inflate(R.layout.category_item_view,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.TV_Category.setText(categorys[position]);
    }


    @Override
    public int getItemCount() {
        return categorys.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView TV_Category;

        public ViewHolder(View itemView) {
            super(itemView);
            TV_Category = (TextView) itemView.findViewById(R.id.txt_category);
        }
    }
}
