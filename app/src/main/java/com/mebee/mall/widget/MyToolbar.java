package com.mebee.mall.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mebee.mall.R;

/**
 * Created by mebee on 2017/8/2.
 */

public class MyToolbar extends Toolbar {

    private static final String TAG = "CnToolBar";
    private LayoutInflater mInflatter;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightImageButton;
    private View mView;

    public MyToolbar(Context context) {
        this(context, null);
        Log.d(TAG, "CnToolbar:1 ");
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        Log.d(TAG, "CnToolbar:2 ");
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "CnToolbar:3 ");
        initView();

        // 设置边距
        setContentInsetsRelative(10,10);

        // 如果attrs 不为空，获取属性值，并根据属性值修改子 view 显示状态
        if (attrs != null) {
            final TintTypedArray mTintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(),attrs,
                    R.styleable.MyToolbar,defStyleAttr,0);

            final Drawable mRightButton = mTintTypedArray.getDrawable(R.styleable.MyToolbar_rightButtonIcon);
            if (mRightButton != null){
                setRightButton(mRightButton);
            }

            boolean isShowSeachView = mTintTypedArray.getBoolean(R.styleable.MyToolbar_isShowSearchView,false);
            if (isShowSeachView) {
                showSearchView();
                hideTitleView();
            }
        }

    }



    private void initView() {

        // 判断mView是否为空
        if (mView == null) {
            mInflatter = LayoutInflater.from(getContext());

            mView = mInflatter.inflate(R.layout.toolbar,null);

            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightImageButton = (Button) mView.findViewById(R.id.toolbar_rightButton);

            if (mTextTitle==null){
                Log.d("view", "initView: ");
            }

            // 设置宽度和高度
            LayoutParams mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            // 加载view
            addView(mView,mLayoutParams);
        }
    }


    public void setRightButton(Drawable icon) {
        if (mRightImageButton != null) {
            mRightImageButton.setBackgroundDrawable(icon);
            mRightImageButton.setVisibility(VISIBLE);
        }
    }

    public void setRightButtonOnClickListener(OnClickListener listener){
        mRightImageButton.setOnClickListener(listener);
    }

    /**
     * 重写 setTitle() 方法，避免自定义的标题与原生的标题冲突
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        if (title != null) {
            initView();
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    /**
     * 重写 setTitle() 方法，避免自定义的标题与原生的标题冲突
     * @param resId
     */
    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }


    public void showSearchView(){
        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);
    }

    public void hideSearchView(){
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView(){
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }

    public void hideTitleView(){
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);
    }
}
