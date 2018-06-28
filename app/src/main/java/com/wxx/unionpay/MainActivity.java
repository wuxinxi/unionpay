package com.wxx.unionpay;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wxx.unionpay.entity.bean.SignBean;
import com.wxx.unionpay.field.ComField;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.socket.SocketUtil;
import com.xuhao.android.libsocket.sdk.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.bean.IPulseSendable;
import com.xuhao.android.libsocket.sdk.bean.ISendable;
import com.xuhao.android.libsocket.sdk.bean.OriginalData;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.wxx.unionpay.util.HexUtil.bytesToHexString;

public class MainActivity extends AppCompatActivity {


    private IConnectionManager mManager;
    private OkSocketOptions mOkOptions;
    private ConnectionInfo mInfo;

    private SocketActionAdapter socketAdapter = new SocketActionAdapter() {
        @Override
        public void onSocketIOThreadStart(Context context, String action) {
            super.onSocketIOThreadStart(context, action);
            MLog.d("onSocketIOThreadStart(MainActivity.java:35)开始：" + action);
        }

        @Override
        public void onSocketIOThreadShutdown(Context context, String action, Exception e) {
            super.onSocketIOThreadShutdown(context, action, e);
            MLog.d("onSocketIOThreadShutdown(MainActivity.java:41)意外断开：" + e.toString());
        }

        @Override
        public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
            super.onSocketDisconnection(context, info, action, e);
            MLog.d("onSocketDisconnection(MainActivity.java:47)主动断开" + e.toString());
        }

        @Override
        public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
            super.onSocketConnectionSuccess(context, info, action);
            MLog.d("onSocketConnectionSuccess(MainActivity.java:53)连接成功");
        }

        @Override
        public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
            super.onSocketConnectionFailed(context, info, action, e);
            MLog.d("onSocketConnectionFailed(MainActivity.java:59)连接失败");
        }

        @Override
        public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(context, info, action, data);
            MLog.d("onSocketReadResponse(MainActivity.java:65)读取响应成功");
        }

        @Override
        public void onSocketWriteResponse(Context context, ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(context, info, action, data);
            MLog.d("onSocketWriteResponse(MainActivity.java:71)发送失败");
        }

        @Override
        public void onPulseSend(Context context, ConnectionInfo info, IPulseSendable data) {
            super.onPulseSend(context, info, data);
            MLog.d("onPulseSend(MainActivity.java:77)发送");
            byte[] sign = ComField.sign();
            String hexSign = bytesToHexString(sign);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button but = findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SocketUtil util = new SocketUtil();
                byte[] sign = ComField.sign();
                util.setData(sign);
                util.exe();
            }
        });
    }

    java.net.Socket socket;

    public void getNetWork() {
        try {
            socket = new java.net.Socket("183.237.71.61", 22102);
            socket.setSoTimeout(5000);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            byte[] sign = ComField.sign();
            String hexSign = bytesToHexString(sign);

            dos.write(sign);
            dos.flush();

            boolean conn = socket.isConnected();
            MLog.d("getNetWork(MainActivity.java:57)连接？" + conn);

            MLog.d("getNetWork(MainActivity.java:59)发送的数据:" + hexSign);

            byte[] len = new byte[2];
            dis.read(len);


            int length = Integer.parseInt(bytesToHexString(len), 16);
            byte[] data = new byte[length];
            dis.read(data);

            MLog.d("getNetWork(MainActivity.java:62)响应的数据:" + bytesToHexString(len) + bytesToHexString(data));
//            StateBean sta=new StateBean(data);
//            MLog.d("getNetWork(MainActivity.java:69)"+sta);
            SignBean signBean = new SignBean(data);
            if (signBean.getResCode().equals("00")) {
                UnionPayApp.getPosManager().setBatchNum(signBean.getBatchNum());
                MLog.d("getNetWork(MainActivity.java:70)签到成功，key=" + signBean.getPosKey());
            }
            MLog.d("getNetWork(MainActivity.java:69)" + signBean);

        } catch (IOException e) {
            e.printStackTrace();
            MLog.d("getNetWork(MainActivity.java:70)" + e.toString());
        }

        MLog.d("getNetWork(MainActivity.java:78)" + socket.isConnected());

    }

}
