package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.TextUtil;

import butterknife.BindView;

public class InputCaseDetailActivity extends BaseActivity {

    @BindView(R.id.edt_details)
    EditText edtDetails;
    @BindView(R.id.btn_save)
    Button btnSave;
    String details;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input_case_detail;
    }

    @Override
    public void initView() {
        setToolBarTitle("请输入详细信息");
        setToolSubBarTitle("");
        details = getIntent().getStringExtra(ConstantValue.KEY_CONTENT);
        if (TextUtil.isEmpty(details) || ConstantValue.NULL.equals(details)) {
            edtDetails.setText("");
        } else {
            edtDetails.setText(details);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ConstantValue.RESULT, edtDetails.getText().toString());
                InputCaseDetailActivity.this.setResult(RESULT_OK, intent);
                InputCaseDetailActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        InputCaseDetailActivity.this.setResult(RESULT_CANCELED);
        InputCaseDetailActivity.this.finish();
    }
}
