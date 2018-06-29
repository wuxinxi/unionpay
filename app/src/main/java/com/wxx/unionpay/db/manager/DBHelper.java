package com.wxx.unionpay.db.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wxx.unionpay.db.dao.DaoMaster;
import com.wxx.unionpay.db.dao.ICParams_380Dao;
import com.wxx.unionpay.db.dao.Public_370Dao;
import com.wxx.unionpay.log.MLog;

import org.greenrobot.greendao.database.Database;


/**
 * 作者：Tangren_ on 2017/3/23 0023.
 * 邮箱：wu_tangren@163.com
 * TODO：更新数据库
 */


public class DBHelper extends DaoMaster.OpenHelper {
    DBHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MLog.d("onUpgrade(DBHelper.java:46)升级");
        update(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        super.onDowngrade(db, oldVersion, newVersion);
        MLog.d("onDowngrade(DBHelper.java:52)降级");
        update(db, oldVersion, newVersion);
    }

    private void update(SQLiteDatabase db, int oldVersion, int newVersion) {
        MLog.d("update(DBHelper.java:57))oldVersion=" + oldVersion + "newVersion=" + newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                }, ICParams_380Dao.class,
                Public_370Dao.class);
    }

}
