package com.zzj.rpc;


import com.zzj.rpc.register.DefaultServiceRegistry;
import com.zzj.rpc.transport.Socket.SocketServer;
import com.zzj.rpc.transport.netty.NettyServer;

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

        HelloServiceImpl helloService = new HelloServiceImpl();
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        // 将服务注册到注册列表里
        defaultServiceRegistry.register(helloService);
        SocketServer rpcServer = new SocketServer(defaultServiceRegistry);
        rpcServer.start(9000);
         */
        /**
         * v[2.0-2.1]服务端测试
         */
        HelloServiceImpl helloService = new HelloServiceImpl();
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        defaultServiceRegistry.register(helloService);
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(9999);
    }
}
