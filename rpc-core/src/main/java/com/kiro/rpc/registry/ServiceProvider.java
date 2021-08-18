package com.kiro.rpc.registry;

/**
 * 保存和提供服务实例对象
 * @author Xufangmin
 * @create 2021-08-18-10:18
 */
public interface ServiceProvider {

    <T> void addServiceProvider(T service);

    Object getServiceProvider(String serviceName);
}
