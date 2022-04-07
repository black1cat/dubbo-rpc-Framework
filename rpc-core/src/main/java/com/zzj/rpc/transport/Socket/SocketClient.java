package com.zzj.rpc.transport.Socket;

import com.zzj.rpc.entity.RpcRequest;
import com.zzj.rpc.serializer.CommonSerializer;
import com.zzj.rpc.transport.RpcClient;
import lombok.extern.slf4j.Slf4j;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public class SocketClient implements RpcClient {
    public Object sendRequest(RpcRequest rpcRequest, String host, int port) {
        try(Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("调用时发生错误：",e);
            return null;
        }
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        return null;
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {

    }
}
