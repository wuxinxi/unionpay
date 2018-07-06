package com.union;

import com.szxb.java8583.module.manager.IManager;
import com.wxx.unionpay.db.sp.CommonSharedPreferences;
import com.wxx.unionpay.db.sp.FetchAppConfig;

/**
 * 作者：Tangren on 2018-07-05
 * 包名：com.union
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class PosManager implements IManager {

    private int tradeSeq;

    private String mch_id;

    private String key;

    private String batchNum;

    private String macKey;

    private String TPDU;

    private String posSN;

    private String operator;

    @Override
    public void loadFromPrefs() {
        key = "F870B9AD203B37F768863EDC8652E343";
        mch_id = "438153341310001";
        posSN = "37002321";
        TPDU = "6005010000";
        operator = "099";
        macKey = FetchAppConfig.macKey();
        batchNum = FetchAppConfig.batchNum();
    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public void setHost(String host) {

    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public void setPort(int port) {

    }

    @Override
    public void setUnionPayUrl(String url) {

    }

    @Override
    public String getUnionPayUrl() {
        return null;
    }

    @Override
    public void setMachId(String mchId) {
        this.mch_id = mchId;
    }

    @Override
    public String getMchId() {
        return mch_id;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setPosSn(String sn) {
        this.posSN = sn;
    }

    @Override
    public String getPosSn() {
        return posSN;
    }

    @Override
    public String getBatchNum() {
        return batchNum;
    }

    @Override
    public void setBatchNum(String num) {
        this.batchNum = num;
        CommonSharedPreferences.put("batch_num", num);
    }

    @Override
    public String getOperatorNumber() {
        return operator;
    }

    @Override
    public void setOperatorNumber(String number) {

    }

    @Override
    public String getMacKey() {
        return macKey;
    }

    @Override
    public void setMacKey(String key) {
        this.macKey = key;
        CommonSharedPreferences.put("mac_key", key);
    }

    @Override
    public String getTPDU() {
        return TPDU;
    }

    @Override
    public void setTPDU(String var) {
        this.TPDU = var;
    }
}
