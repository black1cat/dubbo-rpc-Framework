package com.zzj.rpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author zongzhaojin
 * @date 2022/4/6 21:44
 */
public interface LoadBalancer {
    Instance select(List<Instance> instances);
}
