package com.zzj.rpc.register;

public interface ServiceRegistry  {
    // 注册方法
    <T> void register(T service);
    Object getService(String serviceName);
}
