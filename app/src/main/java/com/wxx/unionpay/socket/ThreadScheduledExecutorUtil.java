package com.wxx.unionpay.socket;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 作者: Tangren on 2017/8/15
 * 包名：com.szxb.buspay.task
 * 邮箱：996489865@qq.com
 * TODO:单利任务调度线程池
 */

public class ThreadScheduledExecutorUtil {

    private static volatile ThreadScheduledExecutorUtil mThreadScheduledExecutorUtil = null;

    private volatile static ScheduledThreadPoolExecutor executor;

    private ThreadScheduledExecutorUtil() {
        executor = new ScheduledThreadPoolExecutor(5);
    }

    public static ThreadScheduledExecutorUtil getInstance() {
        if (mThreadScheduledExecutorUtil == null) {
            synchronized (ThreadScheduledExecutorUtil.class) {
                if (mThreadScheduledExecutorUtil == null) {
                    mThreadScheduledExecutorUtil = new ThreadScheduledExecutorUtil();
                }
            }
        }
        return mThreadScheduledExecutorUtil;
    }

    public ScheduledExecutorService getService() {
        return executor;
    }

    public static void shutdown() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
