package com.wxx.unionpay.entity.bean;

import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.util.HexUtil;

/**
 * 作者：Evergarden on 2017-09-08 18:20 QQ：1941042402
 */

public class PassCode {
    // 9F36 02 01 87
    // 95 05 00 00 00 00 00
    // 9F1E 08 30 30 30 30 30 30 30 30
    // 9F10 13 07 0D 01 03 A0 10 00 01 0A 01 00 00 00 00 00 63 9F A1 D2
    // 9F26 08 B8 54 86 36 04 21 42 17
    // 9F27 01 80
    // 9F37 04 C3 26 23 03
    // 9A 03 16 10 26
    // 9C 01 00
    // 9F02 06 00 00 00 00 00 01
    // 5F2A 02 01 56
    // 82 02 7C 00
    // 9F1A 02 01 56
    // 9F03 06 00 00 00 00 00 00
    // 9F33 03 E0 E1 C8

    private String TAG9F36;
    private String TAG95;
    private String TAG9F1E;
    private String TAG9F10;
    private String TAG9F26;
    private String TAG9F27;
    private String TAG9F37;
    private String TAG9A;
    private String TAG9C;
    private String TAG9F02;
    private String TAG5F2A;
    private String TAG82;
    private String TAG9F1A;
    private String TAG9F03;
    private String TAG9F33;
    private String TAG57;
    private String TAG5F34;

    private String TAG9F35;
    private String TAG9F41;


    public String getTAG9F35() {
        return "9F350122";
    }

    public void setTAG9F35() {
        this.TAG9F35 = "9F350122";
    }

    @Override
    public String toString() {

        byte[] bytes = HexUtil.int2Bytes((getTAG9F10().length() / 2), 1);
        String code = "9F3602" + getTAG9F36() + "9505" + getTAG95() + "9F1E08" + getTAG9F1E() + "9F10" + HexUtil.bytesToHexString(bytes) + getTAG9F10() + "9F2608" + getTAG9F26() + "9F2701" + getTAG9F27() + "9F3704" + getTAG9F37()
                + "9A03" + getTAG9A() + "9C01" + getTAG9C() + "9F0206" + getTAG9F02() + "5F2A02" + getTAG5F2A() + "8202" + getTAG82() + "9F1A02" + getTAG9F1A() + "9F0306" + getTAG9F03() + "9F3303" + getTAG9F33() + getTAG9F35() + getTAG9F41();
        MLog.d("toString(PassCode.java:86)" + code);
        return code;
    }

    public String getTAG9F41() {
        return "9F410400" +  String.format("%06d", UnionPayApp.getPosManager().getTradeSeq());
    }

    public PassCode setTAG9F41(String TAG9F41) {
        this.TAG9F41 = "9F410400" +  String.format("%06d", UnionPayApp.getPosManager().getTradeSeq());
        return this;
    }

    public String getTAG5F34() {
        return TAG5F34;
    }

    public void setTAG5F34(String TAG5F34) {
        this.TAG5F34 = TAG5F34;
    }

    public PassCode() {
        // TODO Auto-generated constructor stub
    }

    public PassCode Bulider() {
        return this;
    }

    public String getTAG9F36() {
        return TAG9F36;
    }

    public PassCode setTAG9F36(String TAG9F36) {
        this.TAG9F36 = TAG9F36;
        return this;
    }

    public String getTAG95() {
        return TAG95;
    }

    public PassCode setTAG95(String TAG95) {
        this.TAG95 = TAG95;
        return this;
    }

    public String getTAG9F1E() {
        return TAG9F1E;
    }

    public PassCode setTAG9F1E(String TAG9F1E) {
        this.TAG9F1E = TAG9F1E;
        return this;
    }

    public String getTAG9F10() {
        return TAG9F10;
    }

    public PassCode setTAG9F10(String TAG9F10) {
        this.TAG9F10 = TAG9F10;
        return this;
    }

    public String getTAG9F26() {
        return TAG9F26;
    }

    public PassCode setTAG9F26(String TAG9F26) {
        this.TAG9F26 = TAG9F26;
        return this;
    }

    public String getTAG9F27() {
        return "80";
    }

    public PassCode setTAG9F27(String TAG9F27) {
        this.TAG9F27 = "80";
        return this;
    }

    public String getTAG9F37() {
        return TAG9F37;
    }

    public PassCode setTAG9F37(String TAG9F37) {
        this.TAG9F37 = TAG9F37;
        return this;
    }

    public String getTAG9A() {
        return TAG9A;
    }

    public PassCode setTAG9A(String TAG9A) {
        this.TAG9A = TAG9A;
        return this;
    }

    public String getTAG9C() {
        return TAG9C;
    }

    public PassCode setTAG9C(String TAG9C) {
        this.TAG9C = TAG9C;
        return this;
    }

    public String getTAG9F02() {
        return TAG9F02;
    }

    public PassCode setTAG9F02(String TAG9F02) {
        this.TAG9F02 = TAG9F02;
        return this;
    }

    public String getTAG5F2A() {
        return TAG5F2A;
    }

    public PassCode setTAG5F2A(String TAG5F2A) {
        this.TAG5F2A = TAG5F2A;
        return this;
    }

    public String getTAG82() {
        return TAG82;
    }

    public PassCode setTAG82(String TAG82) {
        this.TAG82 = TAG82;
        return this;
    }

    public String getTAG9F1A() {
        return "0156";
    }

    public PassCode setTAG9F1A(String TAG9F1A) {
        this.TAG9F1A = "0156";
        return this;
    }

    public String getTAG9F03() {
        return TAG9F03;
    }

    public PassCode setTAG9F03(String TAG9F03) {
        this.TAG9F03 = TAG9F03;
        return this;
    }

    public String getTAG9F33() {
        return TAG9F33;

    }

    public PassCode setTAG9F33(String TAG9F33) {
        this.TAG9F33 = TAG9F33;
        return this;
    }

    public String getTAG57() {
        return TAG57;

    }

    public PassCode setTAG57(String TAG57) {
        TAG57 = TAG57.substring(0, TAG57.indexOf("f"));
        this.TAG57 = TAG57;
        return this;
    }


}
