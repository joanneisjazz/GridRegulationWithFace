package com.jstech.gridregulation.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.utils.SystemUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;

/**
 * 在地图上显示的底部弹框
 */
public class MapBottomWindow {

    PopupWindow mPopupWindow;
    LinearLayout layoutRegulator, layoutCount;
    TextView tvObjectName, tvAddress, tvDistance, tvPoint, tvCount, tvRate, tvTel, tvRegulator;
    Button btnCheck;
    private Context mContext;
    View contentview;
    RegulateObjectBean obj;
    TextView tvCountText;


    public MapBottomWindow(Context mContext) {
        this.mContext = mContext;
    }

    public MapBottomWindow(final Builder builder) {
        mContext = builder.context;
        contentview = LayoutInflater.from(mContext).inflate(R.layout.layout_map_bottom_window, null);
        mPopupWindow =
                new PopupWindow(contentview, SystemUtil.getWith((Activity) mContext), ViewGroup.LayoutParams.WRAP_CONTENT, builder.fouse);
        tvAddress = contentview.findViewById(R.id.tv_address);
        tvDistance = contentview.findViewById(R.id.tv_distance);
        tvPoint = contentview.findViewById(R.id.tv_point);
        tvCount = contentview.findViewById(R.id.tv_count);
        tvRate = contentview.findViewById(R.id.tv_rate);
        layoutRegulator = contentview.findViewById(R.id.layout_regulator);
        tvTel = contentview.findViewById(R.id.tv_tel);
        tvRegulator = contentview.findViewById(R.id.tv_regulator);
        tvObjectName = contentview.findViewById(R.id.tv_object_name);
        btnCheck = contentview.findViewById(R.id.btn_check);
        layoutCount = contentview.findViewById(R.id.layout_count);
        tvCountText = contentview.findViewById(R.id.tv_count_text);
        tvCountText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCountText.getPaint().setAntiAlias(true);
        tvCount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCount.getPaint().setAntiAlias(true);

        btnCheck.setText(builder.status);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obj != null) {
                    builder.listener.task(obj);
                }
            }
        });
//        mPopupWindow.setFocusable(builder.fouse);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setAnimationStyle(builder.animstyle);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                SystemUtil.setBackgroundAlpha(1.0f, (Activity) mContext);
            }
        });


    }


    public void dismiss() {
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            SystemUtil.setBackgroundAlpha(1.0f, (Activity) mContext);
            mPopupWindow.dismiss();
        }
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        if (null != mPopupWindow && null != listener) {
            mPopupWindow.setOnDismissListener(listener);
        }
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
        private RegulateObjectBean obj;
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

        public MapBottomWindow builder() {
            return new MapBottomWindow(this);
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

    public TextView getTvPoint() {
        return tvPoint;
    }

    public void setTvPoint(TextView tvPoint) {
        this.tvPoint = tvPoint;
    }

    public TextView getTvCount() {
        return tvCount;
    }

    public void setTvCount(TextView tvCount) {
        this.tvCount = tvCount;
    }

    public TextView getTvRate() {
        return tvRate;
    }

    public void setTvRate(TextView tvRate) {
        this.tvRate = tvRate;
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

    public TextView getTvRegulator() {
        return tvRegulator;
    }

    public void setTvRegulator(TextView tvRegulator) {
        this.tvRegulator = tvRegulator;
    }

    public LinearLayout getLayoutRegulator() {
        return layoutRegulator;
    }

    public void setLayoutRegulator(LinearLayout layoutRegulator) {
        this.layoutRegulator = layoutRegulator;
    }

    public LinearLayout getLayoutCount() {
        return layoutCount;
    }

    public void setLayoutCount(LinearLayout layoutCount) {
        this.layoutCount = layoutCount;
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
    public MapBottomWindow showAtLocation(int rootviewid, int gravity, int x, int y) {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
//            SystemUtil.setBackgroundAlpha(0.5f, (Activity) mContext);
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
    public MapBottomWindow showAsLaction(int targetviewId, int gravity, int offx, int offy) {
        if (mPopupWindow != null) {
//            SystemUtil.setBackgroundAlpha(0.5f, (Activity) mContext);
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
    public MapBottomWindow showAsLaction(View targetview, int gravity, int offx, int offy) {
        if (mPopupWindow != null) {
//            SystemUtil.setBackgroundAlpha(0.5f, (Activity) mContext);
            mPopupWindow.showAsDropDown(targetview, gravity, offx, offy);
        }
        return this;
    }

    public MapBottomWindow showAsDropDown(int height) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(contentview, 0, height);

        }
        return this;
    }

    public interface TaskInterface {
        void task(RegulateObjectBean objectBean);
    }

    public View getContentview() {
        return contentview;
    }

    public void setContentview(View contentview) {
        this.contentview = contentview;
    }
}
