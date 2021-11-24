package com.xxx.common.auth.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Payload<T> {
    private String id; //tokenId
    private T userInfo; //保存的信息
    private Date expiration; //过期时间
}