package com.wxx.test;

import android.util.Log;

import com.szxb.jni.libszxb;
import com.wxx.unionpay.socket.RxSocket;

/**
 * 作者：Tangren on 2018-09-10
 * 包名：com.wxx.test
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class LoopScan extends Thread {
    private RxSocket socket;

    public LoopScan(RxSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        byte[] recs = new byte[1024];
        int barcode = libszxb.getBarcode(recs);
        if (barcode < 0) {
            return;
        }
        String result = new String(recs, 0, barcode);
        Log.d("BankQr",
                "run(LoopScan.java:24)" + result);
//        Iso8583Message iso8583Message = BankQr.getInstance().qrPayMessageData(result);
//        socket.exeSSL(ELSE, iso8583Message.getBytes());

    }
}
