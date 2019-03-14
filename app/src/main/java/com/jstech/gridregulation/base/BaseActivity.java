package com.jstech.gridregulation.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


import com.jstech.gridregulation.R;
import com.jstech.gridregulation.activity.LoginActivity;
import com.jstech.gridregulation.activity.MainActivity;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.dialog.PictureDialog;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jstech.gridregulation.utils.AppManager.getAppManager;


/**
 * Created by suncc on 2016/11/14.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public abstract class BaseActivity extends RxAppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @BindView(R.id.toolbar_subtitle)
    TextView mToolbarSubTitle;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 沉浸式设计
         */
        //判断版本,如果[4.4)就设置状态栏和导航栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置状态栏透明
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置虚拟导航栏为透明
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //[5.0,8.0)
//        else if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
//
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            //6.0以上版本改变状态字体颜色为黑色
//            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
////            getWindow().setNavigationBarColor(Color.TRANSPARENT);
////            getWindow().setStatusBarColor(Color.TRANSPARENT);
//
//        }


        setContentView(getLayoutId());
        getAppManager().addActivity(this);
        unbinder = ButterKnife.bind(this);

        if (mToolbar != null) {
//            mToolbar.setBackgroundDrawable(getDrawable(R.mipmap.wk_bg));
            setSupportActionBar(mToolbar);
//            setOrChangeTranslucentColor(mToolbar, null);
        }

        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(getTitle());
            //设置默认的标题不显示
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initView();
        initProgressDialog(true);
    }


    /**
     * 沉浸式状态栏  设置状态栏的和底部虚拟导航栏的高度
     */
    @SuppressLint("NewApi")
    public void setOrChangeTranslucentColor(Toolbar toolbar, View bottomNavigationBar) {
        //判断版本,如果[4.4)就设置状态栏和导航栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (toolbar != null) {
                //1.先设置toolbar的高度
                ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                int statusBarHeight = AppUtil.getSystemComponentDimen(this, "status_bar_height");
                params.height += statusBarHeight;
                toolbar.setLayoutParams(params);
                //2.设置paddingTop，以达到状态栏不遮挡toolbar的内容。
                toolbar.setPadding(
                        toolbar.getPaddingLeft(),
                        toolbar.getPaddingTop() + statusBarHeight,
                        toolbar.getPaddingRight(),
                        toolbar.getPaddingBottom());

            }
        }
    }


    /**
     * 加载的布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view控件
     */
    public abstract void initView();

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView getSubTitle() {
        return mToolbarSubTitle;
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        } else {
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
        }
    }

    /**
     * 设置toobar是否隐藏
     */
    public void setToolBarVisable(int visable) {
        if (mToolbar != null) {
            mToolbar.setVisibility(visable);
        }
    }


    /**
     * 设置头部标题
     *
     * @param title
     */
    public void setToolSubBarTitle(CharSequence title) {
        if (mToolbarSubTitle != null) {
            mToolbarSubTitle.setText(title);
        } else {
            mToolbar.setSubtitle(title);
            setSupportActionBar(mToolbar);
        }
    }


    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected boolean isShowBacking() {
        return true;
    }


    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        mToolbar.setNavigationIcon(R.mipmap.ic_back_marker);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if (null != mToolbar && isShowBacking()) {
            showBack();
        }
    }


    @Override
    protected void onDestroy() {
//      AppManager.getAppManager().finishActivity(this);
        unbinder.unbind();
        dissmisPD();
        super.onDestroy();

    }


    /**
     * 解决开发者模式  用户开启不保留活动
     *
     * @param keyCode
     * @param event
     * @return
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int alwaysFinish = Settings.Global.getInt(getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
        if (alwaysFinish == 1) {
            //在这里就要先得到Activity栈里面的倒数第二个元素（比如在这之前是从A跳转到B。那么stack集合里面的元素为{X,X,X,A,B}）,然后判断A是否被finish();
            Activity activityA = getAppManager().preActivity(); //因为数组下标是从0开始，所以要减去2才能得到A
            if (activityA.isFinishing()) { //判断A activity是否被销毁,如果被销毁，则进行跳转操作，如果没有被销毁，则不操作
                Activity activityB = getAppManager().getActivityStack().get(getAppManager().getActivityStack().size() - 1);

                getAppManager().getActivityStack().remove(getAppManager().getActivityStack().size() - 2); //将A移除stack集合中
                getAppManager().getActivityStack().remove(getAppManager().getActivityStack().size() - 1); //将B移除stack集合中

                /**
                 * 返回主界面 需要结束所有的Activity
                 */
                if (activityB instanceof LoginActivity) {
                    getAppManager().finishAllActivity();
                } else {
                    startActivity(new Intent(activityB, activityA.getClass()));//从B跳转到A
                }

                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private PictureDialog pd;

    private void initProgressDialog(boolean cancel) {
        final Context context = this;
        if (pd == null && context != null) {
            pd = new PictureDialog(context);
            pd.setCancelable(cancel);
            pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    lp.dimAmount = 1.0f;
                    ((Activity) context).getWindow().setAttributes(lp);
                }
            });
        }
    }

    protected void showPD() {
        if (null != pd && !pd.isShowing())
            pd.show();
    }

    protected void dissmisPD() {
        if (null != pd && pd.isShowing())
            pd.dismiss();
    }
}
