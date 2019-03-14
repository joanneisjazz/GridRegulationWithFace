package com.jstech.gridregulation.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by suncc on 2016/11/14.
 */

public abstract class BaseFragment extends RxFragment {

    private Unbinder unbinder;

    private View rootView;

    //配合viewpager+fragment的懒汉式加载
    protected boolean isVisible;//标记当前Fragment是否被用户可见

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//判断用户是否可见当前Fragment
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    private void onVisible() {
        LazyLoad();//只有在用户可见的情况下我们才去加载数据
    }

    private void onInvisible() {//用户不可见的情况下不去加载任何的数据
    }

    //加载网络数据
    protected abstract void LazyLoad();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {

            rootView = inflater.inflate(getLayoutResource(), container, false);
            unbinder = ButterKnife.bind(this, rootView);
            initView(rootView);
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，
            // 要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }

    public abstract int getLayoutResource();

    //初始化view
    public abstract void initView(View rootView);

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
