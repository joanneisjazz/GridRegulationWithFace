package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.PictureAdapter;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.TextUtil;
import com.jstech.gridregulation.widget.MyGridLayoutManager;
import com.rxretrofitlibrary.greendao.FileEntityDao;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBeanDao;

public class RegulateDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    private ImageView ivRegulatorSign1;
    private ImageView ivRegulatorSign2;
    private ImageView ivObjectSignImage;
    private TextView tvOpinion;
    private TextView tvObjectName;
    private TextView tvRegulateDate;
    private RecyclerView rvPictures;
    private LinearLayout layoutResult;
    private LinearLayout layoutPdf;
    private LinearLayout layoutRegulator;
    private TextView tvRegulator;
    private View line;

    private PictureAdapter adapter;
    private TaskBean data;
    private List<String> picList = new ArrayList<>();
    private String regulateSign = "", regulateSignSub = "";
    private String type = "";
    private TaskBeanDao taskBeanDao;
    private FileEntityDao fileEntityDao;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regulate_detail;
    }

    @Override
    public void initView() {
        setToolBarTitle("检查结果");
        setToolSubBarTitle("");
        taskBeanDao = MyApplication.getInstance().getSession().getTaskBeanDao();
        fileEntityDao = MyApplication.getInstance().getSession().getFileEntityDao();
        findViews();
        type = getIntent().getStringExtra(ConstantValue.CODE);
        if ("ent".equals(type)) {
            line.setVisibility(View.VISIBLE);
            layoutRegulator.setVisibility(View.VISIBLE);
        }
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(this, 4);
        myGridLayoutManager.setScrollEnabled(false);
        rvPictures.setLayoutManager(myGridLayoutManager);
        adapter = new PictureAdapter(picList, this, R.layout.item_picture);
        rvPictures.setAdapter(adapter);
        getData();
        setListener();

    }

    private void findViews() {
        ivRegulatorSign1 = findViewById(R.id.iv_regulator_sign_1);
        ivRegulatorSign2 = findViewById(R.id.iv_regulator_sign_2);
        ivObjectSignImage = findViewById(R.id.iv_object_sign_image);
        tvOpinion = findViewById(R.id.tv_regulate_opinion);
        tvObjectName = findViewById(R.id.tv_object_name);
        tvRegulateDate = findViewById(R.id.tv_regulate_date);
        rvPictures = findViewById(R.id.recyclerview_pictures);
        layoutResult = findViewById(R.id.layout_result);
        layoutPdf = findViewById(R.id.layout_pdf);
        layoutRegulator = findViewById(R.id.layout_regulator);
        tvRegulator = findViewById(R.id.tv_regulator);
        line = findViewById(R.id.line_layout_regulator);
    }

    private void getData() {
        data = (TaskBean) getIntent().getSerializableExtra(ConstantValue.RESULT);
        if (null == data) {
            return;
        }

        try {
            if ("ent".equals(type)) {
                if (null == data.getPepole() || TextUtil.isEmpty(null == data.getPepole().getRoName())) {
                    tvRegulator.setText(data.getRegulatorName());
                } else {
                    tvRegulator.setText(data.getPepole().getRoName());
                }
            }
            tvObjectName.setText(data.getEntname());
            if (TextUtil.isEmpty(data.getCreateDate())) {
                tvRegulateDate.setText(data.getCreateDateLocal());
            } else {
                tvRegulateDate.setText(data.getCreateDate());
            }
            tvOpinion.setText(data.getInspopinion());

            if (TextUtil.isEmpty(data.getSupersign())) {
                if (!TextUtil.isEmpty(data.getRegulator1SignPathLocal())) {
                    Glide.with(this).load(data.getRegulator1SignPathLocal()).into(ivRegulatorSign1);
                }
                if (!TextUtil.isEmpty(data.getRegulator2SignPathLocal())) {
                    Glide.with(this).load(data.getRegulator2SignPathLocal()).into(ivRegulatorSign2);
                }
            } else {
                String[] signs = data.getSupersign().split(",");
                if (null != signs) {
                    regulateSign = signs[0];
                    Glide.with(this).load(regulateSign).into(ivRegulatorSign1);
                    if (signs.length > 1) {
                        regulateSignSub = signs[1];
                        Glide.with(this).load(regulateSignSub).into(ivRegulatorSign2);
                    }
                }
            }
            if (TextUtil.isEmpty(data.getEntsign())) {
                Glide.with(this).load(data.getObjectSignPath()).into(ivObjectSignImage);
            } else {
                Glide.with(this).load(data.getEntsign()).into(ivObjectSignImage);
            }
            picList.clear();
            if (!TextUtil.isEmpty(data.getImage())) {
                picList.addAll(Arrays.asList(data.getImage().split(",")));
                adapter.notifyDataSetChanged();
            } else {
                List<FileEntity> entities = fileEntityDao.queryBuilder().where(
                        FileEntityDao.Properties.TaskId.eq(data.getId()),
                        FileEntityDao.Properties.Type.eq("img")
                ).list();
                for (FileEntity entity : entities) {
                    picList.add(entity.getLocalPath());
                }
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            LogUtils.d(e.getMessage());
        }


    }

    private void setListener() {
        ivRegulatorSign1.setOnClickListener(this);
        ivRegulatorSign2.setOnClickListener(this);
        ivObjectSignImage.setOnClickListener(this);
        layoutPdf.setOnClickListener(this);
        layoutResult.setOnClickListener(this);
        adapter.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_regulator_sign_1:
                intent.setClass(this, PictureAvtivity.class);
                if (TextUtil.isEmpty(regulateSign)) {
                    intent.putExtra(ConstantValue.SGIN_PATH, data.getRegulator1SignPathLocal());
                } else {
                    intent.putExtra(ConstantValue.SGIN_PATH, regulateSign);
                }
                startActivity(intent);
                break;
            case R.id.iv_regulator_sign_2:
                if (TextUtil.isEmpty(regulateSignSub)) {
                    intent.putExtra(ConstantValue.SGIN_PATH, data.getRegulator2SignPathLocal());
                } else {
                    intent.putExtra(ConstantValue.SGIN_PATH, regulateSignSub);
                }
                intent.setClass(this, PictureAvtivity.class);
                startActivity(intent);
                break;
            case R.id.iv_object_sign_image:
                if (TextUtil.isEmpty(data.getEntsign())) {
                    intent.putExtra(ConstantValue.SGIN_PATH, data.getObjectSignPath());
                } else {
                    intent.putExtra(ConstantValue.SGIN_PATH, data.getEntsign());
                }
                intent.setClass(this, PictureAvtivity.class);
                startActivity(intent);
                break;
            case R.id.layout_result:
                intent.setClass(this, RegulateItemResultActivity.class);
                intent.putExtra(ConstantValue.SGIN_PATH, data.getId());
                startActivity(intent);
                break;
            case R.id.layout_pdf:
//                Toast.makeText(this, "正在开发中", Toast.LENGTH_LONG).show();
                if (TextUtil.isEmpty(data.getInspdoc())) {
                    FileEntity pdf = fileEntityDao.queryBuilder().where(
                            FileEntityDao.Properties.TaskId.eq(data.getId()),
                            FileEntityDao.Properties.Type.eq("pdf")
                    ).unique();
                    intent.putExtra("url", pdf.getLocalPath());
                } else {
                    intent.putExtra("url", data.getInspdoc());
                }

                intent.setClass(RegulateDetailActivity.this, PdfActivity.class);
                intent.putExtra("url", data.getInspdoc());
                startActivity(intent);
                break;
            default:
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.setClass(this, PictureAvtivity.class);
        intent.putExtra(ConstantValue.SGIN_PATH, picList.get(i));
        startActivity(intent);
    }
}
