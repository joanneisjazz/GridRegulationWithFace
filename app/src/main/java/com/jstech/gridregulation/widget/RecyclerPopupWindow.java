package com.jstech.gridregulation.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.utils.SystemUtil;


/**
 * 一个封装好的popupwindow，不需要写重复的代码
 */
public class RecyclerPopupWindow {
    private PopupWindow mPopupWindow;
    private View contentview;
    private TextView titleTextView;
    private RecyclerView recyclerView;
    private Context mContext;
    private Button btnBack;

    public TextView getTitleTextView() {
        return titleTextView;
    }


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


    public RecyclerPopupWindow(final Builder builder) {
        mContext = builder.context;
        contentview = LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_popup_window, null);
        mPopupWindow =
                new PopupWindow(contentview, builder.width, builder.height, builder.fouse);
        titleTextView = contentview.findViewById(R.id.textview_title);
//        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
//        mPopupWindow.setFocusable(builder.fouse);
        btnBack = contentview.findViewById(R.id.btn_back);
        titleTextView.setText(builder.title);
        recyclerView = contentview.findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setAnimationStyle(builder.animstyle);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                SystemUtil.setBackgroundAlpha(1.0f, (Activity) mContext);
            }
        });
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!builder.outsidecancel) {
                    View view = mPopupWindow.getContentView();
                    if (null != view) {
                        view.dispatchTouchEvent(event);
                    }
                }
                return !builder.outsidecancel;
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    /**
     * popup 消失
     */
    public void dismiss() {
        if (mPopupWindow != null) {
            if (mPopupWindow.isShowing()) {
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
    public RecyclerPopupWindow showAtLocation(int rootviewid, int gravity, int x, int y) {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            SystemUtil.setBackgroundAlpha(0.5f, (Activity) mContext);
            View rootview = LayoutInflater.from(mContext).inflate(rootviewid, null);
            mPopupWindow.showAtLocation(rootview, gravity, x, y);
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
    public RecyclerPopupWindow showAsLaction(int targetviewId, int gravity, int offx, int offy) {
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
    public RecyclerPopupWindow showAsLaction(View targetview, int gravity, int offx, int offy) {
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
        private int width;
        private int height;
        private boolean fouse;
        private boolean outsidecancel;
        private int animstyle;
        private String title;

        private Context context;


        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
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

        public RecyclerPopupWindow builder() {
            return new RecyclerPopupWindow(this);
        }
    }
}
