package com.wxx.test.module;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.field.Iso8583FieldType;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.log.MLog;

import java.nio.charset.Charset;

import static com.wxx.test.base.Config.dataHeader;
import static com.wxx.unionpay.util.HexUtil.byteToString;
import static com.wxx.unionpay.util.HexUtil.bytesToHexString;
import static com.wxx.unionpay.util.HexUtil.hex2byte;
import static com.wxx.unionpay.util.HexUtil.mergeByte;
import static com.wxx.unionpay.util.HexUtil.str2Bcd;
import static java.lang.System.arraycopy;

/**
 * 作者：Tangren on 2018-06-29
 * 包名：com.wxx.test.module
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class Down {
    private volatile static Down instance = null;

    private Down() {
    }

    public static Down getInstance() {
        if (instance == null) {
            synchronized (Down.class) {
                if (instance == null) {
                    instance = new Down();
                }
            }
        }
        return instance;
    }


    private Iso8583MessageFactory signFactory() {
        Iso8583MessageFactory facotry = new Iso8583MessageFactory(2, false, Charset.forName("UTF-8"), dataHeader());
        facotry.set(41, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 8))
                .set(42, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 15))
                .set(60, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
                .set(62, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_BYTE_NUMERIC, 0));//单独处理
        return facotry;
    }

    /*-----------------------------------------IC 卡参数开始------------------------------------------------------*/
    //1. POS 状态上送报文 0820  POS 终端 IC 卡参数信息查询
    public Iso8583Message message() {
        Iso8583Message message = new Iso8583Message(signFactory());
        message.setTpdu("6003030000")
                .setHeader("613100313031")
                .setMti("0820")
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(60, bytesToHexString(down_field_60("382")))
                .setValue(62, "100");
        MLog.d("message(TestActivity.java:84)" + message.getBytesString());
        return message;
    }

    //1. POS POS 参数传递报文 0800 POS 终端 IC 卡参数下载
    public Iso8583Message messageAID(String tlvKey) {
        Iso8583Message message = new Iso8583Message(signFactory());
        message.setTpdu("6003030000")
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(60, bytesToHexString(down_field_60("380")))
                .setValue(62, tlvKey);
        MLog.d("message(TestActivity.java:84)" + message.getBytesString());
        return message;
    }

    //1. POS POS 参数传递报文 0800 POS 终端 IC 卡参数下载结束
    public Iso8583Message messageAIDEnd() {
        Iso8583Message message = new Iso8583Message(signFactory());
        message.setTpdu("6003030000")
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(60, bytesToHexString(down_field_60("381")));
        MLog.d("message(TestActivity.java:84)" + message.getBytesString());
        return message;
    }


/*-----------------------------------------IC 卡参数结束------------------------------------------------------*/


/*-----------------------------------------IC 卡公钥开始------------------------------------------------------*/

    //POS 终端 IC 卡公钥信息查询
    public Iso8583Message messagePublicQuery() {
        Iso8583Message message = new Iso8583Message(signFactory());
        message.setTpdu("6003030000")
                .setHeader("613100313031")
                .setMti("0820")
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(60, bytesToHexString(down_field_60("372")))
                .setValue(62, "100");
        MLog.d("message(TestActivity.java:84)" + message.getBytesString());
        return message;
    }

    //POS 终端 IC 卡公钥下载
    public Iso8583Message messageDownPublic(String tlvKey) {
        Iso8583Message message = new Iso8583Message(signFactory());
        message.setTpdu("6003030000")
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(60, bytesToHexString(down_field_60("370")))
                .setValue(62, tlvKey);
        MLog.d("message(TestActivity.java:84)" + message.getBytesString());
        return message;
    }

    //下载结束
    public Iso8583Message messageDownPublicEnd() {
        Iso8583Message message = new Iso8583Message(signFactory());
        message.setTpdu("6003030000")
                .setHeader("613100313031")
                .setMti("0800")
                .setValue(41, UnionPayApp.getPosManager().getPosSn())
                .setValue(42, UnionPayApp.getPosManager().getMchID())
                .setValue(60, bytesToHexString(down_field_60("371")));
        MLog.d("message(TestActivity.java:84)" + message.getBytesString());
        return message;
    }




/*-----------------------------------------IC 卡公钥结束------------------------------------------------------*/


    public static byte[] down_field_60(String code) {
        byte[] field_60_1 = str2Bcd("00");
        byte[] field_60_2 = str2Bcd(UnionPayApp.getPosManager().getBatchNum());
        byte[] field_60_3 = str2Bcd(code, 1);
        return mergeByte(field_60_1, field_60_2, field_60_3);
    }

    /**
     * 保存tlv
     *
     * @param field_62
     */
    public void setParmaInfo(String field_62) {
        byte[] posInfo = hex2byte(field_62);
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

            UnionPayApp.getPosManager().setAidIndexList(sBuilder.toString());
        }

    }

    public void setQueryIndex(String var) {
        byte[] posInfo = hex2byte(var);
        byte[] index_1 = new byte[1];
        arraycopy(posInfo, 0, index_1, 0, index_1.length);
        String state = byteToString(index_1);
        if (state.equals("0")) {
            //无数据
        } else if (state.equals("1")) {
            //后续有参数信息并且可以存满
        } else if (state.equals("2")) {
            //无法存满所有数据需再次上送
            byte[] data = new byte[posInfo.length - 1];
            arraycopy(posInfo, 1, data, 0, data.length);
            StringBuilder sBuilder = new StringBuilder();
            int index = 0;
            for (int i = 0; i < data.length; i += 23) {
                byte[] rid = new byte[2];
                arraycopy(data, index, rid, 0, rid.length);
                MLog.d("setPosInfo(StateBean.java:224)" + bytesToHexString(rid));

                byte[] len = new byte[1];
                arraycopy(data, index += rid.length, len, 0, len.length);
                MLog.d("setPosInfo(StateBean.java:228)" + bytesToHexString(len));

                byte[] value = new byte[9];
                arraycopy(data, index += len.length, value, 0, value.length);
                MLog.d("setPosInfo(StateBean.java:232)" + bytesToHexString(value));
                sBuilder.append(bytesToHexString(rid)).append(bytesToHexString(len)).append(bytesToHexString(value)).append(",");
                index += value.length + 11;
            }

            UnionPayApp.getPosManager().setpublicIndexList(sBuilder.toString());
        }

    }
}
