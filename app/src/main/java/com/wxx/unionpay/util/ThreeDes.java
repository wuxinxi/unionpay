package com.wxx.unionpay.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import static com.wxx.unionpay.util.HexUtil.hex2byte;


public class ThreeDes {

    /**
     * 加密
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    public static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(hex2byte(password));
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密//正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(hex2byte(password));
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    //转换成十六进制字符串  
    public static String byte2Hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) hs = hs + ":";
        }
        return hs.toUpperCase();
    }

    public static byte[] hex(String username) {
        String key = "test";//关键字
        String f = DigestUtils.md5Hex(username + key);
        byte[] bkeys = new String(f).getBytes();
        byte[] enk = new byte[24];
        for (int i = 0; i < 24; i++) {
            enk[i] = bkeys[i];
        }
        return enk;
    }

    static String DES = "DES/ECB/NoPadding";
    static String TriDes = "DESede/ECB/NoPadding";

    /**
     * 3des加密
     *
     * @param key  密钥
     * @param data 明文数据 16进制且长度为16的整数倍
     * @return 密文数据
     */
    public static byte[] Union3DesEncrypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            int len = data.length;
            if (data.length % 8 != 0) {
                len = data.length - data.length % 8 + 8;
            }
            byte[] needData = null;
            if (len != 0)
                needData = new byte[len];

            for (int i = 0; i < len; i++) {
                needData[i] = 0x00;
            }

            System.arraycopy(data, 0, needData, 0, data.length);

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }

            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(needData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 3des解密
     *
     * @param key  密钥
     * @param data 密文数据 16进制且长度为16的整数倍
     * @return 明文数据
     */
    public static byte[] Union3DesDecrypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            int len = data.length;
            if (data.length % 8 != 0) {
                len = data.length - data.length % 8 + 8;
            }
            byte[] needData = null;
            if (len != 0)
                needData = new byte[len];

            for (int i = 0; i < len; i++) {
                needData[i] = 0x00;
            }

            System.arraycopy(data, 0, needData, 0, data.length);

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }

            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(needData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}  