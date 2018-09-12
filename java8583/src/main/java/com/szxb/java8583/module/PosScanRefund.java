package com.szxb.java8583.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.util.MacEcbUtils;

import static com.szxb.java8583.module.BaseFactory.scanPayRefFactory;
import static com.szxb.java8583.util.EncodeUtil.bytesToHexString;
import static com.szxb.java8583.util.EncodeUtil.hex2byte;
import static com.szxb.java8583.util.EncodeUtil.mergeByte;
import static com.szxb.java8583.util.EncodeUtil.str2Bcd;

/**
 * 作者：Tangren on 2018-06-29
 * 包名：com.wxx.test.module
 * 邮箱：996489865@qq.com
 * TODO:银联二维码冲正
 */

public class PosScanRefund {
    private volatile static PosScanRefund instance = null;

    private PosScanRefund() {
    }

    public static PosScanRefund getInstance() {
        if (instance == null) {
            synchronized (PosScanRefund.class) {
                if (instance == null) {
                    instance = new PosScanRefund();
                }
            }
        }
        return instance;
    }


    /**
     * @param seq          流水
     * @param qrCode       二维码
     * @param reason       原因
     * @param refundAmount 金额
     * @return 二维码冲正
     */
    public Iso8583Message refun(int seq, String qrCode, String reason, int refundAmount) {

        Iso8583Message iso8583Message = payMessageData(seq, qrCode, reason, refundAmount);

        byte[] dataAll = iso8583Message.getBytes();

        byte[] data = new byte[dataAll.length - 2];
        System.arraycopy(dataAll, 2, data, 0, data.length);

        iso8583Message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setValue(64, pay_field_64(data));

        return iso8583Message;
    }


    private Iso8583Message payMessageData(int seq, String qrCode, String reason, int refundAmount) {
        Iso8583Message message = new Iso8583Message(scanPayRefFactory());
        message.setMti("0400")
                .setValue(3, "000000")//交易处理码
                .setValue(4, String.format("%012d", refundAmount))//交易金额
                .setValue(11, String.format("%06d", seq))//受卡方系统跟踪号,流水号
                .setValue(22, "032")//服务点输入方式码
                .setValue(25, "00")//服务点条件码
                .setValue(39, reason)//
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(49, "156")//交易货币代码
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
     * @return 64域
     */
    private String pay_field_64(byte[] data) {
        byte[] mac = MacEcbUtils.getMac(hex2byte(BusllPosManage.getPosManager().getMacKey()), data);
        return bytesToHexString(mac);
    }

    /**
     * @return 交易60
     */
    private byte[] pay_field_60() {
        byte[] field_60_1 = str2Bcd("22");
        byte[] field_60_2 = str2Bcd(BusllPosManage.getPosManager().getBatchNum());
        return mergeByte(field_60_1, field_60_2);
    }


}
