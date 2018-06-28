package com.wxx.unionpay.util;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：com.wxx.unionpay.util
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class Utils {

    /**
     * 随机字符串
     *
     * @param length
     * @return
     */
    public static String Random(int length) {
        char[] ss = new char[length];
        int i = 0;
        while (i < length) {
//            int f = (int) (Math.random() * 5);
//            if (f == 0)
//                ss[i] = (char) ('A' + Math.random() * 26);
//            else if (f == 1)
//                ss[i] = (char) ('a' + Math.random() * 26);
//            else
                ss[i] = (char) ('0' + Math.random() * 10);
            i++;
        }
        return new String(ss);
    }

}
