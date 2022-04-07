package com.zzj.rpc;



import com.zzj.rpc.annotation.ServiceScan;

import com.zzj.rpc.provider.ServiceProviderImpl;
import com.zzj.rpc.serializer.KryoSerializer;
import com.zzj.rpc.transport.RpcClient;
import com.zzj.rpc.transport.RpcClientProxy;
import com.zzj.rpc.transport.netty.NettyClient;
import com.zzj.rpc.transport.netty.NettyServer;

@ServiceScan
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
//        HelloServiceImpl helloService = new HelloServiceImpl();
//        ServiceProviderImpl defaultServiceRegistry = new ServiceProviderImpl();
//        defaultServiceRegistry.register(helloService);
//        NettyServer nettyServer = new NettyServer(host, port, serviceProvider, serviceRegister);
//        nettyServer.start(9999);
        /**
         *
         */


//        HelloService helloService = new HelloServiceImpl();
//        NettyServer server = new NettyServer("127.0.0.1", 9999);
//        server.setSerializer(new KryoSerializer());
//        server.publishService(helloService, HelloService.class);

        NettyServer nettyServer = new NettyServer("127.0.0.1", 9999);
        nettyServer.start();
    }
}
