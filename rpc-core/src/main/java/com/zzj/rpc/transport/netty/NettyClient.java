package com.zzj.rpc.transport.netty;

import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.entity.RpcResponse;
import com.zzj.rpc.enumeration.RpcError;
import com.zzj.rpc.exception.RpcException;
import com.zzj.rpc.loadbalance.LoadBalancer;
import com.zzj.rpc.register.NacosServiceDiscovery;
import com.zzj.rpc.register.NacosServiceRegistry;
import com.zzj.rpc.register.ServiceDiscovery;
import com.zzj.rpc.serializer.CommonSerializer;
import com.zzj.rpc.transport.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zongzhaojin
 * @date 2022/4/5 18:49
 */
@Slf4j
public class NettyClient implements RpcClient {

    private static final Bootstrap boostrap;
    private final ServiceDiscovery serviceDiscovery;

    private CommonSerializer serializer;

    static {
        // 创建发送线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        boostrap = new Bootstrap();
        boostrap.group(group)
                .channel(NioSocketChannel.class)
                // 设置tcp心跳机制
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .option(ChannelOption.TCP_NODELAY,true);
    }
    public NettyClient(LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
    }
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null){
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        AtomicReference<Object> result = new AtomicReference<>(null);
        try{
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());

            Channel channel = ChannelProvider.get(inetSocketAddress, serializer, boostrap);

            if(channel != null){
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()) {
                        log.info(String.format("客户端发送消息: %S",rpcRequest));
                    }else{
                        log.error("发送消息是有错误发生：",future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                // 从上下文中获取数据包
                RpcResponse rpcResponse = channel.attr(key).get();
                // 返回想要的数据
                return rpcResponse.getData();

            }

        } catch (InterruptedException e) {
            log.error("发送消息时有错误发生: {}",e);
        }
        return null;
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
