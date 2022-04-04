package com.zzj.rpc.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务器异常"),
    LOGIN_ERROR(500210,"用户名或密码不正确"),
    MOBILE_ERROR(500211,"用户手机号码不正确"),
    SESSION_ERROR(500214,"用户不存在"),
    BIND_ERROR(500212,"参数校验异常"),
    EMPTY_STOCK(500500,"库存不足"),
    REPEATE_ERROR(500501,"该商品每人限购一件"),
    // 订单模块5003xx
    ORDER_NOT_EXIST(500301,"订单错误"),
    ;

    private final Integer code;
    private final String message;


}
