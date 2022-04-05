package com.zzj.rpc.transport.netty;

import com.zzj.rpc.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zongzhaojin
 * @date 2022/4/5 18:40
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
      try{
          log.info(String.format("客户端接收到消息：%s",rpcResponse));
          AttributeKey<Object> key = AttributeKey.valueOf("rpcResponse");
          // 将返回结果直接放到上下文中
          channelHandlerContext.channel().attr(key).set(rpcResponse);
          channelHandlerContext.channel().close();
      }finally {
          // 释放资源
          ReferenceCountUtil.release(rpcResponse);
      }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
