package com.wxx.test.base;

import com.szxb.java8583.field.Iso8583DataHeader;
import com.szxb.java8583.field.Iso8583FieldType;

/**
 * 作者：Tangren on 2018-06-29
 * 包名：com.wxx.test.base
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class Config {

    public  static Iso8583DataHeader dataHeader() {
        //定义报文头数据格式
        return new Iso8583DataHeader(
                // tpdu		BCD编码，5个字节（10个字符长度）
                new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 10),
                // header	BCD编码，6个字节长度
                new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 12),
                // mti		BCD编码，2个字节长度
                new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 4),
                // bitmap	BCD编码，8个字节长度
                new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 16)
        );
    }
}
