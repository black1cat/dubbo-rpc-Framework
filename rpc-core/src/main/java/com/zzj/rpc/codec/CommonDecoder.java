package com.zzj.rpc.codec;

import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.entity.RpcResponse;
import com.zzj.rpc.enumeration.PackageType;
import com.zzj.rpc.enumeration.RpcError;
import com.zzj.rpc.exception.RpcException;
import com.zzj.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author zongzhaojin
 * @date 2022/4/5 16:20
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 读取魔数 判读是否为我们定义的协议包
        int magic = byteBuf.readInt();
        if(magic != MAGIC_NUMBER){
            log.error("不识别的协议包：{}",magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        // 获取协议包的类型 req还是res
        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        if(packageCode == PackageType.REQUEST_PACK.getCode()){
            packageClass = RpcRequest.class;
        }else if(packageCode == PackageType.RESPONSE_PACK.getCode()){
            packageClass = RpcResponse.class;
        }else {
            log.error("不识别的数据包：{}",packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        // 获取序列化用的序列化程序的编号
        int serializerCode = byteBuf.readInt();
        CommonSerializer byCode = CommonSerializer.getByCode(serializerCode);
        if(null == byCode) {
            log.error("不识别的反序列化器：{}",serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        // 获取数据长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        // 获取序列化的数据
        byteBuf.readBytes(bytes);
        // 进行反序列化
        Object deserialize = byCode.deserialize(bytes, packageClass);
        list.add(deserialize);
    }
}
