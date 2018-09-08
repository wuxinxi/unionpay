package com.szxb.manager;

/**
 * 作者：Tangren on 2018-07-12
 * 包名：com.szxb.manager
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public interface IPosManager {

    void loadFromPrefs();

    /**
     * @param url 支付接口
     */
    void setUnionPayUrl(String url);

    String getUnionPayUrl();

    /**
     * @param mchId 银联商户号
     */
    void setUnionMchId(String mchId);

    String getUnionMchId();

    /**
     * @param key 银联秘钥
     */
    void setKey(String key);

    String getKey();

    /**
     * @param sn 银联设备号
     */
    void setUnionPosSn(String sn);

    String getUnionPosSn();

    /**
     * @param num 批次号
     */
    void setBatchNum(String num);

    String getBatchNum();


    /**
     * 银联流水号
     */
    void setTradeSeq();

    int getTradeSeq();


    /**
     * @param number 银联操作员编号
     */
    void setOperatorNumber(String number);

    String getOperatorNumber();


    /**
     * @param key 银联mac
     */
    void setMacKey(String key);

    String getMacKey();


    /**
     * @param var 银联TPDU
     */
    void setTPDU(String var);

    String getTPDU();


    String aidIndexList();

    void setAidIndexList(String list);


    String publicIndexList();

    void setpublicIndexList(String list);

    long getLastUpdateTime();

    void setCurrentUpdateTime(long time);

}
