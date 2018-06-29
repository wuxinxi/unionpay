package com.wxx.unionpay.db.manager;

import com.wxx.unionpay.db.dao.ICParams_380Dao;
import com.wxx.unionpay.db.dao.Public_370Dao;
import com.wxx.unionpay.entity.ICParams_380;
import com.wxx.unionpay.entity.Public_370;
import com.wxx.unionpay.log.MLog;

/**
 * 作者：Tangren on 2018-06-29
 * 包名：com.wxx.unionpay.db.manager
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class DBManager {

    /**
     * 保存IC卡交易参数
     *
     * @param var aid
     */
    public static void save_ic_params(String var) {
        ICParams_380Dao icParams_380Dao = DBCore.getDaoSession().getICParams_380Dao();
        ICParams_380 params = new ICParams_380();
        params.setIcParam(var);
        icParams_380Dao.insertOrReplaceInTx(params);
    }

    /**
     * 保存IC卡交易Key
     *
     * @param var aid
     */
    public static void save_ic_public_key(String var) {
        Public_370Dao dao = DBCore.getDaoSession().getPublic_370Dao();
        Public_370 params = new Public_370();
        params.setPublicKey(var);
        dao.insertOrReplaceInTx(params);
        MLog.d("save_ic_public_key(DBManager.java:39)保存公钥成功");
    }
}
