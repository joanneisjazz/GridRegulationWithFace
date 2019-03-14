package com.jstech.gridregulation.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.utils.SystemUtil;


/**
 * 一个封装好的popupwindow
 */
public class MyPopupWindow {
    private PopupWindow mPopupWindow;
    private View contentview;
    private TextView titleTextView;
    private FrameLayout contentFrameLayout;
    private Button passButton, unpassButton;
    private Context mContext;

    public Button getPassButton() {
        return passButton;
    }

    public Button getUnpassButton() {
        return unpassButton;
    }

    public void setPassButtonOnclickListener(View.OnClickListener listener) {
        passButton.setOnClickListener(null);
        if (null != passButton && null != listener) {
            passButton.setOnClickListener(listener);
        }
    }

    public void setUnPassButtonOnclickListener(View.OnClickListener listener) {
        unpassButton.setOnClickListener(null);
        if (null != unpassButton && null != listener) {
            unpassButton.setOnClickListener(listener);
        }
    }

    public MyPopupWindow(final Builder builder) {
        mContext = builder.context;
        contentview = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_window, null);
        mPopupWindow =
                new PopupWindow(contentview, SystemUtil.getWith((Activity) mContext) * 2 / 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        titleTextView = contentview.findViewById(R.id.textview_title);
        passButton = contentview.findViewById(R.id.btn_pass);
        unpassButton = contentview.findViewById(R.id.btn_unpass);
        contentFrameLayout = contentview.findViewById(R.id.frame_content);
        contentFrameLayout.addView(LayoutInflater.from(mContext).inflate(builder.contentviewid, null));
//        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
//        mPopupWindow.setFocusable(builder.fouse);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        titleTextView.setText(builder.title);
        passButton.setText(builder.pass);
        setPassButtonOnclickListener(builder.passListener);
        unpassButton.setText(builder.unpass);
        setUnPassButtonOnclickListener(builder.unpassListener);
        if (null == builder.unpassListener) {
            setUnPassButtonOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                }
            });
        }

        if (builder.isUnpassVisiable) {
            unpassButton.setVisibility(View.VISIBLE);
        } else {
            unpassButton.setVisibility(View.GONE);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
//        mPopupWindow.setAnimationStyle(builder.animstyle);
        mPopupWindow.setAnimationStyle(R.style.Animation_CustomPopup);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupWindow.dismiss();
                SystemUtil.setBackgroundAlpha(1.0f, (Activity) mContext);
            }
        });
//        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                if (!builder.outsidecancel) {
//                if (false) {
//
//                    View view = mPopupWindow.getContentView();
//                    if (null != view) {
//                        view.dispatchTouchEvent(event);
//                    }
//                }
//                return !builder.outsidecancel;
//            }
//        });

    }


    public FrameLayout getContentFrameLayout() {
        return contentFrameLayout;
    }

    /**
     * popup 消失
     */
    public void dismiss() {
        if (mPopupWindow != null) {
            if (mPopupWindow.isShowing()) {
                SystemUtil.setBackgroundAlpha(1.0f, (Activity) mContext);
                mPopupWindow.dismiss();
            }
        }
    }

    public boolean isShowing() {
        if (null != mPopupWindow) {
            return mPopupWindow.isShowing();
        }
        return false;
    }

    /**
     * 根据id获取view
     *
     * @param viewid
     * @return
     */
    public View getItemView(int viewid) {
        if (mPopupWindow != null) {
            return this.contentview.findViewById(viewid);
        }
        return null;
    }

    /**
     * 根据父布局，显示位置
     *
     * @param rootviewid
     * @param gravity
     * @param x
     * @param y
     * @return
     */
    public MyPopupWindow showAtLocation(int rootviewid, int gravity, int x, int y) {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            SystemUtil.setBackgroundAlpha(0.5f, (Activity) mContext);
            View rootview = LayoutInflater.from(mContext).inflate(rootviewid, null);
            mPopupWindow.showAtLocation(rootview, gravity, x, y);
        }
        return this;
    }

    public MyPopupWindow showAtLocationCenter(int rootviewid) {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            SystemUtil.setBackgroundAlpha(0.5f, (Activity) mContext);
            View rootview = LayoutInflater.from(mContext).inflate(rootviewid, null);
            mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        }
        return this;
    }

    /**
     * 根据id获取view ，并显示在该view的位置
     *
     * @param targetviewId
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MyPopupWindow showAsLaction(int targetviewId, int gravity, int offx, int offy) {
        if (mPopupWindow != null) {
            SystemUtil.setBackgroundAlpha(0.5f, (Activity) mContext);
            View targetview = LayoutInflater.from(mContext).inflate(targetviewId, null);
            mPopupWindow.showAsDropDown(targetview, gravity, offx, offy);
        }
        return this;
    }

    /**
     * 显示在 targetview 的不同位置
     *
     * @param targetview
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MyPopupWindow showAsLaction(View targetview, int gravity, int offx, int offy) {
        if (mPopupWindow != null) {
            SystemUtil.setBackgroundAlpha(0.5f, (Activity) mContext);
            mPopupWindow.showAsDropDown(targetview, gravity, offx, offy);
        }
        return this;
    }


    /**
     * 根据id设置焦点监听
     *
     * @param viewid
     * @param listener
     */
    public void setOnFocusListener(int viewid, View.OnFocusChangeListener listener) {
        View view = getItemView(viewid);
        view.setOnFocusChangeListener(listener);
    }


    /**
     * builder 类
     */
    public static class Builder {
        private int contentviewid;
        private int width;
        private int height;
        private boolean fouse;
        private boolean outsidecancel;
        private int animstyle;
        private String title;
        private String pass;
        private String unpass;
        private boolean isUnpassVisiable = true;
        private View.OnClickListener passListener;
        private View.OnClickListener unpassListener;

        private Context context;

        public Builder setIsUnpassVisiable(boolean isUnpassVisiable) {
            this.isUnpassVisiable = isUnpassVisiable;
            return this;
        }

        public Builder setPass(String pass) {
            this.pass = pass;
            return this;
        }

        public Builder setUnpass(String unpass) {
            this.unpass = unpass;
            return this;
        }

        public Builder setPassListener(View.OnClickListener passListener) {
            this.passListener = passListener;
            return this;
        }

        public Builder setUnpassListener(View.OnClickListener unpassListener) {
            this.unpassListener = unpassListener;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(int contentviewid) {
            this.contentviewid = contentviewid;
            return this;
        }

        public Builder setwidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setheight(int height) {
            this.height = height;
            return this;
        }

        public Builder setFouse(boolean fouse) {
            this.fouse = fouse;
            return this;
        }

        public Builder setOutSideCancel(boolean outsidecancel) {
            this.outsidecancel = outsidecancel;
            return this;
        }

        public Builder setAnimationStyle(int animstyle) {
            this.animstyle = animstyle;
            return this;
        }

        public MyPopupWindow builder() {
            return new MyPopupWindow(this);
        }
    }
}
