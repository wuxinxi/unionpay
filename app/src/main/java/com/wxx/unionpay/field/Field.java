package com.wxx.unionpay.field;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：com.wxx.unionpay.field
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class Field {
    /**
     * 内容长度
     */
    private int len;

    /**
     * 报文类型
     */
    private int type;

    /**
     * 发送的数据
     */
    private byte[] sendData;

    /**
     * 实际长度
     */
    private int length;

    /**
     * 数据长度byte[]
     */
    private byte[] lenByte[];

    /**
     * 具体域byte[]数据
     */
    private byte[] dataByte[];


    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getSendData() {
        return sendData;
    }

    public void setSendData(int offset,byte[] sendData) {

    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[][] getLenByte() {
        return lenByte;
    }

    public void setLenByte(byte[][] lenByte) {
        this.lenByte = lenByte;
    }

    public byte[][] getDataByte() {
        return dataByte;
    }

    public void setDataByte(byte[][] dataByte) {
        this.dataByte = dataByte;
    }
}
