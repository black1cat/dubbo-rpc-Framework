package com.zzj.rpc;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(HelloObject object) {
        log.info("接收到:{}",object.getMessage());
        return "这是调用的返回值，id=" + object.getId();
    }
}
