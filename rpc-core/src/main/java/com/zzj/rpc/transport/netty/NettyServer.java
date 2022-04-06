package com.zzj.rpc.transport.netty;

import com.zzj.rpc.codec.CommonDecoder;
import com.zzj.rpc.codec.CommonEncoder;
import com.zzj.rpc.serializer.JsonSerializer;
import com.zzj.rpc.serializer.KryoSerializer;
import com.zzj.rpc.transport.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zongzhaojin
 * @date 2022/4/4 16:01
 */
@Slf4j
public class NettyServer implements RpcServer {
    @Override
    public void start(int port) {

        // 首先构造两个线程组
        // bossGroup用来接收客户端传过来的请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // workerGroup 用来处理请求完之后的后续操作
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            // 创建服务端启动辅助类（提供网络层配置的容器）
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置reactor
            serverBootstrap.group(bossGroup,workerGroup)
                    // 设置Channel通道类型 本次选择 NioSocketChannel 代表异步的客户端 TCP Socket 连接.
                    .channel(NioServerSocketChannel.class)
                    // 用于指定在服务端启动过程中的一些逻辑 此处用来调用打印日志 级别为INFO级别
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // option设置参数
                    // 设置服务端接收队列的长度，如果队列满了 客户端将被拒绝连接
                    .option(ChannelOption.SO_BACKLOG,256)
                    // 开启TCP心跳机制 默认心跳间隔为2小时
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    // tcp参数 是否立即发送数据 true代表关闭Nagle算法 false表示开启
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 有连接到达时会创建一个channel
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // ChannelHandler链表 用来处理网络请求
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 在流水线pipline后面 进行装配  增加数据读写、处理业务的handler
                            // 编码处理器
                            pipeline.addLast(new CommonEncoder(new KryoSerializer()));
                            // 解码处理器
                            pipeline.addLast(new CommonDecoder());
                            // 业务处理器
                            pipeline.addLast(new NettyServerHandler());

                        }
                    });
            // 绑定server 通过调用sync（）同步方法阻塞知道绑定成功
            ChannelFuture future = serverBootstrap.bind(port).sync();
            // 监听关闭事件 程序会一直等待 直到channel关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务器时发生错误：{}",e);
        }finally {
            // 释放掉所有的资源包括创建的线程
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
