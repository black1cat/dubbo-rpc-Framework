package com.zzj.rpc.transport;

import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@Slf4j
public class WorkerThread implements Runnable {
    Socket socket;
    Object service;
    public WorkerThread(Socket socket,Object service){
        this.socket = socket;
        this.service = service;
    }
    @Override
    public void run() {

        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
        ){
            RpcRequest rpcRequest = (RpcRequest)objectInputStream.readObject();
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            System.out.println("方法执行前");
            Object invoke = method.invoke(service, rpcRequest.getParameters());
            System.out.println("方法执行后");
            objectOutputStream.writeObject(RpcResponse.success(invoke));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("调用或发送时有错误发生：", e);
        }
    }
}
