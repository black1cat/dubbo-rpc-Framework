package com.zzj.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zzj.rpc.enumeration.RpcError;
import com.zzj.rpc.exception.RpcException;
import com.zzj.rpc.loadbalance.LoadBalancer;
import com.zzj.rpc.loadbalance.RandomLoadBalancer;
import com.zzj.rpc.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zongzhaojin
 * @date 2022/4/6 16:43
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegister {
    private static final String SERVER_ADDR = "127.0.0.1:8848";

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try{
            NacosUtil.registerService(serviceName,inetSocketAddress);
        } catch (NacosException e) {
            log.error("注册服务时有错误发生：{}",e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }


}
