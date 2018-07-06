package com.wxx.unionpay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wxx.unionpay.entity.bean.SignBean;
import com.wxx.unionpay.entity.bean.StateBean;
import com.wxx.unionpay.field.ComField;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.socket.SocketUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.wxx.unionpay.util.HexUtil.bytesToHexString;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button but = (Button) findViewById(R.id.sign);
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
            StateBean sta=new StateBean(data);
//            MLog.d("getNetWork(MainActivity.java:69)"+sta);
            SignBean signBean = new SignBean(data);
            if (signBean.getResCode().equals("00")) {
                UnionPayApp.getPosManager().setBatchNum(signBean.getBatchNum());
                MLog.d("getNetWork(MainActivity.java:70)签到成功，key=" + signBean.getPosKey()+",批次号:"+signBean.getBatchNum());
            }
            MLog.d("getNetWork(MainActivity.java:69)" + signBean);

        } catch (IOException e) {
            e.printStackTrace();
            MLog.d("getNetWork(MainActivity.java:70)" + e.toString());
        }

        MLog.d("getNetWork(MainActivity.java:78)" + socket.isConnected());

    }

}
