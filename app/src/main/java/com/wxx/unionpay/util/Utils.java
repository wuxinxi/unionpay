package com.wxx.unionpay.util;

import com.wxx.unionpay.UnionPayApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    /**
     * @return 是否需要更新藏
     */
    public static boolean isUpdateParams() {
        long last = UnionPayApp.getPosManager().getLastUpdateTime();
        long current = System.currentTimeMillis() / 1000;
        //2018,5,1
        return current >= 1525142850L && current - last > 7 * 86400;
    }

    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd HHmmss", new Locale("zh", "CN"));

    /**
     * @param format 日期格式
     * @return 日期
     */
    public static String getCurrentDate(String format) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(format, new Locale("zh", "CN"));
            return fmt.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fmt.format(new Date());
    }
}
