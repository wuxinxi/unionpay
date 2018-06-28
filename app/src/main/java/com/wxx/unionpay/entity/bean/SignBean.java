package com.wxx.unionpay.entity.bean;

import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.util.ThreeDes;

import java.util.Arrays;

import static com.wxx.unionpay.util.HexUtil.bcd2Str;
import static com.wxx.unionpay.util.HexUtil.byte2Int;
import static com.wxx.unionpay.util.HexUtil.byteToString;
import static com.wxx.unionpay.util.HexUtil.bytesToHexString;
import static com.wxx.unionpay.util.HexUtil.hex2byte;
import static java.lang.System.arraycopy;

/**
 * 作者：Tangren on 2018-06-27
 * 包名：com.wxx.unionpay.entity.bean
 * 邮箱：996489865@qq.com
 * TODO:签到
 */

public class SignBean {
    private String mesType;
    private String bitMap;
    private String orderSeq;
    private String orderTime;
    private String orderDate;
    private String markCode;
    private String index;
    private String resCode;
    private String posSN;
    private String mchId;
    private String tradeCode;
    private String batchNum;
    private String managerCode;
    private String posKey;

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public String getBitMap() {
        return bitMap;
    }

    public void setBitMap(String bitMap) {
        this.bitMap = bitMap;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getMarkCode() {
        return markCode;
    }

    public void setMarkCode(String markCode) {
        this.markCode = markCode;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getPosSN() {
        return posSN;
    }

    public void setPosSN(String posSN) {
        this.posSN = posSN;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(String managerCode) {
        this.managerCode = managerCode;
    }

    public String getPosKey() {
        return posKey;
    }

    public void setPosKey(String posKey) {
        this.posKey = posKey;
    }

    public SignBean(byte[] data) {
        int i = 11;
        byte[] mesType = new byte[2];
        arraycopy(data, i, mesType, 0, mesType.length);
        setMesType(bcd2Str(mesType));

        byte[] bitMap = new byte[8];
        arraycopy(data, i += mesType.length, bitMap, 0, bitMap.length);
        setBitMap(bytesToHexString(bitMap));

        byte[] orderSeq = new byte[3];
        arraycopy(data, i += bitMap.length, orderSeq, 0, orderSeq.length);
        setOrderSeq(bcd2Str(orderSeq));

        byte[] orderTime = new byte[3];
        arraycopy(data, i += orderSeq.length, orderTime, 0, orderTime.length);
        setOrderTime(bcd2Str(orderTime));

        byte[] orderDate = new byte[2];
        arraycopy(data, i += orderTime.length, orderDate, 0, orderDate.length);
        setOrderDate(bcd2Str(orderDate));

        byte[] markCodeLen = new byte[1];//LLVAR
        arraycopy(data, i += orderDate.length, markCodeLen, 0, markCodeLen.length);
        int markCodeLenInt = byte2Int(markCodeLen);

        byte[] markCode = new byte[markCodeLenInt / 2];
        arraycopy(data, i += markCodeLen.length, markCode, 0, markCode.length);
        setMarkCode(bcd2Str(markCode));

        byte[] index = new byte[12];
        arraycopy(data, i += markCode.length, index, 0, index.length);
        setIndex(byteToString(index));

        byte[] resCode = new byte[2];
        arraycopy(data, i += index.length, resCode, 0, resCode.length);
        setResCode(byteToString(resCode));

        byte[] posSN = new byte[8];
        arraycopy(data, i += resCode.length, posSN, 0, posSN.length);
        setPosSN(bytesToHexString(posSN));

        byte[] mchId = new byte[15];
        arraycopy(data, i += posSN.length, mchId, 0, mchId.length);
        setMchId(byteToString(mchId));

        byte[] field_60_len = new byte[2];//LLLVAR
        arraycopy(data, i += mchId.length, field_60_len, 0, field_60_len.length);
        int len_60 = byte2Int(field_60_len);
        if ((len_60 & 1) == 1) {
            len_60 = len_60 + 1;
        }
        len_60 = len_60 / 2;

        byte[] field_60 = new byte[len_60];
        arraycopy(data, i += field_60_len.length, field_60, 0, field_60.length);

        int field_index = 0;
        byte[] tradeCode = new byte[1];
        arraycopy(field_60, field_index, tradeCode, 0, tradeCode.length);
        setTradeCode(bcd2Str(tradeCode));

        byte[] batchNum = new byte[3];
        arraycopy(field_60, field_index += tradeCode.length, batchNum, 0, batchNum.length);
        setBatchNum(bcd2Str(batchNum));

        byte[] managerCode = new byte[2];
        arraycopy(field_60, field_index + batchNum.length, managerCode, 0, managerCode.length);
        setManagerCode(bcd2Str(managerCode));


        byte[] field_62_len = new byte[2]; //LLLVAR
        arraycopy(data, i += field_60.length, field_62_len, 0, field_62_len.length);
        int len_62 = byte2Int(field_62_len);
        MLog.d("SignBean(SignBean.java:234)i=" + i + ",=" + bytesToHexString(field_62_len));

        byte[] posKey = new byte[len_62];
        arraycopy(data, i + field_62_len.length, posKey, 0, posKey.length);
        setPosKey(bytesToHexString(posKey));

        setKey(posKey);
    }

    @Override
    public String toString() {
        return "SignBean{" +
                "mesType='" + mesType + '\'' +
                ", bitMap='" + bitMap + '\'' +
                ", orderSeq='" + orderSeq + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", markCode='" + markCode + '\'' +
                ", index='" + index + '\'' +
                ", resCode='" + resCode + '\'' +
                ", posSN='" + posSN + '\'' +
                ", mchId='" + mchId + '\'' +
                ", tradeCode='" + tradeCode + '\'' +
                ", batchNum='" + batchNum + '\'' +
                ", managerCode='" + managerCode + '\'' +
                ", posKey='" + posKey + '\'' +
                '}';
    }

    /**
     * 对于双倍长密钥算法，前20个字节为PIN的工作密钥的密文，后20个字节为MAC的工作密钥的密文。
     * （其中，“PIN工作密钥”前16个字节是密文，后4个字节是checkvalue；前16个字节解出明文后，对8
     * 个数值0做双倍长密钥算法，取结果的前四位与checkvalue 的值比较应该是一致的；“MAC工作密钥”
     * 前8个字节是密文，再8个字节是二进制零，后4个字节是checkvalue；前8个字节解出明文后，对8个数值
     * 0做单倍长密钥算法，取结果的前四位与checkvalue 的值比较应该是一致的
     *
     * @param buffer
     */
    public void setKey(byte[] buffer) {
        int i = 0;
        byte[] pinKey = new byte[16];
        arraycopy(buffer, i, pinKey, 0, pinKey.length);
        MLog.d("setKey(SignBean.java:256)pin 密文：" + bytesToHexString(pinKey));

        byte[] pinKeyCrc = new byte[4];
        arraycopy(buffer, i += pinKey.length, pinKeyCrc, 0, pinKeyCrc.length);
        MLog.d("setKey(SignBean.java:260)pin checkValue=" + bytesToHexString(pinKeyCrc));

        byte[] macKey = new byte[16];
        arraycopy(buffer, i += pinKeyCrc.length, macKey, 0, macKey.length);
        MLog.d("setKey(SignBean.java:264)mac 密文" + bytesToHexString(macKey));

        byte[] macKeyCrc = new byte[4];
        arraycopy(buffer, i + macKey.length, macKeyCrc, 0, macKeyCrc.length);
        MLog.d("setKey(SignBean.java:268)Mac checkValue=" + bytesToHexString(macKeyCrc));

        String key = "9D0BDA257668DF8ADC4C5B856EA26298";
        byte[] des = ThreeDes.Union3DesDecrypt(hex2byte(key), macKey);

        String macKeyHex = bytesToHexString(des);
        String Mac = macKeyHex.substring(0, 16);
        byte[] crc = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        byte[] crcdata = ThreeDes.encrypt(crc, Mac);

        if (crcdata != null) {
            byte[] macCrc = new byte[4];
            arraycopy(crcdata, 0, macCrc, 0, macCrc.length);
            if (Arrays.equals(macCrc, macKeyCrc)) {
                UnionPayApp.getPosManager().setMacKey(bytesToHexString(macKey));
                MLog.d("setKey(SignBean.java:292)MAC正确");

            }
        }

    }

}
