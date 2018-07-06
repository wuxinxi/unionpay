package com.wxx.unionpay.socket;

import android.util.Log;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.quickstart.SingletonFactory;
import com.szxb.java8583.quickstart.special.SpecialField62;
import com.wxx.test.ExeType;
import com.wxx.test.http.CallServer;
import com.wxx.test.http.HttpListener;
import com.wxx.test.module.SSLContextUtil;
import com.wxx.test.module.Sign;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.util.HexUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

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

    public byte[] exe(byte[] sendData) {
        MLog.d("run(RxSocket.java:26)" + HexUtil.bytesToHexString(sendData));
        byte[] lenAndData = null;
        try {
            socket = new Socket(UnionPayApp.getPosManager().getIP(), UnionPayApp.getPosManager().getPort());
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

    public byte[] exeSSL(final ExeType type, byte[] sendData) {
        if (sendData == null) {
            return null;
        }
        String url = "https://" + UnionPayApp.getPosManager().getIP() + ":" + UnionPayApp.getPosManager().getPort() + "/mjc/webtrans/VPB_lb";
        Request<byte[]> request = NoHttp.createByteArrayRequest(url, RequestMethod.POST);
        request.setHeader("User-Agent", "Donjin Http 0.1");
        request.setHeader("Cache-Control", "no-cache");
        request.setHeader("Accept", "*/*");
        request.setHeader("Accept-Encoding", "*");
        request.setHeader("Connection", "close");
        request.setHeader("HOST", "120.204.69.139:30000");

        InputStream stream = new ByteArrayInputStream(sendData);
        request.setDefineRequestBody(stream, "x-ISO-TPDU/x-auth");
        SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
        request.setHostnameVerifier(SSLContextUtil.getHostnameVerifier());
        if (sslContext != null) {
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            request.setSSLSocketFactory(socketFactory);
            CallServer.getHttpclient().add(0, request, new HttpListener<byte[]>() {
                @Override
                public void success(int what, Response<byte[]> response) {

                    Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
                    factory.setSpecialFieldHandle(62, new SpecialField62());
                    byte[] bytes = response.get();
                    MLog.d("success(RxSocket.java:113)" + HexUtil.bytesToHexString(bytes));
                    Iso8583Message message0810 = factory.parse(response.get());
                    Log.d("RxSocket",
                            "success(RxSocket.java:113)" + message0810.toFormatString());

                    if (type == ExeType.SIGN) {
                        String batchNum = message0810.getValue(60).getValue().substring(2, 8);
                        UnionPayApp.getPosManager().setBatchNum(batchNum);
                        BusllPosManage.getPosManager().setBatchNum(batchNum);
                        Sign.getInstance().setMacKey(message0810.getValue(62).getValue());
                    }
                }

                @Override
                public void fail(int what, String e) {
                    Log.d("RxSocket",
                            "fail(RxSocket.java:110)" + e);

                }
            });
        }
        return null;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
