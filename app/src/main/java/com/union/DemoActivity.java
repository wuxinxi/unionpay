package com.union;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.SignIn;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.wxx.test.ExeType;
import com.wxx.test.LoopThread;
import com.wxx.unionpay.R;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.socket.RxSocket;
import com.wxx.unionpay.socket.ThreadScheduledExecutorUtil;

import java.util.concurrent.TimeUnit;

/**
 * 作者：Tangren on 2018-07-05
 * 包名：com.union
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    Button signButton, payButton;
    RxSocket socket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signButton = (Button) findViewById(R.id.sign);
        payButton = (Button) findViewById(R.id.rx);

        signButton.setOnClickListener(this);
        payButton.setOnClickListener(this);

        socket = new RxSocket();

        PosManager manager = new PosManager();
        BusllPosManage.init(manager);
        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new LoopThread(socket), 2000, 200, TimeUnit.MILLISECONDS);
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

                break;
            default:

                break;
        }
    }
}
