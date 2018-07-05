package com.wxx.unionpay.manager;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：com.wxx.unionpay.manager
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public interface IPosManager {
    /**
     * @return 流水号
     */
    int getTradeSeq();

    void setTradeSeq();

    String getIP();

    int getPort();

    String getMchId();

    String getKey();

    /**
     * @return 获取设备SN号
     */
    String getPosSn();

    /**
     * @return 商户号
     */
    String getMchID();

    /**
     * @return 批次号
     */
    String getBatchNum();

    void setBatchNum(String num);

    /**
     * @return 操作员编号
     */
    String getOperatorNumber();

    String getMacKey();

    void setMacKey(String key);

    String aidIndexList();

    void setAidIndexList(String list);

    String publicIndexList();

    void setpublicIndexList(String list);

    void setCurrentUpdateTime(long date);

    long getLastUpdateTime();

    String getTPDU();

    boolean ISSSL();

    void loadParams(boolean ssl);
}
