package com.wxx.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.field.Iso8583DataHeader;
import com.szxb.java8583.field.Iso8583FieldType;
import com.szxb.java8583.quickstart.SingletonFactory;
import com.szxb.java8583.quickstart.special.SpecialField62;
import com.wxx.unionpay.R;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.field.Field_60;
import com.wxx.unionpay.interfaces.OnCallback;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.socket.SocketUtil;
import com.wxx.unionpay.util.HexUtil;

import java.nio.charset.Charset;

/**
 * 作者：Tangren on 2018-06-28
 * 包名：com.wxx.test
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class TestActivity extends AppCompatActivity implements OnCallback {
    SocketUtil socket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        socket = new SocketUtil();
        socket.setCallback(this);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.setData(message().getBytes());
                socket.exe();
            }
        });
    }

    public static Iso8583DataHeader dataHeader() {
        //定义报文头数据格式
        return new Iso8583DataHeader(
                // tpdu		BCD编码，5个字节（10个字符长度）
                new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 10),
                // header	BCD编码，6个字节长度
                new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 12),
                // mti		BCD编码，2个字节长度
                new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 4),
                // bitmap	BCD编码，8个字节长度
                new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 16)
        );
    }

    public static Iso8583MessageFactory signFactory() {
        Iso8583MessageFactory facotry = new Iso8583MessageFactory(2, false, Charset.forName("UTF-8"), dataHeader());
        facotry.set(11, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(41, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 8))
                .set(42, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 15))
                .set(60, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
                .set(63, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_CHAR, 0));
        return facotry;
    }


    public static Iso8583Message message() {
        Iso8583Message message = new Iso8583Message(signFactory());
        message.setTpdu("6003030000")
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(11, String.format("%06d", UnionPayApp.getPosManager().getTradeSeq()))
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(60, HexUtil.bytesToHexString(Field_60.sign_field_602()))
                .setValue(63, "099");
        MLog.d("message(TestActivity.java:84)" + message.getBytesString());
        return message;
    }


    @Override
    public void onCallBack(byte[] data) {
        Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
        factory.setSpecialFieldHandle(62, new SpecialField62());
        Iso8583Message message0810 = factory.parse(data);

        MLog.d("onCallBack(TestActivity.java:90)原始数据:" + HexUtil.bytesToHexString(data));
        MLog.d("onCallBack(TestActivity.java:99)解析后的数据:"+ message0810.toFormatString());
    }
}
