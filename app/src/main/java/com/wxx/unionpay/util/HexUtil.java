package com.wxx.unionpay.util;

import java.io.ByteArrayOutputStream;

import static java.lang.System.arraycopy;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：com.wxx.unionpay.util
 * 邮箱：996489865@qq.com
 * TODO:转换工具类
 */

public class HexUtil {

    /**
     * 十六进制串转化为byte数组
     *
     * @return the array of byte
     */
    public static byte[] hex2byte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = Integer.valueOf(byteint).byteValue();
        }
        return b;
    }


    /**
     * byte 转hex
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * byte转string
     *
     * @param src
     * @return
     */
    public static String byteToString(byte[] src) {
        return hexStringToString(bytesToHexString(src));
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /**
     * hex to int
     *
     * @param data
     * @return
     */
    public static int byte2Int(byte[] data) {
        String hex = bytesToHexString(data);
        return Integer.parseInt(hex);
    }


    public static int byteHex2Int(byte[] data) {
        return Integer.parseInt(bytesToHexString(data), 16);
    }

    /**
     * BCD码转为10进制串(阿拉伯数据)<br/>
     * * BCD与十进制字符串的转换.<br/>
     * BCD（Binary Coded Decimal）是用二进制编码表示的十进制数，<br/>
     * 十进制数采用0~9十个数字，是人们最常用的。在计算机中，同一个数可以用两种BCD格式来表示：<br/>
     * ①压缩的BCD码 ②非压缩的BCD码<br/>
     * 压缩的BCD码：<br/>
     * 压缩的BCD码用4位二进制数表示一个十进制数位，整个十进制数用一串BCD码来表示。<br/>
     * 例如，十进制数59表示成压缩的BCD码为0101 1001，十进制数1946表示成压缩的BCD码为0001 1001 0100 0110。<br/>
     * 非压缩的BCD码：<br/>
     * 非压缩的BCD码用8位二进制数表示一个十进制数位，其中低4位是BCD码，高4位是0。<br/>
     * 例如，十进制数78表示成压缩的BCD码为0111 1000。<br/>
     * 从键盘输入数据时，计算机接收的是ASCII码，要将ASCII码表示的数转换成BCD码是很简单的，<br/>
     * 只要把ASCII码的高4位清零即可。反之，如果要把BCD码转换成ASII码，只要把BCD码 "或|"00110000即可。
     *
     * @param bytes BCD码
     * @return String 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        if (bytes.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            int h = ((aByte & 0xff) >> 4) + 48;
            sb.append((char) h);
            int l = (aByte & 0x0f) + 48;
            sb.append((char) l);
        }
        return sb.toString();
    }

    /**
     * 10进制串转为BCD码<br/>
     *
     * @param data 10进制串
     * @return byte[] BCD码
     */
    public static byte[] str2Bcd(String data) {
        return str2Bcd(data, 0);
    }

    public static byte[] str2Bcd(String data, int flag, int len) {
        int dataLen = data.length();
        if (dataLen < len) {
            int addLen = len - dataLen;
            for (int i = 0; i < addLen; i++) {
                data = "0" + data;
            }
        }
        return str2Bcd(data, flag);
    }


    /**
     * 10进制串转为BCD码<br/>
     *
     * @param data 10进制串
     * @param flag 0:默认左补0，1右补0
     * @return
     */
    public static byte[] str2Bcd(String data, int flag) {
        if (data.length() == 0) {
            return new byte[0];
        }

        String str = data;
        // 奇数个数字需左补零
        if (str.length() % 2 != 0) {
            str = flag == 0 ? "0" + str : str + "0";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = str.toCharArray();
        for (int i = 0; i < cs.length; i += 2) {
            int high = cs[i] - 48;
            int low = cs[i + 1] - 48;
            baos.write(high << 4 | low);
        }
        return baos.toByteArray();
    }


    /**
     * 合并byte数组
     *
     * @param datas .
     * @return .
     */
    public static byte[] mergeByte(byte[]... datas) {
        int length = 0;
        byte[] endData = new byte[2048];
        for (byte[] data : datas) {
            arraycopy(data, 0, endData, length, data.length);
            length += data.length;
        }
        byte[] data = new byte[length];
        arraycopy(endData, 0, data, 0, length);
        return data;
    }

    /**
     * 求长度用的 int转byte[]
     *
     * @param value
     * @param len
     * @return
     */
    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

}
