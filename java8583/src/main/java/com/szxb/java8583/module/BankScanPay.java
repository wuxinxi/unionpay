package com.szxb.java8583.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.util.MacEcbUtils;

import static com.szxb.java8583.module.BaseFactory.qrPayFactory;
import static com.szxb.java8583.util.EncodeUtil.bytesToHexString;
import static com.szxb.java8583.util.EncodeUtil.hex2byte;
import static com.szxb.java8583.util.EncodeUtil.mergeByte;
import static com.szxb.java8583.util.EncodeUtil.str2Bcd;

/**
 * 作者：Tangren on 2018-09-10
 * 包名：com.wxx.test.module
 * 邮箱：996489865@qq.com
 * TODO:银联扫码
 */

public class BankScanPay {

    private volatile static BankScanPay instance = null;

    private BankScanPay() {
    }

    public static BankScanPay getInstance() {
        if (instance == null) {
            synchronized (BankScanPay.class) {
                if (instance == null) {
                    instance = new BankScanPay();
                }
            }
        }
        return instance;
    }


    public Iso8583Message qrPayMessage(String qrCode, int amount, int seq, String macKey) {
        Iso8583Message iso8583Message = qrPayMessageData(qrCode, seq, amount);

        byte[] dataAll = iso8583Message.getBytes();

        byte[] data = new byte[dataAll.length - 2];
        System.arraycopy(dataAll, 2, data, 0, data.length);

        iso8583Message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setValue(64, pay_field_64(data, macKey));

        return iso8583Message;
    }

    private Iso8583Message qrPayMessageData(String qrCode, int seq, int amount) {
        Iso8583Message message = new Iso8583Message(qrPayFactory());
        message.setMti("0200")
                .setValue(3, "000000")//交易处理码
                .setValue(4, String.format("%012d", amount))//交易金额
                .setValue(11, String.format("%06d", seq))//受卡方系统跟踪号,流水号
                .setValue(22, "032")//服务点输入方式码0x03,0x20
                .setValue(25, "00")//服务点条件码
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
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
        return mergeByte(tag_byte, len_byte, value_qr_code_byte);
    }


    /**
     * @return 交易60
     */
    private byte[] pay_field_60() {
        byte[] field_60_1 = str2Bcd("22");
        byte[] field_60_2 = str2Bcd(BusllPosManage.getPosManager().getBatchNum());
        return mergeByte(field_60_1, field_60_2);
    }

    /**
     * @return 64域
     */
    private String pay_field_64(byte[] data, String macKey) {
        byte[] mac = MacEcbUtils.getMac(hex2byte(macKey), data);
        return bytesToHexString(mac);
    }

}

