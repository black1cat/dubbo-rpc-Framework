package com.zzj.rpc.handler;

import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.entity.RpcResponse;
import com.zzj.rpc.provider.ServiceProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author zongzhaojin
 * @date 2022/4/4 14:29
 */
@AllArgsConstructor
@Slf4j
public class RequesthandlerThread implements Runnable {
    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceProvider serviceProvider;

    @Override
    public void run() {
        // 通过socket创建输入输出流
        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());) {
            // 读取客户端发送过来的数据强转成RpcRequest
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 获取接口名字
            String interfaceName = rpcRequest.getInterfaceName();
            // 通过接口名称读取注册表里的服务
            Object service = serviceProvider.getServiceProvider(interfaceName);
            // 通过requestHandler.handle(）获得方法执行后的返回值
            Object result = requestHandler.handle(rpcRequest, service);
            // 将返回值写入输出流中
            objectOutputStream.writeObject(RpcResponse.success(result));
            // 向客户端推送消息
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("RequesthandlerThread error");
        }
    }
}
