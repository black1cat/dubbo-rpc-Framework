package com.zzj.rpc.provider;

public interface ServiceProvider {
    // 注册方法
    <T> void addServiceProvider(T service);
    Object getServiceProvider (String serviceName);
}
