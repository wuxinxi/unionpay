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
import com.wxx.test.module.PosScanRefund2;
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

    Button signButton, payButton, qr,qr_refund;
    RxSocket socket;
    SocketUtil socketUtil = new SocketUtil();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signButton = (Button) findViewById(R.id.sign);
        payButton = (Button) findViewById(R.id.rx);
        qr = (Button) findViewById(R.id.qr);
        qr_refund = (Button) findViewById(R.id.qr_refund);

        signButton.setOnClickListener(this);
        payButton.setOnClickListener(this);
        qr.setOnClickListener(this);
        qr_refund.setOnClickListener(this);

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
                String cardNo = "6217921101024142";
                String cardNum = "01";
                int seq = 45;
                String batchNum = "000002";
//                String macKey="9476C162F18CA8C120D3BC2915F0897A";
                String macKey = BusllPosManage.getPosManager().getMacKey();
//                String cardNo, String cardNum, int seq, String batchNum, String reason,int refundAmount
//                Iso8583Message messageRef = PosRefund.getInstance().refund("",seq, batchNum, "00");
                Iso8583Message refund = com.szxb.java8583.module.PosRefund.getInstance().refund(cardNo, cardNum, seq, batchNum, "00", 1);
                socket.exeSSL(ELSE, refund.getBytes());
                break;
            case R.id.qr_refund:

                String cardNo2="6228450128033376679";
                int seq_qr_refund=38;
                String batchNum_qr_refund="000002";

//                Iso8583Message messageRef2 = PosRefund.getInstance()
//                        .refund(cardNo2,seq_qr_refund,batchNum_qr_refund,"00",1);
                Iso8583Message refun = PosScanRefund2.getInstance().refun(seq_qr_refund, "6226048465780222686","00",1);
                socket.exeSSL(ELSE, refun.getBytes());
                break;
            case R.id.qr:
                UnionPayApp.getPosManager().setTradeSeq();
//                Iso8583Message iso8583Message = BankQr.getInstance().qrPayMessage("6223954368418986178");
                Iso8583Message iso8583Message = BankScanPay.getInstance().qrPayMessage("6226048465780222686", 1, UnionPayApp.getPosManager().getTradeSeq(), UnionPayApp.getPosManager().getMacKey());
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
