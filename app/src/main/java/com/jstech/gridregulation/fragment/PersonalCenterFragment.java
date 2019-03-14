package com.jstech.gridregulation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.activity.IlleagelCaseListActivity;
import com.jstech.gridregulation.activity.MainActivity;
import com.jstech.gridregulation.activity.PersonalInfoActivity;
import com.jstech.gridregulation.activity.RegulateListAvtivity;
import com.jstech.gridregulation.activity.SettingActivity;
import com.jstech.gridregulation.activity.TestActivity;
import com.jstech.gridregulation.base.BaseFragment;
import com.jstech.gridregulation.utils.TextUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;

public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener {

    public static PersonalCenterFragment newInstance() {

        Bundle args = new Bundle();

        PersonalCenterFragment fragment = new PersonalCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    RelativeLayout layoutPersonalInfo;
    LinearLayout layoutRegulateRecords;
    LinearLayout layoutReporteRecords;
    TextView tvName;
    TextView tvLoginName;

    UserBean userBean = null;

    @Override
    protected void LazyLoad() {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_personal_center;
    }

    @Override
    public void initView(View rootView) {
        userBean = MyApplication.getInstance().getUserBean();
        findViews(rootView);
    }

    private void findViews(View rootView) {
        layoutPersonalInfo = rootView.findViewById(R.id.layout_personal_info);
        layoutRegulateRecords = rootView.findViewById(R.id.layout_personal_regulate_records);
        layoutReporteRecords = rootView.findViewById(R.id.layout_personal_reporte_records);
        tvLoginName = rootView.findViewById(R.id.tv_login_name);
        tvName = rootView.findViewById(R.id.tv_name);
        layoutPersonalInfo.setOnClickListener(this);
        layoutRegulateRecords.setOnClickListener(this);
        layoutReporteRecords.setOnClickListener(this);

        tvName.setText(userBean.getUsername());
        tvLoginName.setText("用户名：" + userBean.getLoginname());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.layout_personal_info:
                intent.setClass(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_personal_regulate_records:
                intent.setClass(getActivity(), RegulateListAvtivity.class);
                startActivity(intent);

                break;
            case R.id.layout_personal_reporte_records:
                intent.setClass(getActivity(), IlleagelCaseListActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void setting() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

}
