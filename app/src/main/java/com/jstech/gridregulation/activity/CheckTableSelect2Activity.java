package com.jstech.gridregulation.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.MyApplication;
import com.jstech.gridregulation.R;
import com.jstech.gridregulation.adapter.RegulateItemAdpter;
import com.jstech.gridregulation.api.AddTaskApi;
import com.jstech.gridregulation.api.GetTableAndItem;
import com.jstech.gridregulation.api.SaveItemResultApi;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.LogUtils;
import com.jstech.gridregulation.utils.TextUtil;
import com.rxretrofitlibrary.greendao.CheckItemEntityDao;
import com.rxretrofitlibrary.greendao.CheckTableEntityDao;
import com.rxretrofitlibrary.greendao.DaoSession;
import com.rxretrofitlibrary.greendao.RegulateObjectBeanDao;
import com.rxretrofitlibrary.greendao.RegulateResultBeanDao;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckItemEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckTableEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 选择检查表
 */
public class CheckTableSelect2Activity extends BaseActivity implements View.OnClickListener, RegulateItemAdpter.ClickInterface,
        HttpOnNextListener {

    @BindView(R.id.listview)
    ExpandableListView listView;

    private ArrayList<CheckTableEntity> mCheckTableBeanList = new ArrayList<>();
    private CheckTableEntity seletedTable = null;//选中的检查表
    private ArrayList<CheckItemEntity> selectedItemList = new ArrayList<>();//选中的检查项

    private RegulateItemAdpter mAdpter;
    private HttpManager manager;
    private GetTableAndItem getTableAndItem;
    private AddTaskApi addTaskApi;
    private SaveItemResultApi saveItemResultApi;

    private String objectId;

    private int selectedPosiotn = -1;//选中的表的位置
    private int oldPostion = -1;//上一次点击的检查表的位置

    private CheckItemEntityDao itemDao;
    private CheckTableEntityDao tableDao;
    private RegulateObjectBeanDao regulateObjectBeanDao;
    private RegulateResultBeanDao resultBeanDao;
    private TaskBeanDao taskBeanDao;

    private String userExtId;
    private RegulateObjectBean object;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_table_select;
    }

    @Override
    public void initView() {
        setToolSubBarTitle("下一步");
        initDao();//初始化数据库dao文件
        userExtId = SharedPreferencesHelper.getInstance(this).getSharedPreference("extId", "").toString();
        objectId = getIntent().getStringExtra(ConstantValue.KEY_OBJECT_ID);
        object = (RegulateObjectBean) getIntent().getSerializableExtra(ConstantValue.KEY_OBJECT_BEAN);
//        if (!TextUtil.isEmpty(objectId)) {
//            object = regulateObjectBeanDao.queryBuilder().where(
//                    RegulateObjectBeanDao.Properties.Id.eq(objectId)).unique();
//        }

        mAdpter = new RegulateItemAdpter(this, mCheckTableBeanList, this);
        listView.setAdapter(mAdpter);
        setClickListener();

        manager = new HttpManager(this, this);
        addTaskApi = new AddTaskApi();
        getTableAndItem = new GetTableAndItem();
        saveItemResultApi = new SaveItemResultApi();

        mCheckTableBeanList.addAll(tableDao.loadAll());
        mAdpter.notifyDataSetChanged();
        if (mCheckTableBeanList.isEmpty() || mCheckTableBeanList.size() <= 0) {
            manager.doHttpDeal(getTableAndItem);
        }

    }


    private void initDao() {
        DaoSession session = MyApplication.getInstance().getSession();
        taskBeanDao = session.getTaskBeanDao();
        itemDao = session.getCheckItemEntityDao();
        tableDao = session.getCheckTableEntityDao();
        resultBeanDao = MyApplication.getInstance().getSession().getRegulateResultBeanDao();
        regulateObjectBeanDao = MyApplication.getInstance().getSession().getRegulateObjectBeanDao();
    }

    String tableId = "";

    boolean isAllSelected = false;

    private void setClickListener() {
        getSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemList.size() == 0 || oldPostion == -1) {
                    Toast.makeText(CheckTableSelect2Activity.this, "请选择检查内容", Toast.LENGTH_LONG).show();
                    return;
                }
                showPD();
                addTask();
            }
        });
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                /**
                 * 判断下面是否有子项，如果没有要去数据库里找出来
                 */
                if (null == mCheckTableBeanList.get(groupPosition).getCheckItemBeans()) {
                    List<CheckItemEntity> itemEntityList = itemDao.queryBuilder().where(
                            CheckItemEntityDao.Properties.Tableid.eq(mCheckTableBeanList.get(groupPosition).getId()))
                            .list();
                    mCheckTableBeanList.get(groupPosition).setCheckItemBeans(itemEntityList);
                }

                /**
                 * 判断上次点击的位置与本次点击的位置是否相同
                 * 如果相同，判读现在的转态是展开还是关闭
                 * 如果是展开状态就关闭
                 * 如果是关闭状态就展开
                 */
                if (oldPostion == groupPosition) {
                    if (mCheckTableBeanList.get(groupPosition).isExpanded()) {
                        mCheckTableBeanList.get(groupPosition).setExpanded(false);
                        listView.collapseGroup(groupPosition);
                    } else {
                        mCheckTableBeanList.get(groupPosition).setExpanded(true);
                        listView.expandGroup(groupPosition);
                    }
                    mAdpter.notifyDataSetChanged();
                    return true;
                }

                /**
                 * 如果位置不相同
                 * 判断之前点击的位置是否为-1，如果是-1说明是第一次点击
                 * 如果不是-1，则需要把之前点击的检查表关闭
                 */
                if (oldPostion != -1) {
                    mCheckTableBeanList.get(oldPostion).setExpanded(false);
                    listView.collapseGroup(oldPostion);
                }
                mCheckTableBeanList.get(groupPosition).setExpanded(true);
                listView.expandGroup(groupPosition);
                listView.setSelectedGroup(groupPosition);
                mAdpter.notifyDataSetChanged();
                oldPostion = groupPosition;
                return true;

            }
        });


        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CheckTableEntity checkTableBean = mCheckTableBeanList.get(groupPosition);
                CheckItemEntity checkItemBean = checkTableBean.getCheckItemBeans().get(childPosition);
                tableId = checkTableBean.getId();
                /**
                 * 判断该项是否已经选中
                 * 如果已经选中，取消该项的选中转态
                 */
                if (checkItemBean.getIsSelected()) {
                    /**
                     * 取消的时候
                     */
                    checkItemBean.setIsSelected(false);
                    selectedItemList.remove(checkItemBean);
                    mCheckTableBeanList.get(groupPosition).setAllSected(false);
                    if (isItemAllUnselected(mCheckTableBeanList.get(groupPosition).getCheckItemBeans())) {
                        mCheckTableBeanList.get(groupPosition).setSelected(false);
                    }
                } else {
                    /**
                     * 选中的时候
                     * 先将之前选中的表和检查项清除掉
                     */

                    for (CheckTableEntity e : mCheckTableBeanList) {
                        if (e.getId() != checkTableBean.getId() && e.isSelected()) {
                            e.setAllSected(false);
                            e.setSelected(false);
                            setItemsSelected(e.getCheckItemBeans(), false);
                        }
                    }

                    checkItemBean.setIsSelected(true);
                    selectedItemList.add(checkItemBean);
                    mCheckTableBeanList.get(groupPosition).setSelected(true);
                    if (isItemAllSelected(checkTableBean.getCheckItemBeans())) {
                        mCheckTableBeanList.get(groupPosition).setAllSected(true);
                    } else {
                        mCheckTableBeanList.get(groupPosition).setAllSected(false);
                    }
                }
                mAdpter.notifyDataSetChanged();
                return false;
            }
        });
    }


    private boolean isItemAllSelected(List<CheckItemEntity> list) {
        for (CheckItemEntity entity : list) {
            if (!entity.getIsSelected()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }


    String taskid = "";
    List<RegulateResultBean> regulateResultBeans = new ArrayList<>();

    @Override
    public void onNext(String resulte, String method) {
        LogUtils.d(resulte);
        LogUtils.d(method);
        JSONObject o = JSON.parseObject(resulte);
        final String code = o.getString(ConstantValue.CODE);
        if (!code.equals(ConstantValue.CODE_SUCCESS)) {
//            Toast.makeText(CheckTableSelect2Activity.this, resulte, Toast.LENGTH_LONG).show();
            return;
        }
        if (method.equals(getTableAndItem.getMethod())) {
            //保存所有的检查表和检查项
            Observable.just(o.getJSONObject(ConstantValue.RESULT))
                    .observeOn(Schedulers.newThread())
                    .map(new Func1<JSONObject, Object>() {
                        @Override
                        public Object call(JSONObject jsonObject) {
                            //解析json数据，包括检查表和检查项
                            JSONArray arrayTables = jsonObject.getJSONArray(ConstantValue.TABLES);
                            tableDao.deleteAll();
                            tableDao.saveInTx(arrayTables.toJavaList(CheckTableEntity.class));
                            JSONArray arrayItems = jsonObject.getJSONArray(ConstantValue.ITEMS);
                            itemDao.deleteAll();
                            itemDao.saveInTx(arrayItems.toJavaList(CheckItemEntity.class));
                            mCheckTableBeanList.clear();
                            mCheckTableBeanList.addAll(arrayTables.toJavaList(CheckTableEntity.class));

                            return null;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            mAdpter.notifyDataSetChanged();
                        }
                    });
        } else if (method.equals(addTaskApi.getMethod())) {

            Observable.just(o).observeOn(Schedulers.newThread())
                    .map(new Func1<JSONObject, Exception>() {
                        @Override
                        public Exception call(JSONObject jsonObject) {
                            //保存到数据库
                            try {
//                                taskid = jsonObject.getString(ConstantValue.RESULT);
                                saveItemResultApi.setParams(regulateResultBeans);
                                addTaskBean.setState1("1");
                                taskBeanDao.update(addTaskBean);
                                return null;
                            } catch (Exception e) {
                                dissmisPD();
                                return e;
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Exception>() {
                        @Override
                        public void call(Exception o) {
                            if (null != o) {
                                Toast.makeText(CheckTableSelect2Activity.this, o.getMessage(), Toast.LENGTH_LONG).show();
                                return;
                            }
                            manager.doHttpDeal(saveItemResultApi);
                        }
                    });
        } else if (method.equals(saveItemResultApi.getMethod())) {
            clear();
            addTaskBean.setState2("1");
            taskBeanDao.update(addTaskBean);
            intentToNext();

        }
    }

    @Override
    public void onError(ApiException e, String method) {
        if (method.equals(getTableAndItem.getMethod())) {
            Toast.makeText(CheckTableSelect2Activity.this, "获取检查内容失败，请在有网络的环境同步数据", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CheckTableSelect2Activity.this, SiteActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        clear();
        intentToNext();
    }


    private void intentToNext() {
        Intent intent = new Intent(CheckTableSelect2Activity.this, RegulateItemCheckActivity.class);
        intent.putExtra(ConstantValue.KEY_TASK_ID, addTaskBean.getId());
        intent.putExtra(ConstantValue.KEY_TABLE_ID, addTaskBean.getTableId());
        intent.putExtra(ConstantValue.KEY_OBJECT_ID, addTaskBean.getEntid());
        intent.putExtra(ConstantValue.KEY_OBJECT_BEAN, object);
        dissmisPD();
        startActivityForResult(intent, ConstantValue.REQUEST_CODE_SITE_CHECK);
        finish();

    }

    /**
     * 向服务器请求，新增一个检查任务
     */
    private TaskBean addTaskBean;

    private void addTask() {
//        MyApplication app = (MyApplication) getApplication();
        addTaskBean = new TaskBean();
        addTaskBean.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        addTaskBean.setEntid(object.getId());
        addTaskBean.setTableId(tableId);
        addTaskBean.setOisuper(userExtId);
        addTaskBean.setRegulatorName(SharedPreferencesHelper.getInstance(this).getSharedPreference("userName", "").toString());
        addTaskBean.setEntname(object.getName());
        addTaskBean.setCreateDateLocal(TextUtil.date());
        addTaskBean.setEntcredit(object.getEntcredit());
        addTaskBean.setEntregion(object.getEntregion());
        addTaskBean.setEnttype(object.getNature());
        addTaskBean.setInsptable(tableId);
        addTaskBean.setEntname(object.getName());
        addTaskBean.setUpdateBy(userExtId);
        addTaskBean.setCreateBy(userExtId);
        addTaskBean.setInspstatus("1");
        addTaskBean.setInspstart(TextUtil.date2());
        addTaskBean.setState1("0");
        addTaskBean.setState2("0");
        addTaskBean.setState2_1("0");
        addTaskBean.setState3("0");
        addTaskBean.setState4("0");
        addTaskBean.setState5("0");
        addTaskBean.setState6("0");
        addTaskBean.setInsploc(MyApplication.getInstance().getLongtitude() + "," + MyApplication.getInstance().getLatitude());
        addTaskApi.setParams(addTaskBean);
        taskBeanDao.insert(addTaskBean);

        regulateResultBeans.clear();
        for (CheckItemEntity entity : selectedItemList) {
            if (!entity.getIsSelected()) {
                continue;
            }
            tableId = entity.getTableid();
            RegulateResultBean regulateResultBean = new RegulateResultBean();
            regulateResultBean.setInspid(addTaskBean.getId());
            regulateResultBean.setCreateBy(userExtId);
            regulateResultBean.setUpdateBy(userExtId);
            regulateResultBean.setItemid(entity.getId());
            regulateResultBean.setInsptable(entity.getTableid());
            regulateResultBean.setOisuper(userExtId);
//            regulateResultBean.setStatus("0");
            regulateResultBean.setItemcontent(entity.getContent());
            regulateResultBeans.add(regulateResultBean);
        }
        resultBeanDao.saveInTx(regulateResultBeans);

        manager.doHttpDeal(addTaskApi);

        object.setInspstatus("1");
        object.setOisuper(userExtId);
        object.setOisupername(SharedPreferencesHelper.getInstance(this).getSharedPreference("userName", "").toString());
        regulateObjectBeanDao.update(object);
    }


    /**
     * 全选
     *
     * @param groupPosition
     */
    @Override
    public void selectAll(int groupPosition) {
        boolean selectAll = mCheckTableBeanList.get(groupPosition).isAllSected();
        if (selectAll) {//如果状态已经是全选，则取消
            tableId = "";
            mCheckTableBeanList.get(groupPosition).setAllSected(false);
            mCheckTableBeanList.get(groupPosition).setSelected(false);
            setItemsSelected(mCheckTableBeanList.get(groupPosition).getCheckItemBeans(), false);
        } else {

            for (CheckTableEntity e : mCheckTableBeanList) {
                if (e.isSelected()) {
                    e.setExpanded(false);
                    e.setSelected(false);
                    e.setAllSected(false);
                    setItemsSelected(e.getCheckItemBeans(), false);
                    break;
                }
            }
            //全选
            tableId = mCheckTableBeanList.get(groupPosition).getId();
            mCheckTableBeanList.get(groupPosition).setAllSected(true);
            mCheckTableBeanList.get(groupPosition).setSelected(true);
            setItemsSelected(mCheckTableBeanList.get(groupPosition).getCheckItemBeans(), true);
        }
        mAdpter.notifyDataSetChanged();
    }


    private void setItemsSelected(List<CheckItemEntity> items, boolean selected) {
        if (null != items) {
            selectedItemList.clear();
            for (CheckItemEntity item : items) {
                item.setIsSelected(selected);
            }
            if (selected) {
                selectedItemList.addAll(items);
            }
        }
    }

    private boolean isItemAllUnselected(List<CheckItemEntity> items) {
        if (null == items || items.isEmpty()) {
            return false;
        }
        for (CheckItemEntity item : items) {
            if (item.getIsSelected()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CheckTableSelect2Activity.this, SiteActivity.class);
        startActivity(intent);
        clear();
        finish();
    }

    private void clear() {
        for (int i = 0; i < mCheckTableBeanList.size(); i++) {
            CheckTableEntity entity = mCheckTableBeanList.get(i);
            if (entity.isSelected()) {
                listView.collapseGroup(i);
                entity.setSelected(false);
                entity.setExpanded(false);
                for (CheckItemEntity entity1 : entity.getCheckItemBeans()) {
                    entity1.setIsSelected(false);
                }
                mAdpter.notifyDataSetChanged();
                break;
            }
        }
        selectedItemList.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ConstantValue.REQUEST_CODE_SITE_CHECK) {
//            Intent intent = new Intent();
//            intent.putExtra(ConstantValue.KEY_OBJECT_ID, data.getStringExtra(ConstantValue.KEY_OBJECT_ID));
//            CheckTableSelect2Activity.this.setResult(RESULT_OK, intent);
//            finish();
        } else if (resultCode == RESULT_CANCELED && requestCode == ConstantValue.REQUEST_CODE_SITE_CHECK) {
            CheckTableSelect2Activity.this.setResult(RESULT_CANCELED);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
