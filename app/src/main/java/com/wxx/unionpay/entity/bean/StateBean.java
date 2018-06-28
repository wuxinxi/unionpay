package com.wxx.unionpay.entity.bean;

import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.db.sp.FetchAppConfig;
import com.wxx.unionpay.log.MLog;

import static com.wxx.unionpay.util.HexUtil.bcd2Str;
import static com.wxx.unionpay.util.HexUtil.byte2Int;
import static com.wxx.unionpay.util.HexUtil.byteToString;
import static com.wxx.unionpay.util.HexUtil.bytesToHexString;
import static java.lang.System.arraycopy;

/**
 * 作者：Tangren on 2018-06-27
 * 包名：com.wxx.unionpay.entity.bean
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class StateBean {
    private String mesType;
    private String bitMap;
    private String orderTime;
    private String orderDate;
    private String resCode;
    private String posSN;
    private String mchId;
    private String tradeCode;
    private String batchNum;
    private String managerCode;
    private String posStateInfo;

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

    public String getPosStateInfo() {
        return posStateInfo;
    }

    public void setPosStateInfo(String posStateInfo) {
        this.posStateInfo = posStateInfo;
    }


    public StateBean(byte[] data) {
        int i = 11;
        byte[] mesType = new byte[2];
        arraycopy(data, i, mesType, 0, mesType.length);
        setMesType(bcd2Str(mesType));

        byte[] bitMap = new byte[8];
        arraycopy(data, i += mesType.length, bitMap, 0, bitMap.length);
        setBitMap(bytesToHexString(bitMap));

        byte[] orderTime = new byte[3];
        arraycopy(data, i += bitMap.length, orderTime, 0, orderTime.length);
        setOrderTime(bcd2Str(orderTime));

        byte[] orderDate = new byte[2];
        arraycopy(data, i += orderTime.length, orderDate, 0, orderDate.length);
        setOrderDate(bcd2Str(orderDate));

        byte[] resCode = new byte[2];
        arraycopy(data, i += orderDate.length, resCode, 0, resCode.length);
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
        MLog.d("StateBean(StateBean.java:153)len_60=" + len_60);
        if ((len_60 & 1) == 1) {
            len_60 = len_60 + 1;
        }
        len_60 = len_60 / 2;
        MLog.d("StateBean(StateBean.java:158)len_60=" + len_60);

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

        byte[] field_62_len = new byte[2];
        arraycopy(data, i += field_60.length, field_62_len, 0, field_62_len.length);
        int len_62 = byte2Int(field_62_len);

        byte[] posStateInfo = new byte[len_62];
        arraycopy(data, i + field_62_len.length, posStateInfo, 0, posStateInfo.length);
        setPosStateInfo(bytesToHexString(posStateInfo));

        setPosInfo(posStateInfo);
    }


    @Override
    public String toString() {
        return "StateBean{" +
                "mesType='" + mesType + '\'' +
                ", bitMap='" + bitMap + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", resCode='" + resCode + '\'' +
                ", posSN='" + posSN + '\'' +
                ", mchId='" + mchId + '\'' +
                ", tradeCode='" + tradeCode + '\'' +
                ", batchNum='" + batchNum + '\'' +
                ", managerCode='" + managerCode + '\'' +
                ", posStateInfo='" + posStateInfo + '\'' +
                '}';
    }

    /**
     * @param posInfo
     */
    public void setPosInfo(byte[] posInfo) {
        byte[] index_1 = new byte[1];
        arraycopy(posInfo, 0, index_1, 0, index_1.length);
        String state = byteToString(index_1);
        if (state.equals("0")) {
            //
        } else if (state.equals("1")) {
            //31 9F06
            byte[] data = new byte[posInfo.length - 1];
            arraycopy(posInfo, 1, data, 0, data.length);

            StringBuilder sBuilder = new StringBuilder();
            int index = 0;
            for (int i = 0; i < data.length; i += 11) {
                byte[] rid = new byte[2];
                arraycopy(data, index, rid, 0, rid.length);
                MLog.d("setPosInfo(StateBean.java:224)" + bytesToHexString(rid));

                byte[] len = new byte[1];
                arraycopy(data, index += rid.length, len, 0, len.length);
                MLog.d("setPosInfo(StateBean.java:228)" + bytesToHexString(len));

                byte[] value = new byte[8];
                arraycopy(data, index += len.length, value, 0, value.length);
                MLog.d("setPosInfo(StateBean.java:232)" + bytesToHexString(value));
                sBuilder.append(bytesToHexString(rid)).append(bytesToHexString(len)).append(bytesToHexString(value)).append(",");
                index += value.length;
            }

            MLog.d("setPosInfo(StateBean.java:238)" + sBuilder.toString());
            UnionPayApp.getPosManager().setAidIndexList(sBuilder.toString());

            String aidIndexList = FetchAppConfig.getAidIndexList();
            MLog.d("setPosInfo(StateBean.java:243)"+aidIndexList);
        }

    }
}
