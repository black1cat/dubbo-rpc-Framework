package com.zzj.rpc.transport;


import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;
    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        System.out.println("调用getProxy");
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke调用成功");
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new RpcClient();
        RpcResponse rpcResponse = (RpcResponse) rpcClient.sendRequest(rpcRequest, host, port);
        Object data = rpcResponse.getData();

        return data;
    }
}
