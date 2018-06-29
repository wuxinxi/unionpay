package com.wxx.unionpay.db.sp;


/**
 * TODO:获取全局的SP数据
 */

public class FetchAppConfig {

    /**
     * @return POS 终端交易流水
     */
    public static int tradeSeq() {
        return (Integer) CommonSharedPreferences.get("trade_seq", 0);
    }

    /**
     * @return 42域 商户号
     */
    public static String merchantNumber() {
        return (String) CommonSharedPreferences.get("merchant_number", "940411289996920");
    }

    /**
     * @return 批次号
     */
    public static String batchNum() {
        return (String) CommonSharedPreferences.get("batch_num", "005001");
    }

    /**
     * @return 操作员编号
     */
    public static String operatorNumber() {
        return (String) CommonSharedPreferences.get("operator_number", "099");
    }

    public static String macKey() {
        return (String) CommonSharedPreferences.get("mac_key", String.format("%016X", 0));
    }

    public static String getAidIndexList() {
        return (String) CommonSharedPreferences.get("aid_index_list", String.format("%016X", 0));
    }

    public static String getPublicIndexList() {
        return (String) CommonSharedPreferences.get("public_index_list", String.format("%016X", 0));
    }
}