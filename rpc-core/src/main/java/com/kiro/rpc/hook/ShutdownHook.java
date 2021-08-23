package com.kiro.rpc.hook;

import com.kiro.rpc.util.NacosUtil;
import com.kiro.rpc.factory.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author Xufangmin
 * @create 2021-08-21-16:54
 */
public class ShutdownHook {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownHook.class);
    private final ExecutorService threadPool = ThreadPoolFactory.createDefaultThreadPool("shutdown-hook");
    private static final ShutdownHook SHUTDOWN_HOOK = new ShutdownHook();

    public static ShutdownHook getShutdownHook(){
        return SHUTDOWN_HOOK;
    }

    public void addClearAllHook(){
        LOGGER.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            threadPool.shutdown();
        }));
    }
}
