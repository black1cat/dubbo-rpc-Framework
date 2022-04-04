package com.zzj.rpc;


import com.zzj.rpc.register.DefaultServiceRegistry;
import com.zzj.rpc.transport.RpcServer;

public class TestServer {
    public static void main(String[] args) {
        /**
         * v[1.0]服务端测试

        HelloServiceImpl helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.register(helloService,9000);
         */
        /**
         * v[1.1]服务端测试
         */
        HelloServiceImpl helloService = new HelloServiceImpl();
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        // 将服务注册到注册列表里
        defaultServiceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(defaultServiceRegistry);
        rpcServer.start(9000);
    }
}
