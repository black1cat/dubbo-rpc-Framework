package com.zzj.rpc.register;

import java.net.InetSocketAddress;

/**
 * @author zongzhaojin
 * @date 2022/4/6 16:34
 * 注册中心
 */
public interface ServiceRegister {
    /**
     * 将服务的名称和地址注册进服务注册中心
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
