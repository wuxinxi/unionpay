package com.wxx.unionpay;

import android.app.Application;

import com.wxx.unionpay.db.manager.DBCore;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.manager.PosManager;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：com.wxx.unionpay
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class UnionPayApp extends Application {

    private volatile static UnionPayApp instance;

    private static PosManager posManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DBCore.init(this);
        MLog.setDebug(true);

        posManager = new PosManager();
        posManager.loadParams(false);


        NoHttp.initialize(InitializationConfig.newBuilder(this)
                .readTimeout(6000)
                .connectionTimeout(6000)
                .networkExecutor(new OkHttpNetworkExecutor())
                .build());

        Logger.setDebug(true);
    }

    public static UnionPayApp getInstance() {
        return instance;
    }

    public static PosManager getPosManager() {
        if (posManager == null) {
            posManager = new PosManager();
        }
        return posManager;
    }
}
