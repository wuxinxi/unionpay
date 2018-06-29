package com.wxx.unionpay.db.manager;

import android.content.Context;

import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.db.dao.DaoMaster;
import com.wxx.unionpay.db.dao.DaoSession;

import org.greenrobot.greendao.async.AsyncSession;


/**
 * 作者：Tangren on 2017/6/13 17:22
 * 邮箱：wu_tangren@163.com
 * TODO:一句话描述
 */
public class DBCore {

    private static final String DEFAULT_DB_NAME = "xb_unionpay_.db";
    private static volatile DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static AsyncSession asyncSession;

    private static Context mContext;
    private static String DB_NAME;

    public static void init(Context context) {
        init(context, DEFAULT_DB_NAME);
    }

    public static void init(Context context, String dbName) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        mContext = context.getApplicationContext();
        DB_NAME = dbName;
    }

    /**
     * 获取DaoMaster
     * <p>
     * 判断是否存在数据库，如果没有则创建数据库
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (null == daoMaster) {
            synchronized (DaoMaster.class) {
                if (null == daoMaster) {
                    DBHelper helper = new DBHelper(context, DB_NAME);
                    daoMaster = new DaoMaster(helper.getWritableDatabase());
                }
            }
        }
        return daoMaster;
    }

    //同步操作
    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(UnionPayApp.getInstance());
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    //异步操作
    public static AsyncSession getASyncDaoSession() {
        if (asyncSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(UnionPayApp.getInstance());
            }
            asyncSession = daoMaster.newSession().startAsyncSession();
        }
        return asyncSession;
    }

}
