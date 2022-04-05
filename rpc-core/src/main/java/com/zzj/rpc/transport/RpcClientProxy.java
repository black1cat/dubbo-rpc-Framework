package com.zzj.rpc.transport;


import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.entity.RpcResponse;
import com.zzj.rpc.transport.Socket.SocketClient;
import com.zzj.rpc.transport.netty.NettyClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private final  RpcClient client;
    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("调用方法：{}#{}",method.getDeclaringClass().getName(),method.getName());
        // 构造请求数据
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        return client.sendRequest(rpcRequest);
    }
}
