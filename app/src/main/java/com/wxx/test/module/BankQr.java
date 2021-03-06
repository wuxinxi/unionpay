package com.wxx.test.module;

import android.util.Log;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.util.MacEcbUtils;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.util.HexUtil;

import static com.szxb.java8583.module.BaseFactory.qrPayFactory;
import static com.wxx.unionpay.util.HexUtil.bytesToHexString;
import static com.wxx.unionpay.util.HexUtil.mergeByte;
import static com.wxx.unionpay.util.HexUtil.str2Bcd;

/**
 * 作者：Tangren on 2018-09-10
 * 包名：com.wxx.test.module
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class BankQr {
    private volatile static BankQr instance = null;

    private BankQr() {
    }

    public static BankQr getInstance() {
        if (instance == null) {
            synchronized (BankQr.class) {
                if (instance == null) {
                    instance = new BankQr();
                }
            }
        }
        return instance;
    }


    public Iso8583Message qrPayMessage(String qrCode,int amount) {
        Iso8583Message iso8583Message = qrPayMessageData(qrCode,amount);

        byte[] dataAll = iso8583Message.getBytes();

        byte[] data = new byte[dataAll.length - 2];
        System.arraycopy(dataAll, 2, data, 0, data.length);

        iso8583Message.setTpdu(UnionPayApp.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setValue(64, pay_field_64(data));

        Log.d("BankQr",
                "qrPayMessage(BankQr.java:55)getBitmapString=" + iso8583Message.getBitmapString());
        return iso8583Message;
    }

    public Iso8583Message qrPayMessageData(String qrCode, int amount) {
        Iso8583Message message = new Iso8583Message(qrPayFactory());
        message.setMti("0200")
                .setValue(3, "000000")//交易处理码
                .setValue(4, String.format("%012d", amount))//交易金额
                .setValue(11, String.format("%06d", UnionPayApp.getPosManager().getTradeSeq()))//受卡方系统跟踪号,流水号
                .setValue(22, "032")//服务点输入方式码0x03,0x20
                .setValue(25, "00")//服务点条件码
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(49, "156")//交易货币代码
                .setValue(53, String.format("%016d", 0))//安全控制信息
                .setValue(59, new String(qr_filed_59(qrCode)))
                .setValue(60, bytesToHexString(pay_field_60()))
                .setValue(64, "");

        return message;
    }


    private byte[] qr_filed_59(String qrCode) {
        byte[] value_qr_code_byte = qrCode.getBytes();
        byte[] tag_byte = "A3".getBytes();
        byte[] len_byte = String.format("%03d", value_qr_code_byte.length).getBytes();
        byte[] data = mergeByte(tag_byte, len_byte, value_qr_code_byte);
        return data;
    }


    /**
     * @return 交易60
     */
    private byte[] pay_field_60() {
        byte[] field_60_1 = str2Bcd("22");
        byte[] field_60_2 = str2Bcd(UnionPayApp.getPosManager().getBatchNum());
        return mergeByte(field_60_1, field_60_2);
    }

    /**
     * @return 64域
     */
    private String pay_field_64(byte[] data) {
        byte[] mac = MacEcbUtils.getMac(HexUtil.hex2byte(UnionPayApp.getPosManager().getMacKey()), data);
        return HexUtil.bytesToHexString(mac);
    }

}

