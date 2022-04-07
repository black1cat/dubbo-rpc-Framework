package com.zzj.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zzj.rpc.loadbalance.LoadBalancer;
import com.zzj.rpc.loadbalance.RandomLoadBalancer;
import com.zzj.rpc.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zongzhaojin
 * @date 2022/4/7 15:21
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        if(loadBalancer == null){
            this.loadBalancer = new RandomLoadBalancer();
        }else {
            this.loadBalancer = loadBalancer;
        }
    }


    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try{
            List<Instance> instances = NacosUtil.getInstances(serviceName);
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生",e);
        }
        return null;
    }
}
