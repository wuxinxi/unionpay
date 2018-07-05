package com.wxx.test.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.field.Iso8583FieldType;
import com.szxb.java8583.util.MacEcbUtils;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.log.MLog;
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

public class PosTransaction {
    private volatile static PosTransaction instance = null;

    private PosTransaction() {
    }

    public static PosTransaction getInstance() {
        if (instance == null) {
            synchronized (PosTransaction.class) {
                if (instance == null) {
                    instance = new PosTransaction();
                }
            }
        }
        return instance;
    }


    private Iso8583MessageFactory payFactory() {
        Iso8583MessageFactory facotry = new Iso8583MessageFactory(2, false, Charset.forName("UTF-8"), dataHeader());
        facotry.set(2, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLVAR_NUMERIC, 0))
                .set(3, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(4, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 12))
                .set(11, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(22, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 3))
                .set(23, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 4))
                .set(25, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 2))
                .set(35, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLVAR_NUMERIC, 0))
                .set(41, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 8))
                .set(42, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 15))
                .set(49, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 3))
                .set(53, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 16))
                .set(55, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_BYTE_NUMERIC, 0))
                .set(60, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
                .set(64, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 16));
        return facotry;
    }

    public Iso8583Message payMessage(String cardNo, String cardNum, String cardData, String tlv55) {
        int cardNumLen = cardNum.length();
        for (int i = 0; i < 4 - cardNumLen; i++) {
            cardNum = "0" + cardNum;
        }
        Iso8583Message iso8583Message = payMessageData(cardNo, cardNum, cardData, tlv55);

        byte[] dataAll = iso8583Message.getBytes();

        byte[] data = new byte[dataAll.length - 2];
        System.arraycopy(dataAll, 2, data, 0, data.length);

        MLog.d("payMessage(PosTransaction.java:73)" + HexUtil.bytesToHexString(data));
        iso8583Message.setTpdu(UnionPayApp.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setValue(64, pay_field_64(data));

        return iso8583Message;
    }


    public Iso8583Message payMessageData(String cardNo, String cardNum, String cardData, String tlv55) {
        int cardNumLen = cardNum.length();
        for (int i = 0; i < 4 - cardNumLen; i++) {
            cardNum = "0" + cardNum;
        }
        Iso8583Message message = new Iso8583Message(payFactory());
        message.setMti("0200")
                .setValue(2, cardNo)//主账号
                .setValue(3, "000000")//交易处理码
                .setValue(4, String.format("%012d", 1))//交易金额
                .setValue(11, String.format("%06d", UnionPayApp.getPosManager().getTradeSeq()))//受卡方系统跟踪号,流水号
                .setValue(22, "072")//服务点输入方式码
                .setValue(23, cardNum)//卡片序列号
                .setValue(25, "00")//服务点条件码
                .setValue(35, cardData)//2 磁道数据
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(49, "156")//交易货币代码
                .setValue(53, String.format("%016d", 0))//安全控制信息
                .setValue(55, tlv55)
                .setValue(60, bytesToHexString(pay_field_60()))
                .setValue(64, "");
        return message;
    }

    /**
     * @return 64域
     */
    private String pay_field_64(byte[] data) {
        byte[] mac = MacEcbUtils.getMac(HexUtil.hex2byte(UnionPayApp.getPosManager().getMacKey()), data);
        return HexUtil.bytesToHexString(mac);
    }

    /**
     * @return 交易60
     */
    private byte[] pay_field_60() {
        byte[] field_60_1 = str2Bcd("22");
        byte[] field_60_2 = str2Bcd(UnionPayApp.getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd("000600", 1);
        return mergeByte(field_60_1, field_60_2, field_60_3);
    }


}
