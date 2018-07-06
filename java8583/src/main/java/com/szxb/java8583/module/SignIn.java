package com.szxb.java8583.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.manager.BusllPosManage;

import static com.szxb.java8583.module.manager.BusllPosManage.getPosManager;
import static com.szxb.java8583.util.EncodeUtil.mergeByte;
import static com.szxb.java8583.util.EncodeUtil.str2Bcd;
import static com.szxb.java8583.util.MacEcbUtils.bytesToHexString;

/**
 * 作者：Tangren on 2018-07-05
 * 包名：com.szxb.java8583.module
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class SignIn {

    private volatile static SignIn instance = null;

    private SignIn() {
    }

    public static SignIn getInstance() {
        if (instance == null) {
            synchronized (SignIn.class) {
                if (instance == null) {
                    instance = new SignIn();
                }
            }
        }
        return instance;
    }


    /**
     * @param tradeSeq --999999
     * @return 签到报文
     */
    public Iso8583Message message(int tradeSeq) {
        Iso8583Message message = new Iso8583Message(BaseFactory.signInBaseFactory());
        message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(11, String.format("%06d", tradeSeq))
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(60, bytesToHexString(sign_field_60()))
                .setValue(63, BusllPosManage.getPosManager().getOperatorNumber());
        return message;
    }


    /**
     * @return 签到
     */
    private byte[] sign_field_60() {
        byte[] field_60_1 = str2Bcd("00");
        byte[] field_60_2 = str2Bcd(getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd("003", 1);
        return mergeByte(field_60_1, field_60_2, field_60_3);
    }
}
