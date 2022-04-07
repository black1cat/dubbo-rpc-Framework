package com.zzj.rpc.hook;

import com.zzj.rpc.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zongzhaojin
 * @date 2022/4/6 21:21
 */
@Slf4j
public class ShutdownHook {
    private static final ShutdownHook shutdownHook = new ShutdownHook();
    public  static ShutdownHook getShutdownHook(){
        return shutdownHook;
    }
    public void addClearAllHook(){
        log.info("关闭后将自动注销所有的服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            NacosUtil.clearRegistry();
        }));
    }
}
