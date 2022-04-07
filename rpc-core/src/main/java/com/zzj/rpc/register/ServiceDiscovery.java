package com.zzj.rpc.register;

import java.net.InetSocketAddress;

/**
 * @author zongzhaojin
 * @date 2022/4/7 15:19
 */
public interface ServiceDiscovery {
    /**
     * 服务发现 根据服务的名称从注册中心获取一个服务提供者的地址
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
