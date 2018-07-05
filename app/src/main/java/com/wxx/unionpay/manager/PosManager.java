package com.wxx.unionpay.manager;

import com.wxx.unionpay.db.sp.CommonSharedPreferences;
import com.wxx.unionpay.db.sp.FetchAppConfig;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：com.wxx.unionpay.manager
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class PosManager implements IPosManager {

    private int tradeSeq;
    private String mch_id;
    private String batchNum;
    private String operatorNumber;
    private String macKey;
    private String aidIndexList;
    private String publicIndexList;
    private long lastUpdateTime;

    private String ip;
    private int port;
    private String key;
    private String posSN;
    private String TPDU;

    private boolean isSSL;

    @Override
    public void loadParams(boolean ssl) {
        tradeSeq = FetchAppConfig.tradeSeq();
        mch_id = FetchAppConfig.merchantNumber();
        batchNum = FetchAppConfig.batchNum();
        operatorNumber = FetchAppConfig.operatorNumber();
        macKey = FetchAppConfig.macKey();
        aidIndexList = FetchAppConfig.getAidIndexList();
        publicIndexList = FetchAppConfig.getPublicIndexList();
        lastUpdateTime = FetchAppConfig.getLastUpdateTime();

        if (ssl) {
            ip = "120.204.69.139";
            port = 30000;
            key = "F870B9AD203B37F768863EDC8652E343";
            mch_id = "438153341310001";
            posSN = "37002321";
            TPDU = "6005010000";
            isSSL = true;
        } else {
            ip = "183.237.71.61";
            port = 22102;
            key = "9D0BDA257668DF8ADC4C5B856EA26298";
            mch_id = "940411289996920";
            posSN = "60941241";
            TPDU = "6003030000";
            isSSL = false;
        }
    }

    @Override
    public int getTradeSeq() {
        return tradeSeq;
    }

    @Override
    public void setTradeSeq() {
        if (tradeSeq >= 999999) {
            tradeSeq = 0;
        }
        tradeSeq += 1;
        CommonSharedPreferences.put("trade_seq", tradeSeq);
    }

    @Override
    public String getIP() {
        return ip;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getMchId() {
        return mch_id;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getPosSn() {
//        String posSN = "60941241";
//        if (posSN.length() > 8) {
//            posSN = posSN.substring(0, 8);
//        } else if (posSN.length() < 8) {
//            int i = 8 - posSN.length();
//            for (int i1 = 0; i1 < i; i1++) {
//                posSN = posSN + "0";
//            }
//        }
        return posSN;
    }

    @Override
    public String getMchID() {
        return mch_id;
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
        return operatorNumber;
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
    public String aidIndexList() {
        return aidIndexList;
    }

    @Override
    public void setAidIndexList(String list) {
        this.aidIndexList = list;
        CommonSharedPreferences.put("aid_index_list", list);
    }

    @Override
    public String publicIndexList() {
        return publicIndexList;
    }

    @Override
    public void setpublicIndexList(String list) {
        this.publicIndexList = list;
        CommonSharedPreferences.put("public_index_list", list);
    }

    @Override
    public void setCurrentUpdateTime(long date) {
        this.lastUpdateTime = date;
        CommonSharedPreferences.put("last_update_time", date);
    }

    @Override
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public String getTPDU() {
        return TPDU;
    }

    @Override
    public boolean ISSSL() {
        return isSSL;
    }


}
