package com.wxx.unionpay.socket;

import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.util.HexUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 作者：Tangren on 2018-06-28
 * 包名：com.wxx.unionpay.socket
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class RxSocket {

    private byte[] data;
    private Socket socket;
    private OutputStream outStram;
    private InputStream stream;

    public byte[] exe(byte[]sendData) {
        MLog.d("run(RxSocket.java:26)"+HexUtil.bytesToHexString(sendData));
        byte[] lenAndData = null;
        try {
            socket = new Socket("183.237.71.61", 22102);
            boolean connected = socket.isConnected();
            socket.setSoTimeout(5000);

            MLog.d("run(WorkThread.java:38)是否连接成功:" + connected);

            outStram = socket.getOutputStream();
            outStram.write(sendData);
            outStram.flush();

            stream = socket.getInputStream();
            byte[] length = new byte[2];
            int lenRead = stream.read(length);

            int len = HexUtil.byteHex2Int(length);
            byte[] data = new byte[len];
            int dataRead = stream.read(data);

            lenAndData = HexUtil.mergeByte(length, data);
        } catch (IOException e) {
            e.printStackTrace();
            MLog.d("run(WorkThread.java:37)哇哦出现了异常." + e.toString());
        } finally {
            try {
                if (outStram != null) {
                    outStram.close();
                    outStram = null;
                }
                if (stream != null) {
                    stream.close();
                    stream = null;
                }

                if (socket != null) {
                    socket.close();
                    socket = null;
                }
                MLog.d("run(WorkThread.java:86)关闭成功");
            } catch (Exception e) {
                e.printStackTrace();
                MLog.d("run(WorkThread.java:89)断开连接出现异常." + e.toString());
            }
        }
        return lenAndData;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
