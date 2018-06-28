package com.wxx.unionpay.manager;

import com.wxx.unionpay.db.sp.CommonSharedPreferences;
import com.wxx.unionpay.db.sp.FetchAppConfig;
import com.wxx.unionpay.log.MLog;

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
    private String aidList;

    @Override
    public void loadParams() {
        tradeSeq = FetchAppConfig.tradeSeq();
        mch_id = FetchAppConfig.merchantNumber();
        batchNum = FetchAppConfig.batchNum();
        operatorNumber = FetchAppConfig.operatorNumber();
        macKey = FetchAppConfig.macKey();
        aidList = FetchAppConfig.getAidIndexList();
        
       MLog.d("loadParams(PosManager.java:31)"+tradeSeq); 
        
        MLog.d("loadParams(PosManager.java:34)"+aidList);
        
        MLog.d("loadParams(PosManager.java:36)"+macKey);
        
        MLog.d("loadParams(PosManager.java:38)"+batchNum);
        
        MLog.d("loadParams(PosManager.java:40)");
    }

    @Override
    public int getTradeSeq() {
        if (tradeSeq >= 999999) {
            tradeSeq = 0;
        }
        tradeSeq += 1;
        CommonSharedPreferences.put("trade_seq", tradeSeq);
        return tradeSeq;
    }

    @Override
    public String getPosSn() {
        String posSN = "60941241";
        if (posSN.length() > 8) {
            posSN = posSN.substring(0, 8);
        } else if (posSN.length() < 8) {
            int i = 8 - posSN.length();
            for (int i1 = 0; i1 < i; i1++) {
                posSN = posSN + "0";
            }
        }
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
        return aidList;
    }

    @Override
    public void setAidIndexList(String list) {
        this.aidList = list;
        CommonSharedPreferences.put("aid_index_list", list);
    }


}
