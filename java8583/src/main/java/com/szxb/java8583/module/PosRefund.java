package com.szxb.java8583.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.util.MacEcbUtils;

import static com.szxb.java8583.module.BaseFactory.payRefFactory;
import static com.szxb.java8583.util.EncodeUtil.hex2byte;
import static com.szxb.java8583.util.EncodeUtil.mergeByte;
import static com.szxb.java8583.util.EncodeUtil.str2Bcd;
import static com.szxb.java8583.util.MacEcbUtils.bytesToHexString;

/**
 * 作者：Tangren on 2018-06-29
 * 包名：com.wxx.test.module
 * 邮箱：996489865@qq.com
 * TODO:银联刷卡冲正
 */

public class PosRefund {
    private volatile static PosRefund instance = null;

    private PosRefund() {
    }

    public static PosRefund getInstance() {
        if (instance == null) {
            synchronized (PosRefund.class) {
                if (instance == null) {
                    instance = new PosRefund();
                }
            }
        }
        return instance;
    }


    public Iso8583Message refund(String cardNo, String cardNum, int seq, String batchNum, String reason,int refundAmount) {
        int cardNumLen = cardNum.length();
        for (int i = 0; i < 4 - cardNumLen; i++) {
            cardNum = "0" + cardNum;
        }
        Iso8583Message iso8583Message = payRefundData(cardNo, cardNum, seq, batchNum, reason,refundAmount);

        byte[] dataAll = iso8583Message.getBytes();

        byte[] data = new byte[dataAll.length - 2];
        System.arraycopy(dataAll, 2, data, 0, data.length);

        iso8583Message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setValue(64, pay_field_64(data));

        return iso8583Message;
    }


    /**
     * @param cardNo   主卡号
     * @param cardNum  卡序列号
     * @param seq      流水
     * @param batchNum 批次号
     * @param reason   原因：00
     * @return 退款
     */
    private Iso8583Message payRefundData(String cardNo, String cardNum, int seq, String batchNum, String reason, int refundAmount) {
        int cardNumLen = cardNum.length();
        for (int i = 0; i < 4 - cardNumLen; i++) {
            cardNum = "0" + cardNum;
        }
        Iso8583Message message = new Iso8583Message(payRefFactory());
        message.setMti("0400")
                .setValue(2, cardNo)//主账号
                .setValue(3, "000000")//交易处理码
                .setValue(4, String.format("%012d", refundAmount))//交易金额
                .setValue(11, String.format("%06d", seq))//受卡方系统跟踪号,流水号
                .setValue(22, "042")//服务点输入方式码
                .setValue(23, cardNum)//卡片序列号
                .setValue(25, "00")//服务点条件码
                .setValue(39, reason)//冲正原因
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(49, "156")//交易货币代码
                .setValue(53, String.format("%016d", 0))//安全控制信息
                .setValue(60, bytesToHexString(pay_field_60(batchNum)))
                .setValue(64, "");
        return message;
    }

    /**
     * @return 64域
     */
    private String pay_field_64(byte[] data) {
        byte[] mac = MacEcbUtils.getMac(hex2byte(BusllPosManage.getPosManager().getMacKey()), data);
        return bytesToHexString(mac);
    }


    /**
     * @return 交易60
     */
    private byte[] pay_field_60(String batchNum) {
        byte[] field_60_1 = str2Bcd("22");
        byte[] field_60_2 = str2Bcd(batchNum);
        byte[] field_60_3 = str2Bcd("000600", 1);
        return mergeByte(field_60_1, field_60_2, field_60_3);
    }


}
