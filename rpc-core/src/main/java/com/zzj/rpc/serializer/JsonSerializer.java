package com.zzj.rpc.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.enumeration.SerializerCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author zongzhaojin
 * @date 2022/4/4 16:53
 */
@Slf4j
public class JsonSerializer implements CommonSerializer {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(Object obj) {
        try{
            // 对数据进行序列化 将数据从obj->byte[]
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("序列化时发生错误：{}",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try{
            // 对数据进行反序列化 将数据从byte[] -> obj
            Object obj = objectMapper.readValue(bytes, clazz);
            if(obj instanceof RpcRequest){
                obj = handlerRequest(obj);
            }
            return obj;
        }catch (IOException e) {
            log.error("反序列化时有错误发生: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /*
    这里由于使用JSON序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类型
        需要重新判断处理
     */
    private Object handlerRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for(int i = 0;i < rpcRequest.getParamTypes().length;i++){
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            if(!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())){
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes,clazz);
            }
        }
        return rpcRequest;
    }
    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
