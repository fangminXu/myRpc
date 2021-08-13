package com.kiro.register;

/**
 * @author Xufangmin
 * @create 2021-08-13-16:13
 */
public interface ServiceRegister {
    /**
     * 将一个服务注册进注册表
     */
    <T> void register(T service);

    /**
     * 根据服务名称获得服务实体
     */
    Object getService(String interfaceName);
}
