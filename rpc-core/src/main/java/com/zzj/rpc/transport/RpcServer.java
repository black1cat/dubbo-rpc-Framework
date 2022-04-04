package com.zzj.rpc.transport;

import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class RpcServer {
    public final ExecutorService threadPool;

    public RpcServer() {
        // 五个核心线程
        int corePoolSize = 5;
        // 最大线程数量
        int maximumPoolSize = 50;
        // 存活时间
        long keepAliveTime = 60;
        ArrayBlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,workingQueue,threadFactory);
    }
    public void register(Object service,int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                System.out.println("客户端连接！IP为：" + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket,service));
            }
        }catch (IOException e){
            log.error("连接时有错误发生：",e);
        }
    }
}
