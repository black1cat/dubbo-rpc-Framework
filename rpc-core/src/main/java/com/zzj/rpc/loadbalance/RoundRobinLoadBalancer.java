package com.zzj.rpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author zongzhaojin
 * @date 2022/4/7 15:05
 */
public class RoundRobinLoadBalancer implements LoadBalancer{
    private int index = 0;
    @Override
    public Instance select(List<Instance> instances) {
        if(index >= instances.size()){
            index %= instances.size();
        }
        return  instances.get(index++);
    }
}
