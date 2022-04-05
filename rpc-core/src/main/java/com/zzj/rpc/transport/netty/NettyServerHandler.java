package com.zzj.rpc.transport.netty;

import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.entity.RpcResponse;
import com.zzj.rpc.handler.RequestHandler;
import com.zzj.rpc.register.DefaultServiceRegistry;
import com.zzj.rpc.register.ServiceRegistry;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zongzhaojin
 * @date 2022/4/5 18:24
 * 处理接收到请求后的业务
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;
    static {
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest msg) throws Exception {
        try {
            log.info("服务器接收到请求：{}", msg);
            // 获取接口名称
            String interfaceName = msg.getInterfaceName();
            // 找到对应的实现类
            Object service = serviceRegistry.getService(interfaceName);
            // 调用实现类对应的方式 获取返回结果
            Object result = requestHandler.handle(msg, service);
            // 写入数据并推送
            ChannelFuture future = channelHandlerContext.writeAndFlush(RpcResponse.success(result));
            // 监听客户端channel是否关闭
            future.addListener(ChannelFutureListener.CLOSE);
        }finally {
            // 发送数据后释放数据
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
