package com.zzj.rpc.serializer;

/**
 * @author zongzhaojin
 * @date 2022/4/4 16:47
 */
public interface CommonSerializer {
    /**
     * 序列化
     */
    byte[] serialize(Object obj);
    /**
     * 反序列化
     */
    Object deserialize(byte[] bytes,Class<?> clazz);
    /**
     * 获得该序列化器的编号
     * @return
     */
    int getCode();
    static CommonSerializer getByCode(int code){
        switch (code){
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
