package com.szxb.java8583.module;

import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.field.Iso8583DataHeader;
import com.szxb.java8583.field.Iso8583FieldType;

import java.nio.charset.Charset;

/**
 * 作者：Tangren on 2018-07-05
 * 包名：com.szxb.java8583.module
 * 邮箱：996489865@qq.com
 * TODO:基础Factory（用于扩展）
 */

public class BaseFactory {

    /**
     * @return 签到基础Factory
     */
    public static Iso8583MessageFactory signInBaseFactory() {
        Iso8583MessageFactory factory = new Iso8583MessageFactory(2, false, Charset.forName("UTF-8"), dataHeader());
        factory.set(11, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(41, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 8))
                .set(42, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 15))
                .set(60, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
                .set(63, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_CHAR, 0));
        return factory;
    }


    /**
     * @return 下载参数基础Factory
     */
    public static Iso8583MessageFactory downBaseParamFactory() {
        Iso8583MessageFactory factory = new Iso8583MessageFactory(2, false, Charset.forName("UTF-8"), dataHeader());
        factory.set(41, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 8))
                .set(42, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 15))
                .set(60, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
                .set(62, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_BYTE_NUMERIC, 0));//单独处理
        return factory;
    }


    /**
     * @return 消费基础Factory
     */
    public static Iso8583MessageFactory payBaseFactory() {
        Iso8583MessageFactory facotry = new Iso8583MessageFactory(2, false, Charset.forName("UTF-8"), dataHeader());
        facotry.set(2, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLVAR_NUMERIC, 0))
                .set(3, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(4, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 12))
                .set(11, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 6))
                .set(22, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 3))
                .set(23, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 4))
                .set(25, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 2))
                .set(35, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLVAR_NUMERIC, 0))
                .set(41, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 8))
                .set(42, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 15))
                .set(49, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.CHAR, 3))
                .set(53, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 16))
                .set(55, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_BYTE_NUMERIC, 0))
                .set(60, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.LLLVAR_NUMERIC, 0))
                .set(64, new Iso8583FieldType(Iso8583FieldType.FieldTypeValue.NUMERIC, 16));
        return facotry;
    }


    /**
     * @return 请求头格式
     */
    private static Iso8583DataHeader dataHeader() {
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
