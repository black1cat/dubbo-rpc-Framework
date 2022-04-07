package com.zzj.rpc.transport;

import com.zzj.rpc.annotation.Service;
import com.zzj.rpc.annotation.ServiceScan;
import com.zzj.rpc.enumeration.RpcError;
import com.zzj.rpc.exception.RpcException;
import com.zzj.rpc.provider.ServiceProvider;
import com.zzj.rpc.register.ServiceRegister;
import com.zzj.rpc.serializer.CommonSerializer;
import com.zzj.rpc.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @author zongzhaojin
 * @date 2022/4/7 17:57
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer {

    protected String host;
    protected int port;

    protected ServiceRegister serviceRegister;
    protected ServiceProvider serviceProvider;

    @Override
    public <T> void publishService(T service,String serviceName) {
        serviceProvider.addServiceProvider(service,serviceName);
        serviceRegister.register(serviceName,new InetSocketAddress(host,port));
    }
    public void scanServices(){
        System.out.println("1");
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try{
            startClass = Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(ServiceScan.class)){
                log.error("启动类缺少@ServiceScan");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if("".equals(basePackage)){
            basePackage = mainClassName.substring(0,mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classes = ReflectUtil.getClasses(basePackage);
        for(Class<?> clazz:classes){
            if(clazz.isAnnotationPresent(Service.class)){
                String name = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    log.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                if("".equals(name)){
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface : interfaces) {
                        publishService(obj,oneInterface.getCanonicalName());
                    }
                }else{
                    publishService(obj,name);
                }
            }
        }
    }
}
