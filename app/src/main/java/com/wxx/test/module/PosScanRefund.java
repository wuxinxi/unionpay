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


    public Iso8583Message refun(int seq, String batchNum,String reason) {

        Iso8583Message iso8583Message = payMessageData( seq, batchNum,reason);

        byte[] dataAll = iso8583Message.getBytes();

        byte[] data = new byte[dataAll.length - 2];
        System.arraycopy(dataAll, 2, data, 0, data.length);

        iso8583Message.setTpdu(UnionPayApp.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setValue(64, pay_field_64(data));

        return iso8583Message;
    }


    private Iso8583Message payMessageData( int seq, String batchNum, String cankaohao) {

        Iso8583Message message = new Iso8583Message(payRefFactory());
        message.setMti("0200")
                .setValue(3, "200000")//交易处理码
                .setValue(4, String.format("%012d", 1))//交易金额
                .setValue(11, String.format("%06d", 1000))//受卡方系统跟踪号,流水号
                .setValue(22, "072")//服务点输入方式码
                .setValue(25, "00")//服务点条件码

                .setValue(37, cankaohao)//参考号

                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(49, "156")//交易货币代码
                .setValue(60, bytesToHexString(pay_field_60(batchNum)))
                .setValue(61, bytesToHexString(pay_field_61(batchNum,seq)))
                .setValue(64, "");
        return message;
    }

    private byte[] pay_field_61(String batchNum, int seq) {
        byte[] field_61_1 = str2Bcd(batchNum);
        byte[] field_61_2 = str2Bcd(String.format("%06d", seq));
        return mergeByte(field_61_1, field_61_2);
    }

    /**
     * @return 退款Factory
     */
    public static Iso8583MessageFactory payRefFactory() {
        Iso8583MessageFactory facotry = new Iso8583MessageFactory(2, false, Charset.forName("UTF-8"), dataHeader());
        facotry.set(3, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(4, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 12))
                .set(11, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(22, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 3))
                .set(25, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 2))
                .set(37, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR,12))
                .set(41, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 8))
                .set(42, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 15))
                .set(49, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 3))
                .set(60, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
                .set(61, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
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
    private byte[] pay_field_60(String batchNum) {
        byte[] field_60_1 = str2Bcd("23");
        byte[] field_60_2 = str2Bcd(batchNum);
        return mergeByte(field_60_1, field_60_2);
    }


}
