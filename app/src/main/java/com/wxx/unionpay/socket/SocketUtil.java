package com.wxx.unionpay.socket;

import com.wxx.unionpay.interfaces.OnCallback;
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

public class SocketUtil {

    private OnCallback callback;
    private byte[] data;
    private Socket socket;
    private OutputStream outStram;
    private InputStream stream;

    public SocketUtil() {
    }

    public void exe() {
        ThreadScheduledExecutorUtil.getInstance().getService().submit(new WorkThread());
    }

    private class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                socket = new Socket("183.237.71.61", 22102);
                boolean connected = socket.isConnected();

                MLog.d("run(WorkThread.java:38)是否连接成功:" + connected);

                outStram = socket.getOutputStream();
                outStram.write(getData());
                outStram.flush();

                stream = socket.getInputStream();
                byte[] length = new byte[2];
                int lenRead = stream.read(length);

                int len = HexUtil.byteHex2Int(length);
                byte[] data = new byte[len];
                int dataRead = stream.read(data);

                byte[] lenAndData = HexUtil.mergeByte(length, data);

                MLog.d("run(WorkThread.java:52)响应:" + HexUtil.bytesToHexString(lenAndData));
                if (callback != null) {
                    callback.onCallBack(lenAndData);
                }

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

        }
    }


    public void setCallback(OnCallback callback) {
        this.callback = callback;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
