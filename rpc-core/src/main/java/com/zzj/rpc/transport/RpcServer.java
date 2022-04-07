package com.zzj.rpc.transport;

import com.zzj.rpc.serializer.CommonSerializer;

/**
 * @author zongzhaojin
 * @date 2022/4/4 15:51
 */
public interface RpcServer {
    void start();
    <T> void publishService(T service, String serviceName);
    int DEFAULT_SERIALIZER = 0;


}
