package com.wxx.unionpay.field;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：com.wxx.unionpay.field
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class Config {

    //报文消息类型
    public static final int SGINMESSAGE = 0x01;

    public static final int AIDQUREYMESSAGE = 0x02;

    public static final int DWONAIDMESSAGE = 0x03;

    public static final int PUBLICQUREYKEYMESSAGE = 0x04;

    public static final int AIDEND = 0x05;

    public static final int PUBLICKEYINDEX = 0x06;

    public static final int PUBLICINDEXEND = 0x07;

    public static final int DOWNPUBLICKEY = 0x08;

    public static final int CARDPAY = 0x09;

    public static final int QXCORD = 0x10;

    public static final int DOWNPUBLICKEYEND = 0x11;

    public static final int KEYEVENT = 0x12;

    public static final String URL = "http://139.199.158.253/bipbus/interaction/bankjour";

    //寻卡 二维码消息类型
    public static final int BARCODE = 0x99;

    public static final int UNIONCARD = 0x98;
}
