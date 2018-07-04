package com.wxx.unionpay.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 作者：Tangren on 2018-07-02
 * 包名：com.wxx.unionpay.util
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class MyToast {

    private static Toast mToast = null;

    public static void showToast(final Context context, final String text) {
        MainLooper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                } else {
                    Log.d("MyToast",
                            "showToast(MyToast.java:19)"+text);
                    mToast.setText(text);
                    mToast.setDuration(Toast.LENGTH_SHORT);
                }
                mToast.show();
            }
        });
    }
}
