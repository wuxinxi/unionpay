package com.wxx.unionpay.db.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.wxx.unionpay.UnionPayApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.MODE_MULTI_PROCESS;

/**
 * 作者: Tangren on 2017/7/8
 * 包名：com.szxb.onlinbus.db.sp
 * 邮箱：996489865@qq.com
 * TODO:保存秘钥/获取保存后的
 */

public class CommonSharedPreferences {

    public static final String FILE_NAME = "XB_BASE_PARAMS";

    public static void put(String key, Object value) {

        SharedPreferences sp = UnionPayApp.getInstance().getSharedPreferences(FILE_NAME, MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();

        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long)
            editor.putLong(key, (Long) value);
        else if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else editor.putString(key, (String) value);

        SharedPreferencesCompat.apply(editor);
    }


    public static Object get(String key, Object defaultValue) {
        SharedPreferences sp = UnionPayApp.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultValue instanceof Boolean)
            return sp.getBoolean(key, (Boolean) defaultValue);
        else if (defaultValue instanceof Long)
            return sp.getLong(key, (Long) defaultValue);
        else if (defaultValue instanceof Integer)
            return sp.getInt(key, (Integer) defaultValue);
        else return sp.getString(key, (String) defaultValue);
    }


    private static class SharedPreferencesCompat {

        private static final Method method = findApplyMethod();

        private static Method findApplyMethod() {
            Class clz = SharedPreferences.Editor.class;
            try {
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (method != null) {
                    method.invoke(editor);
                    return;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }

    }

}
