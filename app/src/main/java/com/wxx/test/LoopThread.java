package com.wxx.test;

import android.util.Log;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.quickstart.SingletonFactory;
import com.szxb.java8583.quickstart.special.SpecialField62;
import com.szxb.jni.libszxb;
import com.wxx.test.module.PosTransaction;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.entity.bean.PassCode;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.socket.RxSocket;
import com.wxx.unionpay.util.HexUtil;
import com.wxx.unionpay.util.MyToast;
import com.wxx.unionpay.util.TLV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static com.wxx.test.ExeType.PAY;
import static com.wxx.unionpay.util.Utils.getCurrentDate;

/**
 * 作者：Tangren on 2018-07-02
 * 包名：com.wxx.test
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

class LoopThread extends Thread {

    private String PSE = "00a404000e325041592E5359532E4444463031";
    List<String[]> listTLV;
    Map<String, String> mapTLV;
    private TERM_INFO term_info = new TERM_INFO();

    int money;

    private ArrayList<Map<String, String>> AIDparameters = null;

    private ArrayList<Map<String, String>> pubkeys = null;

    private PassCode retPassCode;

    private RxSocket socket;

    Map<String, String> aid;

    public LoopThread(RxSocket socket) {
        this.socket = socket;
        init();
    }

    @Override
    public void run() {
        super.run();

        try {

            byte[] cardType = new byte[5];
            libszxb.RFID_setAnt(0);

            libszxb.RFID_setAnt(1);

            //寻卡
            String s = libszxb.MifareGetSNR(cardType);
            if (s == null) {
                return;
            }

            MLog.d("run(LoopThread.java:29)卡号:" + HexUtil.bytesToHexString(cardType));

            //复位CPU卡
            if (libszxb.TypeA_RATS() == null) {
                MLog.d("run(LoopThread.java:32)-3");
                return;
            }

            //选择目录
            String[] retStr = libszxb.RFID_APDU(PSE);
            if (retStr == null || retStr.length == 0 || retStr[0].length() == 0) {
                MLog.d("run(LoopThread.java:36)目录选择失败>>卡无效");
                return;
            }


            if (retStr[0].equalsIgnoreCase("6a81")) {
                MLog.d("run(LoopThread.java:50)" + retStr[0]);
            } else if (retStr[0].equalsIgnoreCase("9000")) {

                MLog.d("run(LoopThread.java:93)>>>PSE OK");

                listTLV = TLV.decodingTLV(retStr[1]);
                mapTLV = TLV.decodingTLV(listTLV);

                String sAID = "00A40400" + String.format("%02x", mapTLV.get("4f").length() / 2) + mapTLV.get("4f");
                MLog.d("run(LoopThread.java:73)sAID=" + sAID);

                retStr = libszxb.RFID_APDU(sAID);

                if (retStr == null) {
                    MLog.d("run(LoopThread.java:77)-6");
                    return;
                }

                if (!retStr[0].equalsIgnoreCase("9000")) {
                    MLog.d("run(LoopThread.java:81)-7");
                    return;
                }

            } else if (retStr[0].equalsIgnoreCase("6a82")
                    || retStr[0].equalsIgnoreCase("6283")) {
                MLog.d("run(LoopThread.java:87)");


                for (int i = 0; i < AIDparameters.size(); i++) {
                    aid = AIDparameters.get(i);

                    String sAID = "00A40400" + String.format("%02d", aid.get("9f06").length() / 2) + aid.get("9f06");

                    retStr = libszxb.RFID_APDU(sAID);
                    if (null != retStr) {
                        if (retStr[0].equalsIgnoreCase("9000")) {
                            MLog.d("run(LoopThread.java:128)>>");
                            return;
                        }
                    }
                }

                assert retStr != null;
                if (!retStr[0].equalsIgnoreCase("9000")) {
                    MLog.d("run(LoopThread.java:134)-7");
                    return;
                }

            } else {
                MLog.d("run(LoopThread.java:118)>>无效卡");
                return;
            }

            listTLV = TLV.decodingTLV(retStr[1]);
            mapTLV = TLV.decodingTLV(listTLV);

            listTLV = TLV.decodingPDOL(mapTLV.get("9f38"));
            mapTLV = TLV.decodingTLV(listTLV);

            int len = 0;
            StringBuilder pDOLBuilder = new StringBuilder();

            for (String key : mapTLV.keySet()) {
                len += Integer.parseInt(mapTLV.get(key));
                switch (key) {
                    case "9f66"://终端交易属性,是否支持CDCVM
                        pDOLBuilder.append(term_info.ttq);
                        break;
                    case "9f02"://授权金额（支付金额）
                        String payMoney = String.format("%012d", money);
                        pDOLBuilder.append(payMoney);
                        retPassCode.setTAG9F02(payMoney);
                        break;
                    case "9f03"://返现金额，0
                        String str9f03 = String.format("%012d", 0);
                        pDOLBuilder.append(str9f03);
                        retPassCode.setTAG9F03(str9f03);
                        break;
                    case "9f1a"://国家代码
                        pDOLBuilder.append(term_info.terminal_country_code);
                        retPassCode.setTAG9F1A(term_info.terminal_country_code);
                        break;
                    case "95"://终端验证结果
                        String str95 = String.format("%010d", 0);
                        pDOLBuilder.append(str95);
                        retPassCode.setTAG95(str95);
                        break;
                    case "5f2a"://交易货币代码
                        pDOLBuilder.append(term_info.transaction_currency_code);
                        retPassCode.setTAG5F2A(term_info.transaction_currency_code);
                        break;
                    case "9a"://交易日期yyMMdd
                        String transDate = getCurrentDate("yyMMdd");
                        pDOLBuilder.append(transDate);
                        retPassCode.setTAG9A(transDate);
                        break;
                    case "9c"://交易类型
                        pDOLBuilder.append("00");
                        retPassCode.setTAG9C("00");
                        break;
                    case "9f37"://不可预知数
                        Random random = new Random();
                        String randomStr = String.format("%08x",
                                random.nextInt());
                        pDOLBuilder.append(randomStr);
                        retPassCode.setTAG9F37(randomStr);
                        break;
                    case "df60"://
                        pDOLBuilder.append("00");
                        break;
                    case "9f21"://时间HHmmss
                        String transTime = getCurrentDate("HHmmss");
                        pDOLBuilder.append(transTime);
                        break;
                }
            }

            String GPO = "80a80000"
                    + Integer.toHexString(len + 2) + "83"
                    + Integer.toHexString(len) + pDOLBuilder.toString();
            MLog.d("run(LoopThread.java:174)" + GPO);

            retStr = libszxb.RFID_APDU(GPO);
            if (null == retStr) {
                MLog.d("run(LoopThread.java:178)-8");
                return;
            }

            if (!retStr[0].equalsIgnoreCase("9000")) {
                Log.i(TAG, retStr[0]);
                MLog.d("run(LoopThread.java:184)-9");
                return;
            }

            UnionPayApp.getPosManager().setTradeSeq();

            listTLV = TLV.decodingTLV(retStr[1]);
            mapTLV = TLV.decodingTLV(listTLV);

            if (mapTLV.containsKey("9f36")) {
                retPassCode.setTAG9F36(mapTLV.get("9f36"));
            }

            if (mapTLV.containsKey("5f34")) {
                retPassCode.setTAG5F34(mapTLV.get("5f34"));
            }

            if (mapTLV.containsKey("9f10")) {
                retPassCode.setTAG9F10(mapTLV.get("9f10"));
            }

            if (mapTLV.containsKey("57")) {
                retPassCode.setTAG57(mapTLV.get("57"));
            }

            if (mapTLV.containsKey("9f27")) {
                retPassCode.setTAG9F27(mapTLV.get("9f27"));
                Log.d("9F27", retPassCode.getTAG9F27());
            }

            if (mapTLV.containsKey("9f26")) {
                retPassCode.setTAG9F26(mapTLV.get("9f26"));
            }

            if (mapTLV.containsKey("82")) {
                retPassCode.setTAG82(mapTLV.get("82"));
            }

            String mainCardNo = retPassCode.getTAG57().substring(0, retPassCode.getTAG57().indexOf("d"));
            String cardNum = retPassCode.getTAG5F34();
            String cardData = retPassCode.getTAG57();
            String tlv = retPassCode.toString();

            MLog.d("run(LoopThread.java:233)mainCardNo=" + mainCardNo + ",cardNum=" + cardNum + ",cardData=" + cardData + ",tlv=" + tlv);

            MLog.d("run(LoopThread.java:235)mackey=" + UnionPayApp.getPosManager().getMacKey());

            byte[] bytes = PosTransaction.getInstance().payMessage(mainCardNo, cardNum, cardData, tlv).getBytes();
            if (UnionPayApp.getPosManager().ISSSL()){
                socket.exeSSL(PAY,bytes);
            }else {
                socketExe(bytes);
            }


        } catch (Exception e) {
            e.printStackTrace();
            MLog.d("run(LoopThread.java:258)哇哦……出现了异常,请及时处理>>>" + e.toString());
        }
    }

    private void socketExe(byte[]sendData) {
        byte[] exe = socket.exe(sendData);
        Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
        factory.setSpecialFieldHandle(62, new SpecialField62());
        Iso8583Message message0810 = factory.parse(exe);

        String resValue = message0810.getValue(39).getValue();
        if (resValue.equals("00")) {
            MyToast.showToast(UnionPayApp.getInstance().getApplicationContext(), "支付成功");
        } else if (resValue.equals("51")) {
            MyToast.showToast(UnionPayApp.getInstance().getApplicationContext(), "余额不足");
        } else {
            MyToast.showToast(UnionPayApp.getInstance().getApplicationContext(), "错误[" + resValue + "]");
        }
        MLog.d("run(LoopThread.java:245)" + message0810.toFormatString());

        MLog.d("run(LoopThread.java:235)" + HexUtil.bytesToHexString(exe));
    }

    class TERM_INFO {
        String terminal_country_code; // 9F1A [BCD] : Terminal Country Code
        String TID; // 9F1C [ASC]
        String IFD; // 9F1E [ASC] : IFD Serial Number
        String transaction_currency_code; // 5F2A [BCD]
        String terminal_capabilities; // 9F33 [BIN]
        String terminal_type; // 9F35 [BCD]
        String transaction_currency_exponent; // 5F36 [BCD]
        String additional_terminal_capabilities; // 9F40 [BIN]
        String merchantName; // 9F4E [ASC]
        String ttq; // 9F66 //终端交易属性
        String statusCheckSupport; // qpbc
    }


    public void init() {

        term_info.terminal_country_code = "0156";
        term_info.TID = HexUtil.bytesToHexString("12345678".getBytes());
        term_info.IFD = HexUtil.bytesToHexString("00000000".getBytes());
        term_info.transaction_currency_code = "0156";
        term_info.terminal_capabilities = "E0E1C8";
        term_info.terminal_type = "22";
        term_info.transaction_currency_exponent = "02";
        term_info.additional_terminal_capabilities = "ff00103001";
        term_info.merchantName = HexUtil.bytesToHexString("xiaobing".getBytes());

        term_info.ttq = "a0800080";
        // term_info.ttq = new String("a8000000");// 36000080
        term_info.statusCheckSupport = "40";
        // 36000000


        retPassCode = new PassCode();

        retPassCode.setTAG9F1E(term_info.IFD);
        retPassCode.setTAG9F33(term_info.terminal_capabilities);
        retPassCode.setTAG9F35();

        if (null == AIDparameters) {
            AIDparameters = new ArrayList<Map<String, String>>();
        }

        if (null == pubkeys) {
            pubkeys = new ArrayList<Map<String, String>>();
        }

    }

}
