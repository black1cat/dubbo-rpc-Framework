package com.zzj.rpc.codec;

import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.enumeration.PackageType;
import com.zzj.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author zongzhaojin
 * @date 2022/4/4 16:43
 */
public class CommonEncoder extends MessageToByteEncoder {
    /**
     * +---------------+---------------+-----------------+-------------+
     * |  Magic Number |  Package Type | Serializer Type | Data Length |
     * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
     * +---------------+---------------+-----------------+-------------+
     * |                          Data Bytes                           |
     * |                   Length: ${Data Length}                      |
     * +---------------------------------------------------------------+
     * 数据包结构
     */



    // 魔数 标识一个协议包
    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    private final CommonSerializer commonSerializer;

    public CommonEncoder(CommonSerializer commonSerializer) {
        this.commonSerializer = commonSerializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        // 先写入魔数
        byteBuf.writeInt(MAGIC_NUMBER);
        // 判断是请求还是响应 写入对应的package Type
        if(msg instanceof RpcRequest){
            byteBuf.writeInt(PackageType.REQUEST_PACK.getCode());
        }else {
            byteBuf.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        // 写入对应的序列化的类型
        byteBuf.writeInt(commonSerializer.getCode());
        // 获取序列化后的数据
        byte[] data = commonSerializer.serialize(msg);
        // 写入数据长度
        byteBuf.writeInt(data.length);
        // 写入数据
        byteBuf.writeBytes(data);
    }
}
