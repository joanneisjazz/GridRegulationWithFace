package com.rxretrofitlibrary.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.downlaod.DownInfo;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.AddTaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckItemEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckTableEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CityEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.EnterpriseEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.FileEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulatedObjectEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.TaskBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.UserBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.cookie.CookieResulte;

import com.rxretrofitlibrary.greendao.DownInfoDao;
import com.rxretrofitlibrary.greendao.AddTaskBeanDao;
import com.rxretrofitlibrary.greendao.CheckItemEntityDao;
import com.rxretrofitlibrary.greendao.CheckTableEntityDao;
import com.rxretrofitlibrary.greendao.CityEntityDao;
import com.rxretrofitlibrary.greendao.EnterpriseEntityDao;
import com.rxretrofitlibrary.greendao.FileEntityDao;
import com.rxretrofitlibrary.greendao.RegulatedObjectEntityDao;
import com.rxretrofitlibrary.greendao.RegulateObjectBeanDao;
import com.rxretrofitlibrary.greendao.RegulateResultBeanDao;
import com.rxretrofitlibrary.greendao.TaskBeanDao;
import com.rxretrofitlibrary.greendao.UserBeanDao;
import com.rxretrofitlibrary.greendao.CookieResulteDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig downInfoDaoConfig;
    private final DaoConfig addTaskBeanDaoConfig;
    private final DaoConfig checkItemEntityDaoConfig;
    private final DaoConfig checkTableEntityDaoConfig;
    private final DaoConfig cityEntityDaoConfig;
    private final DaoConfig enterpriseEntityDaoConfig;
    private final DaoConfig fileEntityDaoConfig;
    private final DaoConfig regulatedObjectEntityDaoConfig;
    private final DaoConfig regulateObjectBeanDaoConfig;
    private final DaoConfig regulateResultBeanDaoConfig;
    private final DaoConfig taskBeanDaoConfig;
    private final DaoConfig userBeanDaoConfig;
    private final DaoConfig cookieResulteDaoConfig;

    private final DownInfoDao downInfoDao;
    private final AddTaskBeanDao addTaskBeanDao;
    private final CheckItemEntityDao checkItemEntityDao;
    private final CheckTableEntityDao checkTableEntityDao;
    private final CityEntityDao cityEntityDao;
    private final EnterpriseEntityDao enterpriseEntityDao;
    private final FileEntityDao fileEntityDao;
    private final RegulatedObjectEntityDao regulatedObjectEntityDao;
    private final RegulateObjectBeanDao regulateObjectBeanDao;
    private final RegulateResultBeanDao regulateResultBeanDao;
    private final TaskBeanDao taskBeanDao;
    private final UserBeanDao userBeanDao;
    private final CookieResulteDao cookieResulteDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        downInfoDaoConfig = daoConfigMap.get(DownInfoDao.class).clone();
        downInfoDaoConfig.initIdentityScope(type);

        addTaskBeanDaoConfig = daoConfigMap.get(AddTaskBeanDao.class).clone();
        addTaskBeanDaoConfig.initIdentityScope(type);

        checkItemEntityDaoConfig = daoConfigMap.get(CheckItemEntityDao.class).clone();
        checkItemEntityDaoConfig.initIdentityScope(type);

        checkTableEntityDaoConfig = daoConfigMap.get(CheckTableEntityDao.class).clone();
        checkTableEntityDaoConfig.initIdentityScope(type);

        cityEntityDaoConfig = daoConfigMap.get(CityEntityDao.class).clone();
        cityEntityDaoConfig.initIdentityScope(type);

        enterpriseEntityDaoConfig = daoConfigMap.get(EnterpriseEntityDao.class).clone();
        enterpriseEntityDaoConfig.initIdentityScope(type);

        fileEntityDaoConfig = daoConfigMap.get(FileEntityDao.class).clone();
        fileEntityDaoConfig.initIdentityScope(type);

        regulatedObjectEntityDaoConfig = daoConfigMap.get(RegulatedObjectEntityDao.class).clone();
        regulatedObjectEntityDaoConfig.initIdentityScope(type);

        regulateObjectBeanDaoConfig = daoConfigMap.get(RegulateObjectBeanDao.class).clone();
        regulateObjectBeanDaoConfig.initIdentityScope(type);

        regulateResultBeanDaoConfig = daoConfigMap.get(RegulateResultBeanDao.class).clone();
        regulateResultBeanDaoConfig.initIdentityScope(type);

        taskBeanDaoConfig = daoConfigMap.get(TaskBeanDao.class).clone();
        taskBeanDaoConfig.initIdentityScope(type);

        userBeanDaoConfig = daoConfigMap.get(UserBeanDao.class).clone();
        userBeanDaoConfig.initIdentityScope(type);

        cookieResulteDaoConfig = daoConfigMap.get(CookieResulteDao.class).clone();
        cookieResulteDaoConfig.initIdentityScope(type);

        downInfoDao = new DownInfoDao(downInfoDaoConfig, this);
        addTaskBeanDao = new AddTaskBeanDao(addTaskBeanDaoConfig, this);
        checkItemEntityDao = new CheckItemEntityDao(checkItemEntityDaoConfig, this);
        checkTableEntityDao = new CheckTableEntityDao(checkTableEntityDaoConfig, this);
        cityEntityDao = new CityEntityDao(cityEntityDaoConfig, this);
        enterpriseEntityDao = new EnterpriseEntityDao(enterpriseEntityDaoConfig, this);
        fileEntityDao = new FileEntityDao(fileEntityDaoConfig, this);
        regulatedObjectEntityDao = new RegulatedObjectEntityDao(regulatedObjectEntityDaoConfig, this);
        regulateObjectBeanDao = new RegulateObjectBeanDao(regulateObjectBeanDaoConfig, this);
        regulateResultBeanDao = new RegulateResultBeanDao(regulateResultBeanDaoConfig, this);
        taskBeanDao = new TaskBeanDao(taskBeanDaoConfig, this);
        userBeanDao = new UserBeanDao(userBeanDaoConfig, this);
        cookieResulteDao = new CookieResulteDao(cookieResulteDaoConfig, this);

        registerDao(DownInfo.class, downInfoDao);
        registerDao(AddTaskBean.class, addTaskBeanDao);
        registerDao(CheckItemEntity.class, checkItemEntityDao);
        registerDao(CheckTableEntity.class, checkTableEntityDao);
        registerDao(CityEntity.class, cityEntityDao);
        registerDao(EnterpriseEntity.class, enterpriseEntityDao);
        registerDao(FileEntity.class, fileEntityDao);
        registerDao(RegulatedObjectEntity.class, regulatedObjectEntityDao);
        registerDao(RegulateObjectBean.class, regulateObjectBeanDao);
        registerDao(RegulateResultBean.class, regulateResultBeanDao);
        registerDao(TaskBean.class, taskBeanDao);
        registerDao(UserBean.class, userBeanDao);
        registerDao(CookieResulte.class, cookieResulteDao);
    }
    
    public void clear() {
        downInfoDaoConfig.clearIdentityScope();
        addTaskBeanDaoConfig.clearIdentityScope();
        checkItemEntityDaoConfig.clearIdentityScope();
        checkTableEntityDaoConfig.clearIdentityScope();
        cityEntityDaoConfig.clearIdentityScope();
        enterpriseEntityDaoConfig.clearIdentityScope();
        fileEntityDaoConfig.clearIdentityScope();
        regulatedObjectEntityDaoConfig.clearIdentityScope();
        regulateObjectBeanDaoConfig.clearIdentityScope();
        regulateResultBeanDaoConfig.clearIdentityScope();
        taskBeanDaoConfig.clearIdentityScope();
        userBeanDaoConfig.clearIdentityScope();
        cookieResulteDaoConfig.clearIdentityScope();
    }

    public DownInfoDao getDownInfoDao() {
        return downInfoDao;
    }

    public AddTaskBeanDao getAddTaskBeanDao() {
        return addTaskBeanDao;
    }

    public CheckItemEntityDao getCheckItemEntityDao() {
        return checkItemEntityDao;
    }

    public CheckTableEntityDao getCheckTableEntityDao() {
        return checkTableEntityDao;
    }

    public CityEntityDao getCityEntityDao() {
        return cityEntityDao;
    }

    public EnterpriseEntityDao getEnterpriseEntityDao() {
        return enterpriseEntityDao;
    }

    public FileEntityDao getFileEntityDao() {
        return fileEntityDao;
    }

    public RegulatedObjectEntityDao getRegulatedObjectEntityDao() {
        return regulatedObjectEntityDao;
    }

    public RegulateObjectBeanDao getRegulateObjectBeanDao() {
        return regulateObjectBeanDao;
    }

    public RegulateResultBeanDao getRegulateResultBeanDao() {
        return regulateResultBeanDao;
    }

    public TaskBeanDao getTaskBeanDao() {
        return taskBeanDao;
    }

    public UserBeanDao getUserBeanDao() {
        return userBeanDao;
    }

    public CookieResulteDao getCookieResulteDao() {
        return cookieResulteDao;
    }

}