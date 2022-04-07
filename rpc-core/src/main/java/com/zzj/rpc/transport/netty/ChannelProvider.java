package com.zzj.rpc.transport.netty;

import com.zzj.rpc.codec.CommonDecoder;
import com.zzj.rpc.codec.CommonEncoder;
import com.zzj.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;


import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * @author zongzhaojin
 * @date 2022/4/6 17:58
 */
public class ChannelProvider {
    private static EventLoopGroup eventLoopGroup;
    private static final int MAX_RETRY_COUNT = 5;
    private static Channel channel = null;
    private static Bootstrap bootstrap;

    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer,Bootstrap bs) throws InterruptedException {
        bootstrap = bs;
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new CommonEncoder(serializer))
                        .addLast(new CommonDecoder())
                        .addLast(new NettyClientHandler());
            }
        });
        ChannelFuture future = bootstrap.connect(inetSocketAddress.getAddress(), inetSocketAddress.getPort()).sync();
        channel = future.channel();
        return channel;
    }

}
