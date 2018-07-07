package com.szxb.java8583.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.manager.BusllPosManage;

import static com.szxb.java8583.module.BaseFactory.downBaseParamFactory;
import static com.szxb.java8583.util.EncodeUtil.mergeByte;
import static com.szxb.java8583.util.EncodeUtil.str2Bcd;
import static com.szxb.java8583.util.MacEcbUtils.bytesToHexString;

/**
 * 作者：Tangren on 2018-07-07
 * 包名：com.szxb.java8583.module
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class ParamDownload {

    private volatile static ParamDownload instance = null;

    private ParamDownload() {
    }

    public static ParamDownload getInstance() {
        if (instance == null) {
            synchronized (ParamDownload.class) {
                if (instance == null) {
                    instance = new ParamDownload();
                }
            }
        }
        return instance;
    }


    /*-----------------------------------------IC 卡参数开始------------------------------------------------------*/
    //1. POS 状态上送报文 0820  POS 终端 IC 卡参数信息查询
    public Iso8583Message aidMessage() {
        Iso8583Message message = new Iso8583Message(downBaseParamFactory());
        message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setMti("0820")
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(60, bytesToHexString(down_field_60("382")))
                .setValue(62, "100");
        return message;
    }

    //1. POS POS 参数传递报文 0800 POS 终端 IC 卡参数下载
    public Iso8583Message messageAID(String tlvKey) {
        Iso8583Message message = new Iso8583Message(downBaseParamFactory());
        message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(60, bytesToHexString(down_field_60("380")))
                .setValue(62, tlvKey);
        return message;
    }

    //1. POS POS 参数传递报文 0800 POS 终端 IC 卡参数下载结束
    public Iso8583Message messageAIDEnd() {
        Iso8583Message message = new Iso8583Message(downBaseParamFactory());
        message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(60, bytesToHexString(down_field_60("381")));
        return message;
    }

    /*-----------------------------------------IC 卡参数结束------------------------------------------------------*/



/*-----------------------------------------IC 卡公钥开始------------------------------------------------------*/

    //POS 终端 IC 卡公钥信息查询
    public Iso8583Message messagePublicQuery() {
        Iso8583Message message = new Iso8583Message(downBaseParamFactory());
        message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setMti("0820")
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(60, bytesToHexString(down_field_60("372")))
                .setValue(62, "100");
        return message;
    }

    //POS 终端 IC 卡公钥下载
    public Iso8583Message messageDownPublic(String tlvKey) {
        Iso8583Message message = new Iso8583Message(downBaseParamFactory());
        message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(60, bytesToHexString(down_field_60("370")))
                .setValue(62, tlvKey);
        return message;
    }

    //下载结束
    public Iso8583Message messageDownPublicEnd() {
        Iso8583Message message = new Iso8583Message(downBaseParamFactory());
        message.setTpdu(BusllPosManage.getPosManager().getTPDU())
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(41, BusllPosManage.getPosManager().getPosSn())
                .setValue(42, BusllPosManage.getPosManager().getMchId())
                .setValue(60, bytesToHexString(down_field_60("371")));
        return message;
    }

/*-----------------------------------------IC 卡公钥结束------------------------------------------------------*/


    private static byte[] down_field_60(String code) {
        byte[] field_60_1 = str2Bcd("00");
        byte[] field_60_2 = str2Bcd(BusllPosManage.getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd(code, 1);
        return mergeByte(field_60_1, field_60_2, field_60_3);
    }

}
