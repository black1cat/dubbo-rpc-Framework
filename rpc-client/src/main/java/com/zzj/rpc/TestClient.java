package com.zzj.rpc;


import com.zzj.rpc.transport.RpcClientProxy;
import com.zzj.rpc.transport.netty.NettyClient;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        /**
         * v[1.0]-v[1.1]

        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String hello = helloService.hello(object);
        System.out.println(hello);
         */
        /**
         * v[2.0 - 2.1]
         */
        NettyClient nettyClient = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }

}
