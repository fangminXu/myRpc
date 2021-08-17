package com.kiro.rpc.registry;

/**
 * @author Xufangmin
 * @create 2021-08-16-13:10
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册进注册表
     */
    <T> void register(T service);

    /**
     * 根据服务名称获取服务实体
     */
    Object getService(String serviceName);
}
