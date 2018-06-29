package com.wxx.unionpay.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者：Tangren on 2018-06-29
 * 包名：com.wxx.unionpay.entity
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@Entity
public class Public_370 {
    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String publicKey;

    private String reserve;

    @Generated(hash = 1620701968)
    public Public_370(Long id, String publicKey, String reserve) {
        this.id = id;
        this.publicKey = publicKey;
        this.reserve = reserve;
    }

    @Generated(hash = 1209563606)
    public Public_370() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getReserve() {
        return this.reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }
}
