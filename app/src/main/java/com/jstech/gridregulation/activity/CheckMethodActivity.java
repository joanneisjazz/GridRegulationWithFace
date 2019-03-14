package com.jstech.gridregulation.activity;

import android.widget.TextView;

import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseActivity;
import com.rxretrofitlibrary.greendao.CheckItemEntityDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckItemEntity;

import butterknife.BindView;

/**
 * 查看检查方法
 */
public class CheckMethodActivity extends BaseActivity {

    @BindView(R.id.tv_method)
    TextView tvMethod;

    String strMethod;

    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_method;
    }

    @Override
    public void initView() {
        setToolSubBarTitle("");
        id = getIntent().getStringExtra("method");
        CheckItemEntityDao dao = MyApplication.getInstance().getSession().getCheckItemEntityDao();
        CheckItemEntity itemEntity = dao.queryBuilder().where(CheckItemEntityDao.Properties.Id.eq(id)).unique();
        if (null != itemEntity) {
            strMethod = itemEntity.getMethod();
        }
        tvMethod.setText(strMethod);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
