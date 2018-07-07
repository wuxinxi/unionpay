package com.szxb.java8583.module.manager;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：
 * 邮箱：996489865@qq.com
 * TODO:
 */

public interface IManager {

    void loadFromPrefs();

    @Deprecated
    String getHost();

    @Deprecated
    void setHost(String host);

    @Deprecated
    int getPort();

    @Deprecated
    void setPort(int port);

    void setUnionPayUrl(String url);

    String getUnionPayUrl();

    void setMachId(String mchId);

    String getMchId();

    void setKey(String key);

    String getKey();

    void setPosSn(String sn);

    String getPosSn();

    String getBatchNum();

    int getTradeSeq();

    void setTradeSeq();

    //6位
    void setBatchNum(String num);

    String getOperatorNumber();

    void setOperatorNumber(String number);

    String getMacKey();

    void setMacKey(String key);

    String getTPDU();

    void setTPDU(String var);

}
