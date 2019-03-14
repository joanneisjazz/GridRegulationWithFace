package com.jstech.gridregulation.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.utils.SystemUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;

/**
 * 选择不合格的弹出框
 */
public class ReminderWindow {

    PopupWindow mPopupWindow;
    TextView tvObjectName,tvAddress, tvDistance, tvDetails, tvTel;
    Button btnCheck;
    private Context mContext;
    View contentview;
    RegulateObjectBean obj;

    public ReminderWindow(Context mContext) {
        this.mContext = mContext;
    }

    public ReminderWindow(final Builder builder) {
        mContext = builder.context;
        contentview = LayoutInflater.from(mContext).inflate(R.layout.layout_map_bottom_window, null);
        mPopupWindow =
                new PopupWindow(contentview, builder.width, builder.height, builder.fouse);
        tvAddress = contentview.findViewById(R.id.tv_object_name);
        tvDistance = contentview.findViewById(R.id.tv_distance);
//        tvDetails = contentview.findViewById(R.id.tv_details);
        tvTel = contentview.findViewById(R.id.tv_tel);
        tvObjectName = contentview.findViewById(R.id.tv_object_name);
        btnCheck = contentview.findViewById(R.id.btn_check);

        btnCheck.setText(builder.status);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obj!=null){
                    builder.listener.task(obj);
                }
            }
        });
        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setAnimationStyle(builder.animstyle);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                SystemUtil.setBackgroundAlpha(1.0f, (Activity) mContext);
            }
        });
    }

    /**
     * 设置点击事件
     *
     * @param listener
     */
    private void setOnClickListener(View.OnClickListener listener) {
        if (null == btnCheck) {
            return;
        }
        btnCheck.setOnClickListener(null);
        if (null != listener) {
            btnCheck.setOnClickListener(listener);
        }
    }

    public RegulateObjectBean getObj() {
        return obj;
    }

    public void setObj(RegulateObjectBean obj) {
        this.obj = obj;
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
        private String status;
        private TaskInterface listener;

        private Context context;

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setListener(TaskInterface listener) {
            this.listener = listener;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
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

        public ReminderWindow builder() {
            return new ReminderWindow(this);
        }
    }

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    public void setmPopupWindow(PopupWindow mPopupWindow) {
        this.mPopupWindow = mPopupWindow;
    }

    public TextView getTvAddress() {
        return tvAddress;
    }

    public void setTvAddress(TextView tvAddress) {
        this.tvAddress = tvAddress;
    }

    public TextView getTvDistance() {
        return tvDistance;
    }

    public void setTvDistance(TextView tvDistance) {
        this.tvDistance = tvDistance;
    }

    public TextView getTvDetails() {
        return tvDetails;
    }

    public void setTvDetails(TextView tvDetails) {
        this.tvDetails = tvDetails;
    }

    public TextView getTvTel() {
        return tvTel;
    }

    public void setTvTel(TextView tvTel) {
        this.tvTel = tvTel;
    }

    public Button getBtnCheck() {
        return btnCheck;
    }

    public void setBtnCheck(Button btnCheck) {
        this.btnCheck = btnCheck;
    }

    public TextView getTvObjectName() {
        return tvObjectName;
    }

    public void setTvObjectName(TextView tvObjectName) {
        this.tvObjectName = tvObjectName;
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
    public ReminderWindow showAtLocation(int rootviewid, int gravity, int x, int y) {
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
    public ReminderWindow showAsLaction(int targetviewId, int gravity, int offx, int offy) {
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
    public ReminderWindow showAsLaction(View targetview, int gravity, int offx, int offy) {
        if (mPopupWindow != null) {
            SystemUtil.setBackgroundAlpha(0.5f, (Activity) mContext);
            mPopupWindow.showAsDropDown(targetview, gravity, offx, offy);
        }
        return this;
    }

    public interface TaskInterface{
        void task(RegulateObjectBean objectBean);
    }
}
