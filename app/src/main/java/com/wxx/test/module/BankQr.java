package com.wxx.test.module;

/**
 * 作者：Tangren on 2018-09-10
 * 包名：com.wxx.test.module
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class BankQr {
    private volatile static BankQr instance = null;

    private BankQr() {
    }

    public static BankQr getInstance() {
        if (instance == null) {
            synchronized (BankQr.class) {
                if (instance == null) {
                    instance = new BankQr();
                }
            }
        }
        return instance;
    }

//
//    private Iso8583Message qrPayMessageData(String qrCode) {
//        Iso8583Message message = new Iso8583Message(BaseFactory.payBaseFactory());
//        message.setMti("0200")
//                .setValue(3, "000000")//交易处理码
//                .setValue(4, String.format("%012d", 1))//交易金额
//                .setValue(11, String.format("%06d", UnionPayApp.getPosManager().getTradeSeq()))//受卡方系统跟踪号,流水号
//                .setValue(22, "072")//服务点输入方式码
//                .setValue(23, cardNum)//卡片序列号
//                .setValue(25, "00")//服务点条件码
//                .setValue(35, cardData)//2 磁道数据
//                .setValue(41, UnionPayApp.getPosManager().getPosSn())
//                .setValue(42, UnionPayApp.getPosManager().getMchID())
//                .setValue(49, "156")//交易货币代码
//                .setValue(53, String.format("%016d", 0))//安全控制信息
//                .setValue(55, tlv55)
//                .setValue(60, bytesToHexString(pay_field_60()))
//                .setValue(64, "");
//        return message;
//    }
}

