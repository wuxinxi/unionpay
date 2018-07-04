package com.wxx.unionpay.util;

import android.os.Handler;
import android.os.Looper;

/**
 * 作者：Tangren on 2018-07-02
 * 包名：com.wxx.unionpay.util
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class MainLooper extends Handler {

    private static MainLooper instance = new MainLooper(Looper.getMainLooper());

    protected MainLooper(Looper looper) {
        super(looper);
    }

    public static MainLooper getInstance() {
        return instance;
    }

    public static void runOnUiThread(Runnable runnable) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            runnable.run();
        } else {
            instance.post(runnable);
        }

    }
}
