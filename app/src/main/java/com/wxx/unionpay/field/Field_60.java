package com.wxx.unionpay.field;

import com.wxx.unionpay.UnionPayApp;

import static com.wxx.unionpay.util.HexUtil.mergeByte;
import static com.wxx.unionpay.util.HexUtil.str2Bcd;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：com.wxx.unionpay.field
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class Field_60 {

    /**
     * @return 签到
     */
    public static byte[] sign_field_60() {
        byte[] field_60_1 = str2Bcd("00");
        byte[] field_60_2 = str2Bcd(UnionPayApp.getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd("003", 1);
        byte[] len = str2Bcd("011");
        return mergeByte(len, field_60_1, field_60_2, field_60_3);
    }

    /**
     * @return 签到
     */
    public static byte[] sign_field_602() {
        byte[] field_60_1 = str2Bcd("00");
        byte[] field_60_2 = str2Bcd(UnionPayApp.getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd("003", 1);
        return mergeByte( field_60_1, field_60_2, field_60_3);
    }

    /**
     * @return 状态上送
     */
    public static byte[] state_field_60() {
        byte[] field_60_1 = str2Bcd("00");
        byte[] field_60_2 = str2Bcd(UnionPayApp.getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd("382", 1);
        byte[] len = str2Bcd("011");
        return mergeByte(len, field_60_1, field_60_2, field_60_3);
    }

    /**
     * IC 卡公钥下载交易采用 370/371
     * （含国密公钥下载）
     * IC 卡参数下载交易采用 380/381
     * TMS 参数下载采用 364/365
     * 卡 BIN 黑名单下载采用 390/391
     *
     * @return 下载参数
     */
    public static byte[] down_field_60(String code) {
        byte[] field_60_1 = str2Bcd("00");
        byte[] field_60_2 = str2Bcd(UnionPayApp.getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd(code, 1);
        byte[] len = str2Bcd("011");
        return mergeByte(len, field_60_1, field_60_2, field_60_3);
    }


}
