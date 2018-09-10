package com.union;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.BankScanPay;
import com.szxb.java8583.module.SignIn;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.wxx.test.ExeType;
import com.wxx.test.module.PosRefund;
import com.wxx.unionpay.R;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.socket.RxSocket;
import com.wxx.unionpay.socket.SocketUtil;
import com.wxx.unionpay.util.HexUtil;

import static com.wxx.test.ExeType.ELSE;

/**
 * 作者：Tangren on 2018-07-05
 * 包名：com.union
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    Button signButton, payButton, qr;
    RxSocket socket;
    SocketUtil socketUtil = new SocketUtil();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signButton = (Button) findViewById(R.id.sign);
        payButton = (Button) findViewById(R.id.rx);
        qr = (Button) findViewById(R.id.qr);

        signButton.setOnClickListener(this);
        payButton.setOnClickListener(this);
        qr.setOnClickListener(this);

        socket = new RxSocket();

        PosManager manager = new PosManager();
        BusllPosManage.init(manager);

        BusllPosManage.getPosManager().setTradeSeq();
        Iso8583Message message = SignIn.getInstance().message(BusllPosManage.getPosManager().getTradeSeq());
        socket.exeSSL(ExeType.SIGN, message.getBytes());

//        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new LoopThread(socket), 2000, 200, TimeUnit.MILLISECONDS);
//        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new LoopScan(socket), 1, 3000, TimeUnit.SECONDS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign:
                MLog.d("onClick(DemoActivity.java:49)签到");
                UnionPayApp.getPosManager().setTradeSeq();
                Iso8583Message message = SignIn.getInstance().message(UnionPayApp.getPosManager().getTradeSeq());
                socket.exeSSL(ExeType.SIGN, message.getBytes());
                break;
            case R.id.rx:
                String cardNo = "6214837838429995";
                String cardNum = "01";
                int seq = 123;
                String batchNum = "000002";
//                String macKey="9476C162F18CA8C120D3BC2915F0897A";
                String macKey = BusllPosManage.getPosManager().getMacKey();
                Iso8583Message messageRef = PosRefund.getInstance().refun(cardNo, cardNum, seq, batchNum, macKey);
                socket.exeSSL(ELSE, messageRef.getBytes());
                break;
            case R.id.qr:
                UnionPayApp.getPosManager().setTradeSeq();
//                Iso8583Message iso8583Message = BankQr.getInstance().qrPayMessage("6223954368418986178");
                Iso8583Message iso8583Message = BankScanPay.getInstance().qrPayMessage("6223954368418986178", 1, UnionPayApp.getPosManager().getTradeSeq(), UnionPayApp.getPosManager().getMacKey());
                Log.d("DemoActivity",
                        "onClick(DemoActivity.java:81)" + iso8583Message.toFormatString());
                Log.d("DemoActivity",
                        "onClick(DemoActivity.java:86)>>>" + HexUtil.bytesToHexString(iso8583Message.getBytes()));
                socket.exeSSL(ELSE, iso8583Message.getBytes());
                break;
            default:

                break;
        }
    }
}
