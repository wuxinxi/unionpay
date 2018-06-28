package com.wxx.unionpay.field.parse;

import static com.wxx.unionpay.util.HexUtil.byte2Int;
import static java.lang.System.arraycopy;

/**
 * 作者：Tangren on 2018-06-27
 * 包名：com.wxx.unionpay.field.parse
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class ParseUtil {

    public static void parseSign(byte[] data) {
        int i = 12;
        byte[] mesType = new byte[4];
        arraycopy(data, i, mesType, 0, mesType.length);

        byte[] bitMap = new byte[8];
        arraycopy(data, i += mesType.length, bitMap, 0, bitMap.length);

        byte[] orderSeq = new byte[6];
        arraycopy(data, i += bitMap.length, orderSeq, 0, orderSeq.length);

        byte[] orderTime = new byte[6];
        arraycopy(data, i += orderSeq.length, orderTime, 0, orderTime.length);

        byte[] orderDate = new byte[4];
        arraycopy(data, i += orderTime.length, orderDate, 0, orderDate.length);

        byte[] markCodeLen = new byte[1];//LLVAR
        arraycopy(data, i += orderDate.length, markCodeLen, 0, markCodeLen.length);
        int markCodeLenInt = byte2Int(markCodeLen);

        byte[] markCode = new byte[markCodeLenInt];
        arraycopy(data, i += markCodeLen.length, markCode, 0, markCode.length);

        byte[] index = new byte[12];
        arraycopy(data, i += markCode.length, index, 0, index.length);

        byte[] resCode = new byte[2];
        arraycopy(data, i += index.length, resCode, 0, resCode.length);

        byte[] posSN = new byte[8];
        arraycopy(data, i += resCode.length, posSN, 0, posSN.length);

        byte[] mchId = new byte[15];
        arraycopy(data, i += posSN.length, mchId, 0, mchId.length);

        byte[] field_60_len = new byte[2];//LLLVAR
        arraycopy(data, i += mchId.length, field_60_len, 0, field_60_len.length);
        int len_60 = byte2Int(field_60_len);

        byte[] field_60 = new byte[len_60];
        arraycopy(data, i += field_60_len.length, field_60, 0, field_60.length);

        int field_index = 0;
        byte[] tradeCode = new byte[2];
        arraycopy(field_60, field_index, tradeCode, 0, tradeCode.length);

        byte[] batchNum = new byte[6];
        arraycopy(field_60, field_index += tradeCode.length, batchNum, 0, batchNum.length);

        byte[] managerCode = new byte[3];
        arraycopy(field_60, field_index + batchNum.length, managerCode, 0, managerCode.length);


        byte[] field_62_len = new byte[2]; //LLLVAR
        arraycopy(data, i += field_60.length, field_62_len, 0, field_62_len.length);
        int len_62 = byte2Int(field_62_len);

        byte[] posKey = new byte[len_62];
        arraycopy(data, i += field_62_len.length, posKey, 0, posKey.length);

        byte[] field_63_len = new byte[2];
        arraycopy(data, i += posKey.length, field_63_len, 0, field_63_len.length);
        int len_63 = byte2Int(field_63_len);

        byte[] operatorNumber = new byte[len_63];
        arraycopy(data, i + field_63_len.length, operatorNumber, 0, operatorNumber.length);

    }
}
