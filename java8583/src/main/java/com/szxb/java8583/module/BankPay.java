package com.szxb.java8583.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.util.MacEcbUtils;

import static com.szxb.java8583.util.EncodeUtil.hex2byte;
import static com.szxb.java8583.util.EncodeUtil.mergeByte;
import static com.szxb.java8583.util.EncodeUtil.str2Bcd;
import static com.szxb.java8583.util.MacEcbUtils.bytesToHexString;

/**
 * 作者：Tangren on 2018-07-05
 * 包名：com.szxb.java8583.module
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class BankPay {

    private volatile static BankPay instance = null;

    private BankPay() {
    }

    public static BankPay getInstance() {
        if (instance == null) {
            synchronized (BankPay.class) {
                if (instance == null) {
                    instance = new BankPay();
                }
            }
        }
        return instance;
    }

    public Iso8583Message payMessage(BusCard busCard) {
        String cardNum = busCard.getCardNum();
        int cardNumLen = cardNum.length();
        for (int i = 0; i < 4 - cardNumLen; i++) {
            cardNum = "0" + cardNum;
        }
        busCard.setCardNum(cardNum);
        Iso8583Message iso8583Message = payMessageData(busCard);

        byte[] dataAll = iso8583Message.getBytes();

        byte[] data = new byte[dataAll.length - 2];
        System.arraycopy(dataAll, 2, data, 0, data.length);

        iso8583Message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setValue(64, pay_field_64(data, busCard.getMacKey()));

        return iso8583Message;
    }


    private Iso8583Message payMessageData(BusCard busCard) {
        Iso8583Message message = new Iso8583Message(BaseFactory.payBaseFactory());
        message.setMti("0200")
                .setValue(2, busCard.getMainCardNo())//主账号
                .setValue(3, "000000")//交易处理码
                .setValue(4, String.format("%012d", busCard.getMoney()))//交易金额
                .setValue(11, String.format("%06d", busCard.getTradeSeq()))//受卡方系统跟踪号,流水号
                .setValue(22, "072")//服务点输入方式码
                .setValue(23, busCard.getCardNum())//卡片序列号
                .setValue(25, "00")//服务点条件码
                .setValue(35, busCard.getMagTrackData())//2 磁道数据
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(49, "156")//交易货币代码
                .setValue(53, String.format("%016d", 0))//安全控制信息
                .setValue(55, busCard.getTlv55())
                .setValue(60, bytesToHexString(pay_field_60()))
                .setValue(64, "");
        return message;
    }

    /**
     * @return 64域
     */
    private String pay_field_64(byte[] data, String macKey) {
        byte[] mac = MacEcbUtils.getMac(hex2byte(macKey), data);
        return bytesToHexString(mac);
    }

    /**
     * @return 交易60
     */
    private byte[] pay_field_60() {
        byte[] field_60_1 = str2Bcd("22");
        byte[] field_60_2 = str2Bcd(BusllPosManage.getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd("000600", 1);
        return mergeByte(field_60_1, field_60_2, field_60_3);
    }


}
