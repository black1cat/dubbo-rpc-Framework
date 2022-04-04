package com.zzj.rpc.exception;

import com.zzj.rpc.enumeration.RpcError;

/**
 * @author zongzhaojin
 * @date 2022/4/4 13:04
 * RPC调用异常
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
