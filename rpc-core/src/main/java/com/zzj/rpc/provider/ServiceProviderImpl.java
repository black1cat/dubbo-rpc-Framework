package com.zzj.rpc.provider;

import com.zzj.rpc.enumeration.RpcError;
import com.zzj.rpc.exception.RpcException;
import com.zzj.rpc.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceProviderImpl implements ServiceProvider {
   private final static Map<String,Object> serviceMap =  new ConcurrentHashMap<>();
   private final static Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized  <T> void addServiceProvider(T service,String serviceName) {
        // 判断注册列表中是否存在服务
        if(registeredService.contains(serviceName)) {
            // 存在服务则直接返回
            return ;
        }
        // 不存在将服务添加到注册表中
        registeredService.add(serviceName);
        // 获取服务的接口名称
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length < 1){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> i : interfaces) {
            // 将该对象实现的接口的完整类名作为服务名
            serviceMap.put(i.getCanonicalName(),service);
        }
        log.info("向接口: {} 注册服务：{}",interfaces,serviceName);

    }

    @Override
    public Object getServiceProvider(String serviceName) {
        // 获取服务
        Object service = serviceMap.get(serviceName);
        // 若获取不到则抛异常
        if(service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
