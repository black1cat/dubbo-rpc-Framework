package com.zzj.rpc.transport.netty;

import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.entity.RpcResponse;
import com.zzj.rpc.serializer.JsonSerializer;
import com.zzj.rpc.serializer.KryoSerializer;
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
    public NettyClient(String host,int port){
        this.host = host;
        this.port = port;
    }
    static {
        // 创建发送线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        boostrap = new Bootstrap();
        boostrap.group(group)
                .channel(NioSocketChannel.class)
                // 设置tcp心跳机制
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 添加解码，编码。业务处理器
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new KryoSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null){
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        AtomicReference<Object> result = new AtomicReference<>(null);
        try{
            // tcp与服务器建立连接
            ChannelFuture future = boostrap.connect(host, port).sync();
            log.info("客户端连接到服务器{}:{}",host,port);
            // 获取处理器
            Channel channel = future.channel();
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
