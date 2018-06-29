package com.wxx.test;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.quickstart.SingletonFactory;
import com.szxb.java8583.quickstart.special.SpecialField62;
import com.wxx.test.module.Down;
import com.wxx.test.module.PosTransaction;
import com.wxx.test.module.Sign;
import com.wxx.unionpay.R;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.db.manager.DBManager;
import com.wxx.unionpay.interfaces.OnCallback;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.socket.SocketUtil;

import static com.wxx.unionpay.util.HexUtil.bytesToHexString;

/**
 * 作者：Tangren on 2018-06-28
 * 包名：com.wxx.test
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class TestActivity extends AppCompatActivity implements OnCallback, View.OnClickListener {
    SocketUtil socket;
    Button signButton;
    Button queryButton;
    Button downAIDButton;

    Button public_query;
    Button public_down;
    Button payButton;

    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        socket = new SocketUtil();
        socket.setCallback(this);
        signButton = findViewById(R.id.sign);
        queryButton = findViewById(R.id.query);
        downAIDButton = findViewById(R.id.downAID);
        public_query = findViewById(R.id.public_query);
        public_down = findViewById(R.id.public_down);
        payButton = findViewById(R.id.pay);

        signButton.setOnClickListener(this);
        queryButton.setOnClickListener(this);
        downAIDButton.setOnClickListener(this);
        public_query.setOnClickListener(this);
        public_down.setOnClickListener(this);
        payButton.setOnClickListener(this);
    }


    @Override
    public void onCallBack(byte[] data) {
        Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
        factory.setSpecialFieldHandle(62, new SpecialField62());
        Iso8583Message message0810 = factory.parse(data);


        MLog.d("onCallBack(TestActivity.java:90)原始数据:" + bytesToHexString(data));
        MLog.d("onCallBack(TestActivity.java:99)解析后的数据:" + message0810.toFormatString());

        if (id == R.id.sign) {
            //签到返回
            String batchNum = message0810.getValue(60).getValue().substring(2, 8);
            UnionPayApp.getPosManager().setBatchNum(batchNum);
            Sign.getInstance().setMacKey(message0810.getValue(62).getValue());
        } else if (id == R.id.query) {
            Down.getInstance().setParmaInfo(message0810.getValue(62).getValue());
        } else if (id == R.id.downAID) {
            DBManager.save_ic_params(message0810.getValue(62).getValue());
        } else if (id == R.id.public_query) {
            Down.getInstance().setQueryIndex(message0810.getValue(62).getValue());
        } else if (id == R.id.public_down) {
            DBManager.save_ic_public_key(message0810.getValue(62).getValue());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign:
                id = R.id.sign;
                socket.setData(Sign.getInstance().message().getBytes());
                socket.exe();
                break;
            case R.id.query:
                id = R.id.query;
                socket.setData(Down.getInstance().message().getBytes());
                socket.exe();
                break;
            case R.id.downAID:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        id = R.id.downAID;
                        String adiList = UnionPayApp.getPosManager().aidIndexList();
                        MLog.d("onClick(TestActivity.java:91)" + adiList);
                        String macs[] = adiList.split(",");
                        for (String mac : macs) {
                            socket.setData(Down.getInstance().messageAID(mac).getBytes());
                            socket.exe();
                            SystemClock.sleep(2000);
                        }
                        MLog.d("run(TestActivity.java:102)AID下载结束");
                        id = 0;
                        socket.setData(Down.getInstance().messageAIDEnd().getBytes());
                        socket.exe();

                    }
                }).start();

                break;
            case R.id.public_query:
                id = R.id.public_query;
                socket.setData(Down.getInstance().messagePublicQuery().getBytes());
                socket.exe();
                break;
            case R.id.public_down:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        id = R.id.public_down;
                        String pubIndex = UnionPayApp.getPosManager().publicIndexList();
                        MLog.d("onClick(TestActivity.java:91)" + pubIndex);
                        String publics[] = pubIndex.split(",");
                        for (String pub : publics) {
                            socket.setData(Down.getInstance().messageDownPublic(pub).getBytes());
                            socket.exe();
                            SystemClock.sleep(2000);
                        }
                        MLog.d("run(TestActivity.java:102)公钥下载结束");
                        id = 0;
                        socket.setData(Down.getInstance().messageDownPublicEnd().getBytes());
                        socket.exe();
                    }
                }).start();
                break;
            case R.id.pay:
                socket.setData(PosTransaction.getInstance().payMessage("987654321","123456789").getBytes());
                socket.exe();
                break;
            default:

                break;
        }
    }
}
