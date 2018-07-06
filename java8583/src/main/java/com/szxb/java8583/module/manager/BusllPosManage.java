package com.szxb.java8583.module.manager;

/**
 * 作者：Tangren on 2018-07-05
 * 包名：com.szxb.java8583.module.manager
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class BusllPosManage {

    private static IManager posManager = null;

    public static void init(IManager manager) {
        posManager = manager;
        posManager.loadFromPrefs();
    }

    public static IManager getPosManager() {
        return posManager;
    }
}
