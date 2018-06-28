package com.wxx.unionpay.field;

import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.log.MLog;

import static com.wxx.unionpay.util.HexUtil.bytesToHexString;
import static com.wxx.unionpay.util.HexUtil.int2Bytes;
import static com.wxx.unionpay.util.HexUtil.mergeByte;
import static com.wxx.unionpay.util.HexUtil.str2Bcd;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：com.wxx.unionpay.field
 * 邮箱：996489865@qq.com
 * TODO:常用的域
 */

public class ComField {

    private static final byte[] TPDU = {0x60, 0x03, 0x03, 0x00, 0x00};

    private static final byte[] APDU = {0x61, 0x31, 0x00, 0x31, 0x30, 0x31};

    private static final byte[] SIGN_BITMAP = {0x00, 0x20, 0x00, 0x00, 0x00, (byte) 0xC0, 0x00, 0x12};
    private static final byte[] SIGN_MSG_TYPE = {(byte) 0x08, 0x00};

    private static final byte[] STATE_MSG_TYPE = {(byte) 0x08, 0x20};
    private static final byte[] STATE_BITMAP = {0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0, 0x00, 0x14};


    //POS参数传递
    private static final byte[] DOWN_MSG_TYPE = {0x08, 0x00};
    private static final byte[] DOWN_BITMAP = {0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0, 0x00, 0x14};


    /**
     * @return 11域, 终端流水号
     */
    public static byte[] field_11() {
        String order_seq = String.format("%06d", UnionPayApp.getPosManager().getTradeSeq());
        return str2Bcd(order_seq);
    }

    /**
     * @return 终端编号
     */
    public static byte[] field_41() {
        String posSN = UnionPayApp.getPosManager().getPosSn();
        return posSN.getBytes();
    }


    /**
     * @return 受卡方标识码
     */
    public static byte[] field_42() {
        return UnionPayApp.getPosManager().getMchID().getBytes();
    }


    /**
     * 请求报文中该域占三个字节，第
     * 一个字节为数字 1，表示是认证中
     * 心公钥信息查询报文；后面两个
     * 字节联合起来表示 POS 收到的所
     * 有公钥信息个数，所以首先上送
     * 该请求交易时，整个域取值应为
     * 100
     *
     * @return 62域
     */
    public static byte[] field_62(String code) {
        byte[] field_62_data=code.getBytes();
        byte[] field_62_len = int2Bytes(field_62_data.length, 2);
        return mergeByte(field_62_len, field_62_data);
    }

    /**
     * @return 操作员编号
     */
    public static byte[] field_63_1() {
        String operatorNumber = UnionPayApp.getPosManager().getOperatorNumber();
        return operatorNumber.getBytes();
    }

    public static byte[] field_63() {
        byte[] field_63_1 = field_63_1();
        byte[] len = str2Bcd("003");
        return mergeByte(len, field_63_1);
    }

    public static byte[] field_632() {
        byte[] field_63_1 = field_63_1();
        return mergeByte(field_63_1);
    }

    /**
     * @return 签到
     */
    public static byte[] sign() {
        byte[] data_head = mergeByte(TPDU, APDU, SIGN_MSG_TYPE, SIGN_BITMAP);
        byte[] field_11 = field_11();
        byte[] field_41 = field_41();
        byte[] field_42 = field_42();

        byte[] field_60 = Field_60.sign_field_60();
        byte[] field_63 = field_63();
        byte[] data_content = mergeByte(field_11, field_41, field_42, field_60, field_63);
        byte[] len = int2Bytes(data_head.length + data_content.length, 2);

        return mergeByte(len, data_head, data_content);
    }


    /**
     * @return 状态上送
     */
    public static byte[] state() {
        byte[] data_head = mergeByte(TPDU, APDU, STATE_MSG_TYPE, STATE_BITMAP);
        byte[] field_41 = field_41();
        byte[] field_42 = field_42();

        byte[] field_60 = Field_60.state_field_60();
        byte[] field_62 = field_62("100");
        byte[] data_content = mergeByte(field_41, field_42, field_60, field_62);
        byte[] len = int2Bytes(data_head.length + data_content.length, 2);

        return mergeByte(len, data_head, data_content);
    }

    /**
     * @return 下载IC 卡公钥/参数/TMS 参数/卡 BIN 黑名单下载结束
     */
    public static byte[] downParam() {
        byte[] data_head = mergeByte(TPDU, APDU, DOWN_MSG_TYPE, DOWN_BITMAP);
        byte[] field_41 = field_41();
        byte[] field_42 = field_42();

        byte[] field_60 = Field_60.down_field_60("380");
        String string = UnionPayApp.getPosManager().aidIndexList();
        String[] aidIndexs = string.split(",");
        for (int i = 0; i < aidIndexs.length; i++) {
            MLog.d("downParam(ComField.java:138)" + aidIndexs[i]);

        }
        byte[] field_62 = field_62(aidIndexs[2]);
        MLog.d("downParam(ComField.java:136)" + bytesToHexString(field_62));
        byte[] data_content = mergeByte(field_41, field_42, field_60, field_62);
        byte[] len = int2Bytes(data_head.length + data_content.length, 2);

        return mergeByte(len, data_head, data_content);
    }


}
