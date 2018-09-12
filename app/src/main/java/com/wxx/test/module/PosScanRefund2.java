package com.wxx.test.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.field.Iso8583FieldType;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.util.MacEcbUtils;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.util.HexUtil;

import java.nio.charset.Charset;

import static com.wxx.test.base.Config.dataHeader;
import static com.wxx.unionpay.util.HexUtil.bytesToHexString;
import static com.wxx.unionpay.util.HexUtil.mergeByte;
import static com.wxx.unionpay.util.HexUtil.str2Bcd;

/**
 * 作者：Tangren on 2018-06-29
 * 包名：com.wxx.test.module
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class PosScanRefund2 {
    private volatile static PosScanRefund2 instance = null;

    private PosScanRefund2() {
    }

    public static PosScanRefund2 getInstance() {
        if (instance == null) {
            synchronized (PosScanRefund2.class) {
                if (instance == null) {
                    instance = new PosScanRefund2();
                }
            }
        }
        return instance;
    }


    public Iso8583Message refun(int seq,String qrCode, String reason,int refundAmount) {

        Iso8583Message iso8583Message = payMessageData( seq,qrCode,reason,refundAmount);

        byte[] dataAll = iso8583Message.getBytes();

        byte[] data = new byte[dataAll.length - 2];
        System.arraycopy(dataAll, 2, data, 0, data.length);

        iso8583Message.setTpdu(UnionPayApp.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setValue(64, pay_field_64(data));

        return iso8583Message;
    }


    private Iso8583Message payMessageData( int seq, String qrCode,String reason,int refundAmount) {
        Iso8583Message message = new Iso8583Message(scanPayRefFactory());
        message.setMti("0400")
                .setValue(3, "000000")//交易处理码
                .setValue(4, String.format("%012d", refundAmount))//交易金额
                .setValue(11, String.format("%06d", seq))//受卡方系统跟踪号,流水号
                .setValue(22, "032")//服务点输入方式码
                .setValue(25, "00")//服务点条件码
                .setValue(39, reason)//
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
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
     * @return 退款Factory
     */
    public static Iso8583MessageFactory scanPayRefFactory() {
        Iso8583MessageFactory facotry = new Iso8583MessageFactory(2, false, Charset.forName("UTF-8"), dataHeader());
        facotry.set(3, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(4, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 12))
                .set(11, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(22, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 3))
                .set(25, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 2))
                .set(39, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR,2))
                .set(41, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 8))
                .set(42, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 15))
                .set(49, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 3))
                .set(59, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_CHAR, 0))
                .set(60, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
                .set(64, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 16));
        return facotry;
    }

    /**
     * @return 64域
     */
    private String pay_field_64(byte[] data) {
        byte[] mac = MacEcbUtils.getMac(HexUtil.hex2byte(BusllPosManage.getPosManager().getMacKey()), data);
        return HexUtil.bytesToHexString(mac);
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
