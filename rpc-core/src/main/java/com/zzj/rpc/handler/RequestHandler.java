package com.zzj.rpc.handler;

import com.zzj.rpc.enumeration.ResponseCode;
import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zongzhaojin
 * @date 2022/4/4 13:28
 */
@Slf4j
public class RequestHandler {
    public Object handle(RpcRequest rpcRequest,Object service)  {
        Object result = null;
        try {
            // 获取实现方法的返回数据
            result = invokeTargetMethod(rpcRequest,service);
            log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.info("RequestHandler 调用或发送时有错误发生：",e);
        }
        return result;
    }
    // 方法执行控制器
    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try{
            // 根据反射获取服务的方法（必须通过方法名和方法参数两个属性才能唯一的获取一个方法）
            method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            log.error("RequestHandler 获取method失败");
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND);
        }
        // 执行方法然后将执行结果返回
        return method.invoke(service,rpcRequest.getParameters());
    }
}
