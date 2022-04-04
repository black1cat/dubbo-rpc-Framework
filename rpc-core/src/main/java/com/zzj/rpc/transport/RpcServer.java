package com.zzj.rpc.transport;

import com.zzj.rpc.handler.RequestHandler;
import com.zzj.rpc.handler.RequesthandlerThread;
import com.zzj.rpc.register.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class RpcServer {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 30;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    public final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;

    // 初始化注册表类、初始化创建一个线程池
    public RpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        ArrayBlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,workingQueue,threadFactory);
    }
   public void start(int port) {
        // 启动SocketServer
        try(ServerSocket serverSocket = new ServerSocket(port)){
            log.info("服务器启动......");
            Socket socket;
            // 获取连接请求
            while((socket = serverSocket.accept()) != null){
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                // 获取到请求创建一个线程执行RequesthandlerThread方法
                threadPool.execute(new RequesthandlerThread(socket,requestHandler,serviceRegistry));
            }
            // 关闭线程池
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:", e);
        }
   }
}
