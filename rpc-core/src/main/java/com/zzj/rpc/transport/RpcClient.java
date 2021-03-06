package com.zzj.rpc.transport;

import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.serializer.CommonSerializer;

/**
 * @author zongzhaojin
 * @date 2022/4/4 15:52
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
    void setSerializer(CommonSerializer serializer);
}
