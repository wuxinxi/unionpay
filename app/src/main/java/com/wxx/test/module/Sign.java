package com.wxx.test.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.field.Iso8583FieldType;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.util.ThreeDes;

import java.nio.charset.Charset;
import java.util.Arrays;

import static com.wxx.test.base.Config.dataHeader;
import static com.wxx.unionpay.util.HexUtil.bytesToHexString;
import static com.wxx.unionpay.util.HexUtil.hex2byte;
import static com.wxx.unionpay.util.HexUtil.mergeByte;
import static com.wxx.unionpay.util.HexUtil.str2Bcd;
import static java.lang.System.arraycopy;

/**
 * 作者：Tangren on 2018-06-29
 * 包名：com.wxx.test.sign
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class Sign {
    private volatile static Sign instance = null;

    private Sign() {
    }

    public static Sign getInstance() {
        if (instance == null) {
            synchronized (Sign.class) {
                if (instance == null) {
                    instance = new Sign();
                }
            }
        }
        return instance;
    }


    private Iso8583MessageFactory signFactory() {
        Iso8583MessageFactory facotry = new Iso8583MessageFactory(2, false, Charset.forName("UTF-8"), dataHeader());
        facotry.set(11, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(41, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 8))
                .set(42, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 15))
                .set(60, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
                .set(63, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_CHAR, 0));
        return facotry;
    }


    public Iso8583Message message() {
        Iso8583Message message = new Iso8583Message(signFactory());
        message.setTpdu(UnionPayApp.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(11, String.format("%06d", UnionPayApp.getPosManager().getTradeSeq()))
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(60, bytesToHexString(sign_field_60()))
                .setValue(63, "099");
        MLog.d("message(TestActivity.java:84)" + message.getBytesString());
        return message;
    }


    /**
     * @return 签到
     */
    private byte[] sign_field_60() {
        byte[] field_60_1 = str2Bcd("00");
        byte[] field_60_2 = str2Bcd(UnionPayApp.getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd("003", 1);
        return mergeByte(field_60_1, field_60_2, field_60_3);
    }

    public void setMacKey(String value) {
        byte[] field_60_data = hex2byte(value);
        int i = 0;
        byte[] pinKey = new byte[16];
        arraycopy(field_60_data, i, pinKey, 0, pinKey.length);
        MLog.d("setKey(SignBean.java:256)pin 密文：" + bytesToHexString(pinKey));

        byte[] pinKeyCrc = new byte[4];
        arraycopy(field_60_data, i += pinKey.length, pinKeyCrc, 0, pinKeyCrc.length);
        MLog.d("setKey(SignBean.java:260)pin checkValue=" + bytesToHexString(pinKeyCrc));

        byte[] macKey = new byte[16];
        arraycopy(field_60_data, i += pinKeyCrc.length, macKey, 0, macKey.length);
        MLog.d("setKey(SignBean.java:264)mac 密文" + bytesToHexString(macKey));

        byte[] macKeyCrc = new byte[4];
        arraycopy(field_60_data, i + macKey.length, macKeyCrc, 0, macKeyCrc.length);
        MLog.d("setKey(SignBean.java:268)Mac checkValue=" + bytesToHexString(macKeyCrc));

        String key = UnionPayApp.getPosManager().getKey();
        byte[] des = ThreeDes.Union3DesDecrypt(hex2byte(key), macKey);

        String macKeyHex = bytesToHexString(des);
        String Mac = macKeyHex.substring(0, 16);
        byte[] crc = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        byte[] crcdata = ThreeDes.encrypt(crc, Mac);

        if (crcdata != null) {
            byte[] macCrc = new byte[4];
            arraycopy(crcdata, 0, macCrc, 0, macCrc.length);
            if (Arrays.equals(macCrc, macKeyCrc)) {
                UnionPayApp.getPosManager().setMacKey(macKeyHex);
                MLog.d("setKey(SignBean.java:292)MAC正确");

            }
        }
    }
}
